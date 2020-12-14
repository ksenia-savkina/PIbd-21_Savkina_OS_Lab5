package com.company;

import java.util.Random;
import java.util.Vector;

public class Core {
    private Vector<Process> firstProcesses;
    private Vector<Process> secondProcesses;

    public void firstPlan() {
        int kvant = 20;
        int time = 0;
        while (firstProcesses.size() > 0) {
            Process process;
            Thread thread;
            int tempKvant;
            for (int i = 0; i < firstProcesses.size(); i++) {
                tempKvant = kvant;
                process = firstProcesses.get(i);
                System.out.println("Квант = " + tempKvant);
                while (tempKvant > 0 && process.getSizeThread() > 0) {
                    thread = process.createThread();
                    if (process.waiting(thread)) {
                        time += process.calculateIOtime(thread);
                        System.out.println("Процесс с ID " + process.getPID() + " ожидает устроиство ввода/вывода");
                    } else {
                        time += 0;
                    }
                    tempKvant = thread.runTime(tempKvant);
                    if (thread != null)
                        time += thread.getTime();
                    if (thread.getTime() == 0)
                        process.removeThread(thread);
                }
                System.out.println("Процесс с ID " + process.getPID() + " завершен");
                if (process.getSizeThread() == 0)
                    firstProcesses.remove(process);
                System.out.println("Квант = " + tempKvant);
                System.out.println();
            }
        }
        System.out.println("Прошло времени без блокировки процессов: " + time);
        System.out.println();
    }

    public void secondPlan() {
        int kvant = 20;
        int time = 0;
        Vector<Process> blockedProcesses = new Vector<>();
        while (secondProcesses.size() > 0) {
            Process process;
            Thread thread = null;
            int tempKvant;
            for (int i = 0; i < secondProcesses.size(); i++) {
                tempKvant = kvant;
                process = secondProcesses.get(i);
                if (blockedProcesses.contains(process)) {
                    continue;
                }
                System.out.println("Квант = " + tempKvant);
                while (tempKvant > 0 && process.getSizeThread() > 0) {
                    thread = process.createThread();
                    if (process.waiting(thread)) {
                        System.out.println("Процесс с ID " + process.getPID() + " заблокирован");
                        blockedProcesses.add(process);
                    }
                    tempKvant = thread.runTime(tempKvant);
                    if (thread != null)
                        time += thread.getTime();
                    if (thread.getTime() == 0)
                        process.removeThread(thread);

                    for (Process curProcess : blockedProcesses) {
                        curProcess.IOwait(tempKvant, thread, process.getPID());
                    }
                    for (int j = 0; j < blockedProcesses.size(); j++) {
                        Process curProcess = blockedProcesses.get(j);
                        {
                            if (curProcess.waiting(thread)) {
                                blockedProcesses.remove(j);
                            }
                        }
                    }
                }
                System.out.println("Процесс с ID " + process.getPID() + " завершен");
                if (process.getSizeThread() == 0)
                    secondProcesses.remove(process);
                System.out.println("Квант = " + tempKvant);
                System.out.println();
            }
            if (secondProcesses.size() > 0 && blockedProcesses.size() == secondProcesses.size()) {
                System.out.println("В ожидании находятся все процессы");
                int tempTime = Integer.MAX_VALUE;
                for (Process curProcess : blockedProcesses)
                    tempTime = Integer.min(tempTime, curProcess.calculateIOtime(thread));
                for (Process curProcess : blockedProcesses)
                    curProcess.IOwait(tempTime, thread, curProcess.getPID());
                for (int j = 0; j < blockedProcesses.size(); j++) {
                    Process curProcess = blockedProcesses.get(j);
                    {
                        if (curProcess.waiting(thread)) {
                            blockedProcesses.remove(j);
                        }
                    }
                }
                time += tempTime;
                System.out.println("Прошло времени в ожидании : " + tempTime);
            }
        }
        System.out.println("Прошло времени с блокировкой процессов: " + time);
    }


    public void createProcess() {
        Random rnd = new Random();
        firstProcesses = new Vector<>();
        secondProcesses = new Vector<>();
        int size = rnd.nextInt(5) + 7;
        int index = rnd.nextInt(size / 2);
        int threadCount = rnd.nextInt(5) + 4;
        for (int i = 0; i < size; i++) {
            Process fp = new Process(i);
            Process sp = new Process(i);
            if (i == index) {
                int IO = rnd.nextInt(3) + 1;
                fp.launch(threadCount, IO);
                sp.launch(threadCount, IO);
            } else {
                fp.launch(threadCount, 0);
                sp.launch(threadCount, 0);
            }
            firstProcesses.add(fp);
            secondProcesses.add(sp);
        }
    }
}

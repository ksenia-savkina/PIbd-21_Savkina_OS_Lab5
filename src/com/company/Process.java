package com.company;

import java.util.ArrayList;
import java.util.Vector;

public class Process {
    private final int pID;
    private Vector<Thread> threads;
    private int extraTID = 0;

    public Process(int pID) {
        this.pID = pID;
        threads = new Vector<>();
    }

    public void launch(int threadCount, int IO) {
        for (int i = 0; i < threadCount; i++) {
            threads.add(new Thread(getExtraTID(), IO));
        }
    }

    public int calculateIOtime(Thread thread) {
        int k = 0;
        for (int i = 0; i < thread.getIOtime().size(); i++) {
            k += thread.getIOtime().get(i);
        }
        return k;
    }

    public boolean waiting(Thread thread) {
        return calculateIOtime(thread) > 0;
    }

    public void IOwait(int count, Thread thread, int number) {
        ArrayList<Integer> curIOtime = thread.getIOtime();
        if (waiting(thread)) {
            for (int i = 0; i < curIOtime.size(); i++) {
                if (curIOtime.get(i) > 0) {
                    System.out.println("Процесс с ID " + number + " ожидает устроиство ввода/вывода №" + i);
                    if (curIOtime.get(i) > count) {
                        curIOtime.set(i, curIOtime.get(i) - count);
                    } else {
                        curIOtime.set(i, 0);
                    }
                }
            }
        }
    }

    public Thread createThread() {
        if (getSizeThread() > 0) {
            return threads.get(getSizeThread() - 1);
        }
        return null;
    }

    public void removeThread(Thread thread) {
        if (thread != null)
            threads.remove(thread);
    }

    private int getExtraTID() {
        extraTID++;
        return extraTID;
    }

    public int getPID() {
        return pID;
    }

    public int getSizeThread() {
        return threads.size();
    }
}

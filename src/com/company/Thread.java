package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Thread {

    private final int tID;
    private int time;
    private int IO;
    private ArrayList<Integer> IOtime;

    public Thread(int tID, int countIO) {
        Random rnd = new Random();
        this.tID = tID;
        this.time = rnd.nextInt(25) + 5;
        this.IO = countIO;
        IOtime = new ArrayList<>();
        for (int i = 0; i < countIO; i++)
            IOtime.add(rnd.nextInt(300 - 20) + 20);
    }

    public ArrayList<Integer> getIOtime() {
        return IOtime;
    }

    public int runTime(int number) {
        int k;
        if (number < time) {
            k = 0;
            time -= number;
        } else {
            k = number - time;
            time = 0;
        }
        return k;
    }

    public int getTime() {
        return time;
    }
}

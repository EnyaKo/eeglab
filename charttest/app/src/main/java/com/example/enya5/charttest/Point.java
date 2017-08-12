package com.example.enya5.charttest;

import java.util.Random;

public class Point {

    private static final Random RANDOM = new Random();
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point randomPoint(int x) {
        return new Point(x, RANDOM.nextInt(25)-15);
    }

}
package com.example.enya5.charttest;


public class Timer {
    public long min;
    public long sec;
    public long startTime;

    public void startTimer(){
        startTime = System.currentTimeMillis(); //取得目前時間
    }

    public void updateTimer(){
        Long spentTime = System.currentTimeMillis() - startTime;
        min = (spentTime/1000)/60;  //計算目前已過分鐘數
        sec = (spentTime/1000) % 60;   //計算目前已過秒數
    }

}

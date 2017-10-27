package com.example.enyako.eegtest;

/**
 * Created by EnyaKo on 9/23/2017.
 */
import java.util.List;
import java.util.ArrayList;

public class Result{
    public boolean musician;
    private int flag = 0;
    String[] titles; // 定義折線的名稱
    List<float[]> x = new ArrayList<float[]>(); // 點的x坐標
    List<float[]> y = new ArrayList<float[]>(); // 點的y坐標

    public Result(){
        super();
    }

    public void isMusician(boolean isMusician){
        musician = isMusician;
    }

    public void setData(String[] string, double[] xValues, double[] yValues){
        if(flag == 0 && string.equals("chord")){
            titles = new String[] { "chord", "interval" };
            flag = 1;
        }else if(flag == 0 && string.equals("interval")){
            titles = new String[] { "interval", "chord" };
            flag = 1;
        }// end of if else


    }

    /*public void addNewPoints(int length) {
        float[] temp1 = new float[length];
        float[] temp2 = new float[length];
        for(int i = 0; i < length; i++){
            temp1[i] = i+1;
            temp2[i] = 1;
        }// end of for

        x.add(temp1);
        y.add(temp2);
    }*/

    public void addNewPoints(int length, float[] yValues) {
        float[] temp = new float[length];
        for(int i = 0; i < length; i++){
            temp[i] = i+1;
        }// end of for

        x.add(temp);
        y.add(yValues);
    }


}
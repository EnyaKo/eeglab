package com.example.enyako.chartmusic;

/**
 * Created by EnyaKo on 9/23/2017.
 */
import java.util.List;
import java.util.ArrayList;

public class Result{
    public boolean musician;
    private int flag = 0;
    String[] titles; // 定義折線的名稱
    List<double[]> x = new ArrayList<double[]>(); // 點的x坐標
    List<double[]> y = new ArrayList<double[]>(); // 點的y坐標

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

    public void addNewPoints(double[] xValues, double[] yValues) {
        x.add(xValues);
        y.add(yValues);
    }


}
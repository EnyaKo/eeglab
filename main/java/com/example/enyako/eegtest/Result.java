package com.example.enyako.eegtest;

/**
 * Created by EnyaKo on 9/23/2017.
 */
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Result{
    public boolean musician;
    public float[] N1 = new float[2];;
    public float[] P2 = new float[2];
    public float N1dif, intNtest, chNtest;
    public float P2dif, intPtest, chPtest;
    String[] titles; // 定義折線的名稱
    List<float[]> x = new ArrayList<float[]>(); // 點的x坐標
    List<float[]> y = new ArrayList<float[]>(); // 點的y坐標

    public Result(){
        super();
        musician = false;
    }

    public void isMusician(){
        /*for(int i = 0; i < titles.length; i++){
            float[] temp = x.get(i);
            N1[i] = temp[37];
            for(int j = 37; j < 48; j++){
                if(temp[j] < N1[i]){
                    N1[i] = temp[j];
                }// end of if
            }// end of for j
            P2[i] = temp[50];
            for(int j = 50; j < 63; j++){
                if(temp[j] > P2[i]){
                    P2[i] = temp[j];
                }// end of if
            }// end of for j
        }// end of for i
        */

        chNtest = N1[0];
        intNtest = N1[1];
        chPtest = P2[0];
        intPtest = P2[1];
        N1dif = Math.abs(N1[1]-N1[0]);
        P2dif = Math.abs(P2[1]-P2[0]);
        if( ( 8 > N1dif && N1dif > 2) && ( 8 > P2dif && P2dif > 2) ){
            musician = true;
        }// end of if
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

    public void addNewPoints(int ind, int length, float[] yValues) {
        float[] temp = new float[length];
        for(int i = 0; i < length; i++){
            temp[i] = ((i+1)*8)-200;
        }// end of for

        N1[ind] = yValues[37];
        for(int j = 37; j < 48; j++){
            if(yValues[j] < N1[ind]){
                N1[ind] = yValues[j];
            }// end of if
        }// end of for j
        P2[ind] = yValues[50];
        for(int j = 50; j < 63; j++) {
            if (yValues[j] > P2[ind]) {
                P2[ind] = yValues[j];
            }// end of if
        }// end of for j

        x.add(temp);
        y.add(yValues);

    }


}
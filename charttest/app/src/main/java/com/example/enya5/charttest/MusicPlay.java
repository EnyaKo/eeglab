package com.example.enya5.charttest;

import java.util.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MusicPlay {
    public boolean start;
    public boolean stop;

    final int[] p = {
            R.raw.music1, R.raw.music2, R.raw.music3, R.raw.music4, R.raw.music5, R.raw.music6, R.raw.music7, R.raw.music8, R.raw.music9, R.raw.music10,
            R.raw.music11, R.raw.music12, R.raw.music13, R.raw.music14, R.raw.music15, R.raw.music16, R.raw.music17, R.raw.music18, R.raw.music19, R.raw.music20,
            R.raw.music21, R.raw.music22, R.raw.music23, R.raw.music24, R.raw.music25, R.raw.music26, R.raw.music27, R.raw.music28, R.raw.music29, R.raw.music30,
            R.raw.music31, R.raw.music32, R.raw.music33, R.raw.music34, R.raw.music35, R.raw.music36, R.raw.music37, R.raw.music38, R.raw.music39, R.raw.music40,
            R.raw.music41, R.raw.music42, R.raw.music43, R.raw.music44, R.raw.music45, R.raw.music46, R.raw.music47, R.raw.music48, R.raw.music49, R.raw.music50,
            R.raw.music51, R.raw.music52, R.raw.music53, R.raw.music54, R.raw.music55, R.raw.music56, R.raw.music57, R.raw.music58, R.raw.music59, R.raw.music60
    };

    class map {
        String path;
        int number;

        public map(String path, Integer number) {
            this.path = path;
            this.number = number;
        }
    }

    private int times = 2;
    map[] data = new map[60 * times];
    private static final int REQUEST_WRITE_STORAGE = 112;
    private String OutputData = "start\n";

    public MusicPlay() {
        this.start = false;
        this.stop = false;
    }

    public void musicStart(MainActivity Activity) {
        this.start = true;
        for (int i = 0; i < times; i++) {
            for (int j = 0; j < 60; j++) {
                int tem = j + 1;
                data[i * 60 + j] = new map("music" + tem, p[j]);
            }
        }
        Collections.shuffle(Arrays.asList(data));
        Out(OutputData, Activity);
        play(-1, Activity);
    }

    public void musicStop() {
        this.stop = true;
    }

    private void Out(String tem, MainActivity Activity) {

        boolean hasPermission = (ContextCompat.checkSelfPermission(Activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(Activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        } else {
            String sdPath = Environment.getExternalStorageDirectory() + "/data/";
            try {
                FileWriter fw = new FileWriter(sdPath + "MyData.txt", false);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(tem);
                bw.newLine();
                bw.close();
            } catch (Exception e) {
                Log.i("Failed to save", e.getMessage());
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        this.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String sdPath = Environment.getExternalStorageDirectory() + "/data/";
                    try {
                        FileWriter fw = new FileWriter(sdPath + "MyData.txt", false);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(OutputData);
                        bw.newLine();
                        bw.close();
                    } catch (Exception e) {
                        Log.i("Failed to save", e.getMessage());
                    }

                } else {
                    //not allowed to write
                }
            }
        }
    }

    private void play(int num, final MainActivity Activity) {

        num++;
        final int tem = num;
        Random r = new Random();
        int ran = r.nextInt(2500 - 2000) + 2000;

        try {
            //delay
            Thread.sleep(ran);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //TextView t1 = (TextView)findViewById(R.id.textView);
        //TextView t2 = (TextView)findViewById(R.id.textView2);
        //t1.setText("random time:"+ran);
        //t2.setText(num+"  : "+data[num].path);
        OutputData += "\n" + data[num].path;

        MediaPlayer mp = MediaPlayer.create(Activity, data[num].number);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                if (tem < 59 + 60 * (times - 1)) {
                    play(tem, Activity);
                } else {
                    Out(OutputData, Activity);
                }
            }

            ;
        });

    }
}

package com.example.enyako.eegtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
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

import android.os.Handler;
import android.content.Intent;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private Long startTime;
    private Handler handler = new Handler();
    private Long minius;
    private Long seconds;
    private Long minisec;

    final int [] p={
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
            this.number=number;
        }
    }
    private int times=1;
    map [] data = new map[60*times];
    private static final int REQUEST_WRITE_STORAGE = 112;
    private String OutputData = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void resultbtn(View view){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ResultActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

    public void playbtn(View view) {
        startTime = System.currentTimeMillis(); //取得目前時間
        handler.removeCallbacks(updateTimer); //設定定時要執行的方法
        handler.postDelayed(updateTimer, 1000); //設定Delay的時間
        for(int i=0; i<times ; i++){
            for(int j=0; j<60; j++){
                int tem=j+1;
                data[i*60+j]=new map(""+tem,p[j]);
            }
        }
        Collections.shuffle(Arrays.asList(data));
        play(-1);
    }

    private void Out(String tem){

        boolean hasPermission = (ContextCompat.checkSelfPermission(this,  Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},  REQUEST_WRITE_STORAGE);
        }else{
            String sdPath = Environment.getExternalStorageDirectory() + "/data/";
            try{
                FileWriter fw = new FileWriter(sdPath + "MyData.txt", false);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(tem);
                bw.close();
            }catch (Exception e){
                Log.i("Failed to save", e.getMessage());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    String sdPath = Environment.getExternalStorageDirectory() + "/data/";
                    try{
                        FileWriter fw = new FileWriter(sdPath + "MyData.txt", false);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(OutputData);
                        bw.close();
                    }catch (Exception e){
                        Log.i("Failed to save", e.getMessage());
                    }

                } else
                {
                    //not allowed to write
                }
            }
        }
    }

    private void play(int num){

        num++;
        final int tem=num;
        Random r = new Random();
        int ran=r.nextInt(2500 - 2000) + 2000;

        try {
            //delay
            Thread.sleep(ran);

        }catch (Exception e){
            e.printStackTrace();
        }

        if(seconds !=null){
            OutputData+="\n"+data[num].path+" "+minius+":"+seconds+":"+minisec;
        }else if(num==59){
            OutputData+="\n"+data[num].path+" "+minius+":"+seconds+":"+minisec;
        }else{
            OutputData+=data[num].path+" "+"0:0:250";
        }

        MediaPlayer mp= MediaPlayer.create(this, data[num].number);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                if(tem<59+60*(times-1)){
                    play(tem);
                }else{
                    Out(OutputData);
                }
            };
        });
    }
    //固定要執行的方法
    private Runnable updateTimer = new Runnable() {
        public void run() {
            //final TextView time = (TextView) findViewById(R.id.timer);
            Long spentTime = System.currentTimeMillis() - startTime;
            minius = (spentTime/1000)/60; //計算目前已過分鐘數
            seconds = (spentTime/1000) % 60; //計算目前已過秒數
            minisec = spentTime % 1000;
            //time.setText(minius+":"+seconds);
            handler.postDelayed(this, 1000);
        }
    };



}

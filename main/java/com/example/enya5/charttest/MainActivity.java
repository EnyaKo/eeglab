package com.example.enya5.charttest;

// for display
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Handler;
import android.os.HandlerThread;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.GraphicalView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity{

    private LineChart lineChart;
    private MusicPlay musicPlay = new MusicPlay();
    private GraphicalView graphView;
    private int sampleRate = 128;   // Sample Rate: 1 data per 128ms
    private int event = 60;         // Event total amount = 60
    private int TimePerEvent = 450;// Time per event = 450 ms
    private int eventSpace;        //  Time Between each event : random 2000ms~2500ms
    private Long startTime;
    public Long min;
    public Long sec;
    private Handler mUI_Handler = new Handler();
    private Handler handler = new Handler();
    private Handler mThreadHandler;
    private HandlerThread mThread;


    @Override
    public void onCreate(Bundle savedInstanceState) {
               super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button2 = (Button)findViewById(R.id.button02);
        button2.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ResultActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        //musicPlay.musicStart(this);

    }

    public void playBtn01(View view){

        createChart();
        musicPlay.musicStart(this);

        startTime = System.currentTimeMillis(); //取得目前時間
        handler.removeCallbacks(r2);    //設定定時要執行的方法
        handler.postDelayed(r2, 1000);  //設定Delay的時間

        mThread = new HandlerThread("name");
        mThread.start();
        mThreadHandler=new Handler(mThread.getLooper());
        //mThreadHandler.post(r3);
        mThreadHandler.post(r1);
    }

    private Runnable r1=new Runnable () {
        public void run() {

            for (int i = 0; ; i++) {
                //if(musicPlay.stop == true){     // stop refreshing graph when music stops
                //    break;
                //}// end of if
                final int x = i;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO display realtime brainwave: add new point
                        lineChart.addNewPoints(Point.randomPoint(x));
                        graphView.repaint();    // graph refresh: paint a new sample
                    }
                });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // manage error ...
                }// end of exceptionn
            }// end of for
        }
    };

    private Runnable r2=new Runnable () {
        public void run() {
            final TextView time = (TextView) findViewById(R.id.timer);
            Long spentTime = System.currentTimeMillis() - startTime;
            min = (spentTime/1000)/60;  //計算目前已過分鐘數
            sec = (spentTime/1000) % 60;   //計算目前已過秒數
            time.setText(min+":"+sec);
            handler.postDelayed(this, 1000);
        }
    };

    private Runnable r3=new Runnable() {
        public void run() {
            setMusic();
        }
    };

    protected void setMusic(){
        musicPlay.musicStart(this);
    }

    protected void createChart(){
        lineChart = new LineChart();
        graphView = lineChart.getView(this);
        //setContentView(chart);
        LinearLayout layout = (LinearLayout) findViewById(R.id.displayECG);
        layout.addView(graphView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1000));
    }

    // 定義折線圖名稱
/*    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
                                    String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor) {
        renderer.setChartTitle(title); // 折線圖名稱
        renderer.setBackgroundColor(Color.BLACK);
        renderer.setApplyBackgroundColor(true);
        renderer.setChartTitleTextSize(40); // 折線圖名稱字形大小
        renderer.setLabelsTextSize(40); // Label Text Size
        renderer.setAxisTitleTextSize(40); // Axis Title Text Size
        renderer.setXTitle(xTitle); // X軸名稱
        renderer.setYTitle(yTitle); // Y軸名稱
        renderer.setXAxisMin(xMin); // X軸顯示最小值
        renderer.setXAxisMax(xMax); // X軸顯示最大值
        renderer.setXLabelsColor(Color.WHITE); // X軸線顏色
        renderer.setYAxisMin(yMin); // Y軸顯示最小值
        renderer.setYAxisMax(yMax); // Y軸顯示最大值
        renderer.setAxesColor(axesColor); // 設定坐標軸顏色
        renderer.setYLabelsColor(0, Color.WHITE); // Y軸線顏色
        renderer.setLabelsColor(Color.YELLOW); // 設定標籤顏色
        renderer.setMarginsColor(Color.BLACK); // 設定背景顏色
        renderer.setShowGrid(true); // 設定格線
        renderer.setZoomButtonsVisible(true);
    }

    // 定義折線圖的格式
    private XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles, boolean fill) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            r.setFillPoints(fill);
            renderer.addSeriesRenderer(r); //將座標變成線加入圖中顯示
        }
        return renderer;
    }

    // 資料處理
    private XYMultipleSeriesDataset buildDatset(String[] titles, List<double[]> xValues,
                                                List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

        int length = titles.length; // 折線數量
        for (int i = 0; i < length; i++) {
            // XYseries對象,用於提供繪製的點集合的資料
            XYSeries series = new XYSeries(titles[i]); // 依據每條線的名稱新增
            double[] xV = xValues.get(i); // 獲取第i條線的資料
            double[] yV = yValues.get(i);
            int seriesLength = xV.length; // 有幾個點

            for (int k = 0; k < seriesLength; k++) // 每條線裡有幾個點
            {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
        return dataset;
    }*/
}

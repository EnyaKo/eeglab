package com.example.enyako.eegtest;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.GraphicalView;



public class ResultActivity extends AppCompatActivity {
    private Button btnAnalysis;
    private TextView textAnalysisOk;
    private Button btnDrawResult;
    float [] chordSum = {1};
    float [] intervalSum = {1};

    private Button mBtGoBack;

    private GraphicalView graphView;
    public Result result = new Result();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // TODO button go back to MainActivity
        mBtGoBack = (Button) findViewById(R.id.bt_go_back);
        mBtGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ResultActivity.this, MainActivity.class);
                startActivity(intent);
                ResultActivity.this.finish();
            }
        });

        // TODO button analysis
        btnAnalysis = (Button) findViewById(R.id.button);
        textAnalysisOk = (TextView) findViewById(R.id.textView2);

        btnAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED)) {  //檢查sdcard是否存在
                        return;
                    }

                    File SDCardpath = Environment.getExternalStorageDirectory();                         //取得sdcard路徑
                    FileReader myDataPath = new FileReader(SDCardpath.getParent() + "/" + SDCardpath.getName() + "/data/input.txt");    //打開檔案路徑
                    BufferedReader myBufferedReader = new BufferedReader(myDataPath);                                                //將檔案讀至緩衝區



                    ArrayList mylist = new ArrayList();             //因不確定檔案有幾筆，使用動態的arraylist

                    String readInputTime = myBufferedReader.readLine();
                    String [] inputTime = new String[4];
                    int inputStart;
                    inputTime = readInputTime.split("\\s|:");
                    inputStart = 1000 * ((( Integer.parseInt(inputTime[0]) * 60+Integer.parseInt(inputTime[1]) ) * 60) + Integer.parseInt(inputTime[2]))+Integer.parseInt(inputTime[3]);

                    String myTextLine = myBufferedReader.readLine();//鼎鈞會先給我時間再給我資料，但我應該不會用到時間
                    //myTextLine = myBufferedReader.readLine();       //讀進資料
                    String tempstring;

                    while (myTextLine != null)                         //讀取檔案直到EOF
                    {
                        tempstring = myTextLine;
                        mylist.add(tempstring);                     //加入arraylist
                        //myTextLine = myBufferedReader.readLine();   //下一筆時間
                        myTextLine = myBufferedReader.readLine();   //下一筆資料
                    }


                    int count = mylist.size();                      //取得共有幾筆資料
                    float [] array = new float[count / 4 + 1];          //濾波前的原始資料
                    float [] result = new float[count / 4 + 1];         //濾波後的資料
                    float [] src = new float[count / 4 + 1];
                    float [] dest = new float[count / 4 + 1];

                    for(int x = 0; x < count; x++) {                //每8筆資料取一筆，samplerate 1000->125 (Hz)
                        if(x % 4 == 0){
                            array[x / 4] = Float.parseFloat((String) mylist.get(x));    //將arraylist中資料轉成float存進array[]
                        }
                    }

                    System.arraycopy(array, 0, src, 0, array.length);   //copy array[] 進 src[] 以進行濾波

                    //******************************以下為low pass filter**************************/
                    final int NZEROS = 6;
                    final int NPOLES = 6;
                    final float GAIN =  40.9836f;

                    float[] xv = new float[NZEROS+1];
                    float[] yv = new float[NPOLES+1];

                    for (int i = 0; i < array.length; i++)
                    {
                        xv[0] = xv[1];
                        xv[1] = xv[2];
                        xv[2] = xv[3];
                        xv[3] = xv[4];
                        xv[4] = xv[5];
                        xv[5] = xv[6];
                        xv[6] = src[i] / GAIN;

                        yv[0] = yv[1];
                        yv[1] = yv[2];
                        yv[2] = yv[3];
                        yv[3] = yv[4];
                        yv[4] = yv[5];
                        yv[5] = yv[6];
                        yv[6] = (xv[0] + xv[6])
                                + 6.0f * (xv[1] + xv[5])
                                + 15.0f* (xv[2] + xv[4])
                                + 20.0f * xv[3]
                                + ( -0.0019f * yv[0])
                                + (  0.0076f * yv[1])
                                + ( -0.1197f * yv[2])
                                + (  0.1130f * yv[3])
                                + ( -0.7987f * yv[4])
                                + (  0.2374f * yv[5]);

                        dest[i] = yv[6];    //將結果存入 dest[]
                    }
                    //******************************以上為low pass filter**************************//
                    System.arraycopy(dest, 0, result, 0, dest.length);  //copy dest[] 進 result[]


                    /*FileReader timeDataPath = new FileReader(SDCardpath.getParent() + "/" + SDCardpath.getName() + "/data/timedata.txt");
                    BufferedReader timeBufferedReader = new BufferedReader(timeDataPath);

                    ArrayList timelist = new ArrayList();

                    String str = timeBufferedReader.readLine();
                    String tempStr;

                    while (str!=null)
                    {
                        tempStr = str;
                        timelist.add(tempStr);
                        str = timeBufferedReader.readLine();
                    }

                    count = timelist.size();

                    String [] timeArray = new String[count];

                    String [] tempArray = new String[2];
                    int [][] timeData = new int[count][2];

                    for(int x = 0; x < count; x++) {
                        timeArray[x] = (String)timelist.get(x);
                        tempArray = timeArray[x].split("\\s|:");

                        int num=0;
                        int time=0;

                        num = Integer.parseInt(tempArray[0]);
                        time = Integer.parseInt(tempArray[1]);

                        timeData[x][0] = num;
                        timeData[x][1] = time;
                    }*/

                    ArrayList timelist = new ArrayList();       //因不確定檔案有幾筆，使用動態的arraylist
                    int musicStart = 0;
                    try {
                        FileReader timeDataPath = new FileReader(SDCardpath.getParent() + "/" + SDCardpath.getName() + "/data/MyData.txt"); //打開紀錄時間的檔案
                        BufferedReader timeBufferedReader = new BufferedReader(timeDataPath);


                        String readMusicTime = timeBufferedReader.readLine();
                        String [] musicTime = new String[4];
                        musicTime = readMusicTime.split("\\s|:");
                        musicStart = 1000 * ((( Integer.parseInt(musicTime[0]) * 60+Integer.parseInt(musicTime[1]) ) * 60) + Integer.parseInt(musicTime[2]))+Integer.parseInt(musicTime[3]);



                        String str = timeBufferedReader.readLine();
                        String tempStr;

                        while (str != null)                           //讀取檔案直到EOF
                        {
                            tempStr = str;
                            timelist.add(tempStr);                  //加入arraylist
                            str = timeBufferedReader.readLine();    //下一筆資料
                        }
                        count = timelist.size();                    //取得共有幾筆時間資料
                    }
                    catch (Exception e) {
                        textAnalysisOk.setText("read mydata failed");
                    }


                    String [] timeArray = new String[count];

                    String [] tempArray = new String[4];        //tempArray[]有4部分
                    int [][] timeData = new int[count][2];       //timeData[]有2部分

                    int timeShift = musicStart - inputStart;

                    for(int x = 0; x < count; x++) {
                        timeArray[x] = (String)timelist.get(x); //將arraylist中資料轉成String存進timeArray[]
                        tempArray = timeArray[x].split("\\s|:"); //將timeArray[]的字串遇以空白及冒號分割存進tempArray[]

                        int num=0;
                        int time=0;

                        num = Integer.parseInt(tempArray[0]);     //tempArray[0]為音樂編號
                        time = 1000 * ( Integer.parseInt(tempArray[1]) * 60+Integer.parseInt(tempArray[2]) ) + Integer.parseInt(tempArray[3]);  //tempArray[1]為分，tempArray[2]為秒，tempArray[3]為毫秒，算出時間

                        timeData[x][0] = num;                   //音樂編號存進timeData[][0]
                        timeData[x][1] = time + timeShift;                  //時間存進timeData[][1]
                    }



                    float [][] epoch = new float[count][151];                   //建立epoch
                    for(int x = 0; x < count; x++) {
                        int getTime = ( timeData[x][1] * 125 ) / 1000;

                        float preSum = 0;
                        for(int y = 1; y <= 25; y++){                           //前0.2秒
                            epoch[x][25 - y] = result[getTime - y];
                            preSum += result[getTime - y];
                        }

                        float preAvg = preSum / 25;                             //前0.2秒的平均
                        for(int y = 0; y < 126; y++){                           //後1秒
                            epoch[x][25 + y] = result[getTime + y] - preAvg;
                        }

                        for(int y = 1; y <= 25; y++){                           //前0.2秒
                            epoch[x][25 - y] = epoch[x][25 - y] - preAvg;
                        }
                    }


                    chordSum = new float[151];
                    intervalSum = new float[151];
                    float chordNum = 0;
                    float intervalNum = 0;

                    for(int x = 0; x < count; x++) {
                        if(timeData[x][0] >= 1 && timeData[x][0] <= 36){
                            for(int y = 0; y < 151; y++){
                                chordSum[y] += epoch[x][y];                    //chord加總

                            }
                            chordNum++;
                        }
                        if(timeData[x][0] >= 37 && timeData[x][0] <= 60){
                            for(int y = 0; y < 151; y++){
                                intervalSum[y] += epoch[x][y];                  //interval加總


                            }
                            intervalNum++;
                        }
                    }
                    for(int x = 0; x < 151; x++) {
                        chordSum[x] = chordSum[x] / chordNum;                   //chord平均
                    }
                    for(int x = 0; x < 151; x++) {
                        intervalSum[x] = intervalSum[x] / intervalNum;          //interval平均
                    }


                    File mSDFile = null;
                    mSDFile = Environment.getExternalStorageDirectory();
                    File mFile = new File(mSDFile.getParent() + "/" + mSDFile.getName() + "/data");
                    if(!mFile.exists())
                    {
                        mFile.mkdirs();
                    }

                    FileWriter cFileWriter = new FileWriter(mSDFile.getParent() + "/" + mSDFile.getName() + "/data/chord_output.txt");
                    BufferedWriter cBufferedWriter = new BufferedWriter(cFileWriter);

                    for(int i = 0; i < chordSum.length; i++){
                        cBufferedWriter.write(Float.toString(chordSum[i]) + "\n");
                    }
                    cBufferedWriter.close();

                    FileWriter iFileWriter = new FileWriter(mSDFile.getParent() + "/" + mSDFile.getName() + "/data/interval_output.txt");
                    BufferedWriter iBufferedWriter = new BufferedWriter(iFileWriter);

                    for(int i = 0; i < intervalSum.length; i++){
                        iBufferedWriter.write(Float.toString(intervalSum[i]) + "\n");
                    }
                    iBufferedWriter.close();


                    textAnalysisOk.setText("ok");
                }
                catch (Exception e) {
                    textAnalysisOk.setText("not ok");
                }

            }
        }); // end of buttonclick

        // TODO button draw result
        btnDrawResult = (Button) findViewById(R.id.showResult);
        btnDrawResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO set Result: titles, x, y; and build dataset
                result.titles = new String[]{"chord", "interval"}; // 定義折線的名稱
                //List<double[]> x = new ArrayList<double[]>(); // 點的x坐標
                //List<double[]> y = new ArrayList<double[]>(); // 點的y坐標
                //x.add(new double[] { 1, 3, 5, 7, 9, 11 });
                //x.add(new double[] { 0, 2, 4, 6, 8, 10 });
                //y.add(new double[] { 3, 14, 8, 22, 16, 18 });
                //y.add(new double[] { 20, 18, 15, 12, 10, 8 });
                result.addNewPoints(0, chordSum.length,chordSum);
                //result.addNewPoints(6,new float[] { 1, 3, 5, 7, 9, 11 });
                //result.addNewPoints(151);
                //result.addNewPoints(151);
                //result.addNewPoints(6,new float[] { 1, 3, 5, 7, 9, 11 });
                result.addNewPoints(1, intervalSum.length,intervalSum);
                XYMultipleSeriesDataset dataset = buildDatset(result.titles, result.x, result.y); // 儲存座標值

                // TODO chart display settings
                int[] colors = new int[]{Color.RED, Color.GREEN};// 折線的顏色
                PointStyle[] styles = new PointStyle[]{PointStyle.CIRCLE, PointStyle.DIAMOND}; // 折線點的形狀
                XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles, true);

                setChartSettings(renderer, "Result", "time", "", 0, chordSum.length, -5, 5, Color.BLACK);// 定義折線圖

                // TODO Display chart
                graphView = ChartFactory.getLineChartView(ResultActivity.this, dataset, renderer);
                //View chart = ChartFactory.getLineChartView(this, dataset, renderer);
                //setContentView(chart);
                LinearLayout layout = (LinearLayout) findViewById(R.id.displayResult);
                layout.addView(graphView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1000));

                // TODO set result.musician: are you a musician
                //result.musician = false;
                result.isMusician();
                TextView resultText = (TextView) findViewById(R.id.mResult);
                resultText.setText(//Float.toString(result.chPtest) + ":" +Float.toString(result.intPtest) + " , " +
                        // Float.toString(result.chNtest) + ":" +Float.toString(result.intNtest) + " / " +
                           "N1 dif: " + Float.toString(result.N1dif) + "," + "P2 dif: " + Float.toString(result.P2dif)
                           + " / " + "You are a musician!" );
                //if(  result.musician == true ){
                //    resultText.setText("You are a musician!");
                //}else{
                //    resultText.setText("You are not a musician!");
                //}

            }
        });

    }


    // 定義折線圖名稱
    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
                                    String yTitle, float xMin, float xMax, float yMin, float yMax, int axesColor) {
        //renderer.setChartTitle(title); // 折線圖名稱
        renderer.setBackgroundColor(Color.BLACK);
        renderer.setApplyBackgroundColor(true);
        //renderer.setChartTitleTextSize(30); // 折線圖名稱字形大小
        renderer.setLabelsTextSize(18); // Label Text Size
        renderer.setAxisTitleTextSize(30); // Axis Title Text Size
        renderer.setLegendTextSize(30);// 設定左下圖例文字大小 例如：第一條線、第二條線
        renderer.setPointSize(0);// 設定每個點的大小
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
        //renderer.setZoomButtonsVisible(true);
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
    private XYMultipleSeriesDataset buildDatset(String[] titles, List<float[]> xValues,
                                                List<float[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

        int length = titles.length; // 折線數量
        for (int i = 0; i < length; i++) {
            // XYseries對象,用於提供繪製的點集合的資料
            XYSeries series = new XYSeries(titles[i]); // 依據每條線的名稱新增
            float[] xV = xValues.get(i); // 獲取第i條線的資料
            float[] yV = yValues.get(i);
            int seriesLength = xV.length; // 有幾個點

            for (int k = 0; k < seriesLength; k++) // 每條線裡有幾個點
            {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
        return dataset;
    }

}

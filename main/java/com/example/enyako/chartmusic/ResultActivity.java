package com.example.enyako.chartmusic;

/**
 * Created by EnyaKo on 9/22/2017.
 */

import android.icu.text.UnicodeSetSpanner;
import android.support.v7.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;
import android.widget.LinearLayout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.GraphicalView;


public class ResultActivity extends AppCompatActivity {

    private Button mBtGoBack;
    private GraphicalView graphView;
    public Result result = new Result();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

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

        // TODO set Result: titles, x, y; and build dataset
        result.titles = new String[] { "chord", "interval" }; // 定義折線的名稱
        //List<double[]> x = new ArrayList<double[]>(); // 點的x坐標
        //List<double[]> y = new ArrayList<double[]>(); // 點的y坐標
        //x.add(new double[] { 1, 3, 5, 7, 9, 11 });
        //x.add(new double[] { 0, 2, 4, 6, 8, 10 });
        //y.add(new double[] { 3, 14, 8, 22, 16, 18 });
        //y.add(new double[] { 20, 18, 15, 12, 10, 8 });
        result.addNewPoints(new double[]{1,3,5,7,9,11},new double[] { 3, 14, 8, 22, 16, 18 });
        result.addNewPoints(new double[]{1,3,5,7,9,11},new double[] { 20, 18, 15, 12, 10, 8 });
        XYMultipleSeriesDataset dataset = buildDatset(result.titles, result.x, result.y); // 儲存座標值

        // TODO chart display settings
        int[] colors = new int[] { Color.RED, Color.GREEN };// 折線的顏色
        PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND }; // 折線點的形狀
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles, true);

        setChartSettings(renderer, "Result", "time", "value", 0, 12, 0, 25, Color.BLACK);// 定義折線圖

        // TODO Display chart
        graphView = ChartFactory.getLineChartView(this, dataset, renderer);
        //View chart = ChartFactory.getLineChartView(this, dataset, renderer);
        //setContentView(chart);
        LinearLayout layout = (LinearLayout) findViewById(R.id.displayResult);
        layout.addView(graphView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1000));

        // TODO set result.musician: are you a musician
        result.musician = false;
        TextView result = (TextView) findViewById(R.id.mResult);
        result.setText("You are not a musician!");
    }


    // 定義折線圖名稱
    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
                                    String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor) {
        //renderer.setChartTitle(title); // 折線圖名稱
        renderer.setBackgroundColor(Color.BLACK);
        renderer.setApplyBackgroundColor(true);
        //renderer.setChartTitleTextSize(30); // 折線圖名稱字形大小
        renderer.setLabelsTextSize(18); // Label Text Size
        renderer.setAxisTitleTextSize(30); // Axis Title Text Size
        renderer.setLegendTextSize(30);// 設定左下圖例文字大小 例如：第一條線、第二條線
        renderer.setPointSize(8f);// 設定每個點的大小
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
    }

}

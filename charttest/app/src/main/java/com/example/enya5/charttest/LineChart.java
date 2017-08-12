package com.example.enya5.charttest;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;


public class LineChart {

    protected TimeSeries data = new TimeSeries("Mz");
    protected XYMultipleSeriesDataset multiData = new XYMultipleSeriesDataset();
    protected XYSeriesRenderer renderer = new XYSeriesRenderer();
    protected XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
    protected GraphicalView view;

    public LineChart() {
        // add data
        multiData.addSeries(data);

        // customize
        renderer.setColor(Color.WHITE);
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setFillPoints(true);

        // Enable zoom
        multiRenderer.setChartTitle("BrainWave");
        multiRenderer.setChartTitleTextSize(50);
        multiRenderer.setZoomButtonsVisible(true);
        multiRenderer.setXTitle("Time");
        multiRenderer.setYTitle("Value");
        multiRenderer.setBackgroundColor(Color.BLACK);
        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setXLabelsColor(Color.WHITE);
        multiRenderer.setYLabelsColor(0, Color.WHITE);
        multiRenderer.setXLabelsColor(Color.RED);
        multiRenderer.setYLabelsColor(0, Color.RED);
        multiRenderer.setPanEnabled(true);
        //multiRenderer.setXAxisMax(xMax); // X軸顯示最大值
        multiRenderer.setLabelsTextSize(35);
        multiRenderer.setTextTypeface(null, Typeface.BOLD);
        multiRenderer.setAxisTitleTextSize(40);

        // add single renderer to multiple
        multiRenderer.addSeriesRenderer(renderer);
    }

    public GraphicalView getView(Context context) {
        view = ChartFactory.getLineChartView(context, multiData, multiRenderer);
        return view;
    }

    public void addNewPoints(Point p) {
        data.add(p.x, p.y);
        if(data.getItemCount() >= 30) {
            data.remove(data.getItemCount() - 30);
        }// end of if
    }

}
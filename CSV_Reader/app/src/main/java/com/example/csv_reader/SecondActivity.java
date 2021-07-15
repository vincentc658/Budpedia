package com.example.csv_reader;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.csv_reader.model.DataKPI;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SecondActivity extends AppCompatActivity {
    private LineChart chart;
    private TextView tvTime;
    ArrayList<DataKPI> usersData = new ArrayList();
    ArrayList<String> regions = new ArrayList();
    ArrayList<String> times = new ArrayList();
    ArrayList<Integer> colors = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colors.add(Color.BLACK);
        colors.add(Color.DKGRAY);
        colors.add(Color.GRAY);
        colors.add(R.color.purple_700);
        colors.add(R.color.teal_700);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        colors.add(Color.BLACK);
        colors.add(Color.DKGRAY);
        chart = findViewById(R.id.chart1);
        tvTime = findViewById(R.id.tvTime);

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        chart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);
        chart.setBackgroundColor(getResources().getColor(R.color.transparent));

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);
//        l.setYOffset(11f);
        chart.getExtraBottomOffset();
        chart.setNoDataText("Procesing data...");
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setTextColor(ColorTemplate.getHoloBlue());
        rightAxis.setDrawGridLines(true);
        rightAxis.setGranularityEnabled(true);


//        setChart(5, 4);
        readData();

    }

    public void readData() {
        new Thread() {
            @Override
            public void run() {

                InputStream inputStream = getResources().openRawResource(R.raw.data_kpi);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                try {
                    Long time = System.currentTimeMillis();
                    String csvLine;
                    BigDecimal temp = BigDecimal.ZERO;
                    boolean isFirsline = true;
                    while ((csvLine = reader.readLine()) != null) {
                        if (isFirsline) {
                            isFirsline = false;
                        } else {
                            String[] items = csvLine.split(",");
                            if (!regions.contains(items[2])) {
                                regions.add(items[2]);
                            }
                            if (!times.contains(items[0])) {
                                times.add(items[0]);
                            }
                            temp = temp.add(removeScientificNotation(items[4]));
                            DataKPI data = new DataKPI();
                            data.setDateId(items[0]);
                            data.setDeviceAlias(items[1]);
                            data.setRegionAlias(items[2]);
                            data.setService_availability(items[3]);
                            data.setBandwidth_utilization_max(removeScientificNotation(items[4]));
                            usersData.add(data);
                        }
                    }
                    runOnUiThread(() -> {
                        for (String s : regions) {
                            Log.d("region ", "" + s);
                        }
                        for (String s : times) {
                            Log.d("region times ", "" + s);
                        }
                    });
                    setData();


                } catch (IOException ex) {
                    throw new RuntimeException("Error in reading CSV file: " + ex);
                }
                super.run();
            }
        }.start();
    }

    private void setData() {
        HashMap<String, ArrayList<Float>> dataF = new HashMap<>();
        HashMap<String, ArrayList<Float>> dataG = new HashMap<>();
        Long timeSystem = System.currentTimeMillis();
        for (String region : regions) {
            for (String time : times) {
                BigDecimal totalData = BigDecimal.ZERO;
                BigDecimal totalSum = BigDecimal.ZERO;
                BigDecimal totalBandwith = BigDecimal.ZERO;
                for (DataKPI dataKPI : usersData) {
                    if (dataKPI.getDateId().equals(time) && dataKPI.getRegionAlias().equals(region)) {
                        totalData = totalData.add(new BigDecimal(1));
                        totalSum = totalSum.add(new BigDecimal(dataKPI.getService_availability()));
                        totalBandwith = totalBandwith.add(dataKPI.getBandwidth_utilization_max());
                    }
                }
                Float average = 0f;
                Float average2 = 0f;
                if (!totalData.equals(BigDecimal.ZERO)) {
                    BigDecimal averageBg = totalSum.divide(totalData, 3, RoundingMode.CEILING);
                    BigDecimal averageBf = totalBandwith.divide(totalData, 3, RoundingMode.CEILING);
                    average = averageBg.floatValue();
                    average2 = averageBf.floatValue();
                }


                ArrayList<Float> dataArr = new ArrayList<>();
                if (dataF.get(region) != null) {
                    dataArr = dataF.get(region);
                }
                dataArr.add(average);
                dataF.put(region, dataArr);
                Log.d("setData ->>>> ", "---" + time + "  " + region);

                //

                ArrayList<Float> dataArr2 = new ArrayList<>();
                if (dataG.get(region) != null) {
                    dataArr2 = dataG.get(region);
                }
                dataArr2.add(average2);
                dataG.put(region, dataArr2);
            }
        }


        // set data
        runOnUiThread(() -> {
            int position = 0;
            ArrayList<ILineDataSet> sets = new ArrayList<>();
            ArrayList<Entry> valuesChart = new ArrayList<>();
            for (Map.Entry<String, ArrayList<Float>> entry : dataG.entrySet()) {
                Log.d("set data testing ", "->" + entry.getKey() + "->" + entry.getValue().size());
                valuesChart = new ArrayList<>();
                for (int i = 0; i < entry.getValue().size(); i++) {
                    Log.d("set data testing ", "----------------> entry2 " + entry.getValue().get(i));
                    valuesChart.add(new Entry(i, entry.getValue().get(i)));
                }

                LineDataSet set = new LineDataSet(valuesChart, entry.getKey());
                set.setAxisDependency(YAxis.AxisDependency.LEFT);
                set.setColor(colors.get(position));
                set.setCircleColor(Color.WHITE);
                set.setLineWidth(2f);
                set.setCircleRadius(3f);
                set.setFillAlpha(65);
                set.setFillColor(ColorTemplate.getHoloBlue());
                set.setHighLightColor(Color.rgb(244, 117, 117));
                set.setDrawCircleHole(false);
                sets.add(set);
                position++;

            }
            LineData data = new LineData(sets);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);
            chart.setData(data);
            chart.invalidate();
            chart.animateX(200);
            tvTime.setText("" + timeSystem + " -" + System.currentTimeMillis());
        });
    }

    public static BigDecimal removeScientificNotation(String value) {
        return new BigDecimal(value);
    }

    public void setChart(int count, int range) {
        new Thread() {
            @Override
            public void run() {


                ArrayList<Entry> values1 = new ArrayList<>();
                values1.add(new Entry(0, 0.0f));
                values1.add(new Entry(1, 1.0f));
                values1.add(new Entry(2, 0.0f));


                ArrayList<Entry> values2 = new ArrayList<>();
                values2.add(new Entry(0, 1.0f));
                values2.add(new Entry(1, 0.0f));
                values2.add(new Entry(2, 1.0f));

                ArrayList<Entry> values3 = new ArrayList<>();

                values3.add(new Entry(0, 1.2f));
                values3.add(new Entry(1, 1.7f));
                values3.add(new Entry(2, 0.8f));

                ArrayList<ILineDataSet> sets = new ArrayList<>();

                LineDataSet set;
                for (int i = 0; i < 2; i++) {
                    if (i % 2 == 0) {
                        set = new LineDataSet(values1, "DataSet 1");
                    } else {
                        set = new LineDataSet(values3, "DataSet 1" + i);
                    }
                    set.setAxisDependency(YAxis.AxisDependency.LEFT);
                    set.setColor(ColorTemplate.getHoloBlue());
                    set.setCircleColor(Color.WHITE);
                    set.setLineWidth(2f);
                    set.setCircleRadius(3f);
                    set.setFillAlpha(65);
                    set.setFillColor(ColorTemplate.getHoloBlue());
                    set.setHighLightColor(Color.rgb(244, 117, 117));
                    set.setDrawCircleHole(false);
                    sets.add(set);

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        LineData data = new LineData(sets);
                        data.setValueTextColor(Color.WHITE);
                        data.setValueTextSize(9f);

                        // set data
                        chart.setData(data);
                        chart.notify();
                    }
                });
            }
        }.start();

    }
//    }
}

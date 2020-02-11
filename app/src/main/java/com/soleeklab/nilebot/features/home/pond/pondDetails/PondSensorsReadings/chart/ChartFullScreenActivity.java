package com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.soleeklab.nilebot.BaseActivity;
import com.soleeklab.nilebot.GetSensorsReadingByPondIDQuery;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.models.MyCache;
import com.soleeklab.nilebot.data.repo.LocalRepo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartFullScreenActivity extends BaseActivity {

    @BindView(R.id.chart1)
    LineChart chart;
    @BindView(R.id.btn_land_scape)
    ImageView btnLandScape;

    @Inject
    LocalRepo localRepo;

    //  int[] colors = {Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED, Color.CYAN,Color.MAGENTA,Color.LTGRAY};
    int[] colors = {Color.rgb(0, 122, 255),
            Color.rgb(255, 204, 0),

            Color.rgb(255, 59, 48),

            Color.rgb(52, 199, 89),
            Color.rgb(255, 149, 0),
            Color.rgb(175, 82, 222),
            Color.rgb(142, 142, 147),
            Color.rgb(88, 86, 214),

            Color.rgb(90, 200, 250),

            Color.rgb(121, 85, 72),
            Color.rgb(0, 150, 136),
            Color.rgb(205, 220, 57)
    };
    int i = 0;
    int Xcount = 0;
    Map<String, List<Entry>> sensorListMap = new TreeMap<>();

    Map<String, List<GetSensorsReadingByPondIDQuery.Parameter>> map;


    @Inject
    MyCache myCache;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_full_screen);
        ButterKnife.bind(this);
        map = myCache.getMap();
        btnLandScape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                drawChart();
            }
        });
    }

    @Override
    protected View getView() {
        return null;
    }


    private void drawChart() {
        ArrayList<String> xAxesValues = new ArrayList<>();
        LineData lineData = new LineData();
        Xcount = 0;
        for (String time : map.keySet()) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
                String key_date = new SimpleDateFormat("yyyy-MMM-dd, h:mm a").format(date);
                xAxesValues.add(Xcount, key_date);

            } catch (ParseException e) {
                e.printStackTrace();
            }


            for (GetSensorsReadingByPondIDQuery.Parameter sensor : map.get(time)) {
                for (GetSensorsReadingByPondIDQuery.Reading reading : sensor.readings()) {
                    if (sensorListMap.containsKey(sensor.name())) {
                        List<Entry> list = sensorListMap.get(sensor.name());
                        list.add(new Entry(Xcount, (float) reading.value()));
                        sensorListMap.put(sensor.name(), list);
                    } else {
                        List<Entry> list = new ArrayList<>();
                        list.add(new Entry(Xcount, (float) reading.value()));
                        sensorListMap.put(sensor.name(), list);
                    }
                }
            }
            Xcount++;
        }
        for (String sensor : sensorListMap.keySet()) {
            addSensorData(lineData, sensorListMap.get(sensor), localRepo.getTranslation(sensor));
        }

        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setNoDataText("No chart data available.");

        chart.getXAxis().setDrawLabels(true);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setAxisLineColor(Color.BLACK);
        chart.getXAxis().setAxisLineWidth(1f);

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setAxisLineColor(Color.BLACK);
        chart.getAxisLeft().setAxisLineWidth(1f);
        chart.getAxisLeft().setAxisMinimum(0f);

        chart.setExtraOffsets(0, 0, 0, 15);
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.CIRCLE);
        // l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setWordWrapEnabled(true);
        l.setXEntrySpace(20f);

//         System.out.println(xAxesValues.toString());

        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxesValues));
        chart.getXAxis().setLabelRotationAngle(30f);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setCenterAxisLabels(true);

        chart.getXAxis().setAxisMinimum(lineData.getXMin() - 1f);
        chart.getXAxis().setAxisMaximum(lineData.getXMax() + 1f);
        chart.setData(lineData);

//        chart.setVisibleXRangeMaximum(1000);

        chart.invalidate();
    }


    private void addSensorData(LineData data, List<Entry> yAxesvalues, String name) {


        LineDataSet set = new LineDataSet(yAxesvalues, name);
        set.setLineWidth(2.5f);
        set.setCircleRadius(4.5f);
        int color = colors[(i++ % colors.length)];

        set.setColor(color);
        set.setCircleColor(color);
        set.setDrawCircleHole(false);
        // set.setDrawValues(false);

        data.addDataSet(set);

    }

}

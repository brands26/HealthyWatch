package com.healthywatch.brands.healthywatch.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.healthywatch.brands.healthywatch.R;
import com.healthywatch.brands.healthywatch.app.HWConfig;
import com.healthywatch.brands.healthywatch.helper.SessionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by brandon on 07/05/16.
 */
public class UserDetakJantungMainFragment extends Fragment {
    private static final long DRAWING_GRAPH_INTERVAL = 5*1000;
    private static final long DRAWING_GRAPH_DELAY = 3*1000;

    private Firebase mref;
    private SessionHelper session;
    private LineChart chart;
    private LineData data;
    private TextView txtBPM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.user_detak_jantung_main_fragment, container, false);
        mref = new Firebase(HWConfig.FIREBASE_URL);
        session = new SessionHelper(getActivity());
        chart = (LineChart) v.findViewById(R.id.chart);
        txtBPM = (TextView) v.findViewById(R.id.txtBPM);
        data = new LineData();
        chart.setData(data);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        Legend l = chart.getLegend();
        XAxis xl = chart.getXAxis();
        xl.setEnabled(false);
        YAxis lx = chart.getAxisLeft();
        lx.setEnabled(false);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        chart.invalidate();
        return v;
    }
    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "DataSet 1");
        set.setLineWidth(2.5f);
        set.setCircleRadius(4.5f);
        set.setColor(Color.rgb(240, 99, 99));
        set.setCircleColor(Color.rgb(240, 99, 99));
        set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueTextSize(10f);

        return set;
    }
    public void addEntry(int detak) {
        LineData data = chart.getData();

        if(data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            // add a new x-value first
            data.addXValue(set.getEntryCount() + "");

            // choose a random dataSet
            int randomDataSetIndex = (int) (Math.random() * data.getDataSetCount());

            data.addEntry(new Entry(detak, set.getEntryCount()), randomDataSetIndex);

            // let the chart know it's data has changed
            chart.notifyDataSetChanged();


            chart.setVisibleXRangeMaximum(10);
            chart.moveViewTo(data.getXValCount()-7, 55f, YAxis.AxisDependency.LEFT);
        }
    }

    public void setBPM(String BPM){
        txtBPM.setText(BPM+" BPM");
    }
    @Override
    public void onPause(){
        super.onPause();
    }
    @Override
    public void onResume(){
        super.onResume();
    }


}



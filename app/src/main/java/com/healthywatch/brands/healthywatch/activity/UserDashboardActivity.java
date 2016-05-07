package com.healthywatch.brands.healthywatch.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.firebase.client.Firebase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.healthywatch.brands.healthywatch.R;
import com.healthywatch.brands.healthywatch.app.HWConfig;
import com.healthywatch.brands.healthywatch.fragment.FragmentDrawer;
import com.healthywatch.brands.healthywatch.fragment.UserDetakJantungMainFragment;
import com.healthywatch.brands.healthywatch.fragment.UserFitnessMainFragment;
import com.healthywatch.brands.healthywatch.fragment.UserSuhuMainFragment;
import com.healthywatch.brands.healthywatch.helper.SessionHelper;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class UserDashboardActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{

    private FragmentDrawer drawerFragment;
    private Toolbar toolbar;
    private Firebase mref;
    private ViewPager viewPager;
    private Fragment mainFitness,mainSuhu;
    private UserDetakJantungMainFragment mainDetakJantung;
    private LineChart chart;
    private List detak = new ArrayList();
    private SessionHelper session;
    private Firebase detakref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        mref = new Firebase(HWConfig.FIREBASE_URL);
        session = new SessionHelper(this);
        detakref = mref.child("jantung").child(session.getUserId());
        setContentView(R.layout.activity_user_dashboard);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);

        final AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.jantung, R.drawable.heart_48, R.color.colorAccent);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.fitness, R.drawable.fitness_48, R.color.colorAccent);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.suhu, R.drawable.temperature_48, R.color.colorAccent);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                viewPager.setCurrentItem(position,true);
            }
        });

        viewPager = (ViewPager) findViewById(R.id.frameDashboard);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigation.setCurrentItem(position);
                switch (position){
                    case 0:
                        toolbar.setTitle("Detak Jantung");
                        break;
                    case 1:
                        toolbar.setTitle("Fitness");
                        break;
                    case 2:
                        toolbar.setTitle("Suhu");
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        feedMultiple();
    }

    private void setupViewPager(ViewPager viewPager) {

        mainDetakJantung = new UserDetakJantungMainFragment();
        mainFitness = new UserFitnessMainFragment();
        mainSuhu = new UserSuhuMainFragment();
        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return mainDetakJantung;
                    case 1:
                        return mainFitness;
                    case 2:
                        return mainSuhu;
                    default:
                        return null;
                }

            }
        };
        viewPager.setAdapter(adapter);
    }
    @Override
    public void onDrawerItemSelected(View view, int position) {
        Log.d("position:",""+position);
        switch (position){
            case 5:
                mref.unauth();
                Intent i = new Intent(this,AuthenticationActivity.class);
                startActivity(i);
                finish();
                break;

        }
    }
    private void feedMultiple() {

        new Thread(new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                while (!Thread.interrupted())
                    try
                    {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {

                            @Override
                            public void run()
                            {
                                int jumlahDetak = detak.size();
                                if(jumlahDetak==60){
                                    int dpm =sum(detak);
                                    detak.clear();
                                    Long tsLong = System.currentTimeMillis()/1000;
                                    String ts = tsLong.toString();
                                    Map<String, String> detakMap = new HashMap<String, String>();
                                    detakMap.put("detak",""+dpm);
                                    detakMap.put("time",""+ts);
                                    detakref.push().setValue(detakMap);
                                    mainDetakJantung.setBPM(dpm);
                                }
                                Random rand = new Random();
                                int n = rand.nextInt(2);
                                Log.d("detak  ",""+n);
                                detak.add(n);
                                mainDetakJantung.addEntry(n);
                            }
                        });
                    }
                    catch (InterruptedException e)
                    {
                        // ooops
                    }
            }
        })).start();
    }
    public static int sum (List<Integer> list) {
        int sum = 0;
        for (int i: list) {
            sum += i;
        }
        return sum;
    }






}

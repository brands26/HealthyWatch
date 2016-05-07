package com.healthywatch.brands.healthywatch.activity;

import android.animation.ArgbEvaluator;
import android.animation.IntArrayEvaluator;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.data.Session;
import com.healthywatch.brands.healthywatch.R;
import com.healthywatch.brands.healthywatch.app.HWConfig;
import com.healthywatch.brands.healthywatch.fragment.OnBoardFragmentOne;
import com.healthywatch.brands.healthywatch.fragment.OnBoardFragmentThree;
import com.healthywatch.brands.healthywatch.fragment.OnBoardFragmentTwo;
import com.healthywatch.brands.healthywatch.helper.SessionHelper;
import com.healthywatch.brands.healthywatch.helper.SettingHelper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFunction;

public class OnboardActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Button mNextBtn,mFinishBtn,mBackBtn;
    private View[] indicators;
    private int page;
    private ImageView zero,one,two;
    private SettingHelper settingHelper;
    private Firebase mRef;
    private SessionHelper session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        settingHelper = new SettingHelper(this);
        mRef = new Firebase(HWConfig.FIREBASE_URL);
        session = new SessionHelper(this);
        if(!settingHelper.getOnboard()){

            mRef.addAuthStateListener(new Firebase.AuthStateListener() {
                @Override
                public void onAuthStateChanged(AuthData authData) {
                    if (authData != null) {
                        Intent i = new Intent(getApplicationContext(),UserDashboardActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(getApplicationContext(),AuthenticationActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }
        setContentView(R.layout.activity_onboard);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        mNextBtn = (Button) findViewById(R.id.intro_btn_next);
        mFinishBtn = (Button) findViewById(R.id.intro_btn_finish);
        mBackBtn = (Button) findViewById(R.id.intro_btn_back);
        zero = (ImageView) findViewById(R.id.intro_indicator_0);
        one = (ImageView) findViewById(R.id.intro_indicator_1);
        two = (ImageView) findViewById(R.id.intro_indicator_2);
        indicators = new ImageView[]{zero, one, two};
        updateIndicators(page);
        final int color1 = ContextCompat.getColor(this, R.color.white);
        final int color2 = ContextCompat.getColor(this, R.color.white);
        final int color3 = ContextCompat.getColor(this, R.color.white);
        final int[] colorList = new int[]{color1, color2, color3};
        final ArgbEvaluator evaluator = new ArgbEvaluator();;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int colorUpdate = (Integer) evaluator.evaluate(positionOffset, colorList[position], colorList[position == 2 ? position : position + 1]);
                viewPager.setBackgroundColor(colorUpdate);
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                updateIndicators(page);
                switch (position) {
                    case 0:
                        viewPager.setBackgroundColor(color1);
                        break;
                    case 1:
                        viewPager.setBackgroundColor(color2);
                        break;
                    case 2:
                        viewPager.setBackgroundColor(color3);
                        break;
                }
                mNextBtn.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                mBackBtn.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                mFinishBtn.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    public void finishOnClick(View v){
        settingHelper.setOnboard(false);
        finish();
        Intent i = new Intent(v.getContext(),AuthenticationActivity.class);
        startActivity(i);
    }
    public void nextPage(View view){
        viewPager.setCurrentItem(page+1,true);
    }
    public void backPage(View view){
        viewPager.setCurrentItem(page-1,true);
    }

    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OnBoardFragmentOne(), "ONE");
        adapter.addFragment(new OnBoardFragmentTwo(), "TWO");
        adapter.addFragment(new OnBoardFragmentThree(), "THREE");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

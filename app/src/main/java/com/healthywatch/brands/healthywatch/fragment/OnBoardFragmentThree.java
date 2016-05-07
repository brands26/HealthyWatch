package com.healthywatch.brands.healthywatch.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthywatch.brands.healthywatch.R;

/**
 * Created by brandon on 28/04/16.
 */
public class OnBoardFragmentThree extends Fragment {
    private Context context;

    public OnBoardFragmentThree() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboard_three, container, false);
    }
}
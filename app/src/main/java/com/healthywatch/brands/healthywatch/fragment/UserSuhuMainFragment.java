package com.healthywatch.brands.healthywatch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;
import com.healthywatch.brands.healthywatch.R;
import com.healthywatch.brands.healthywatch.app.HWConfig;
import com.healthywatch.brands.healthywatch.helper.SessionHelper;

/**
 * Created by brandon on 07/05/16.
 */
public class UserSuhuMainFragment extends Fragment {
    private Firebase mref;
    private SessionHelper session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.user_detak_suhu_main_fragment, container, false);
        mref = new Firebase(HWConfig.FIREBASE_URL);
        session = new SessionHelper(getActivity());
        Fragment measure = new UserDetakJantungMeasureFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.mainFrameDetakJantung,measure,"Detak Jantung");
        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        transaction.commit();
        return v;
    }
}

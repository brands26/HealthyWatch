package com.healthywatch.brands.healthywatch.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.firebase.client.Firebase;
import com.healthywatch.brands.healthywatch.R;
import com.healthywatch.brands.healthywatch.fragment.AuthenticationForgotFragment;
import com.healthywatch.brands.healthywatch.fragment.AuthenticationLoginFragment;
import com.healthywatch.brands.healthywatch.fragment.AuthenticationRegisterFragment;

public class AuthenticationActivity extends AppCompatActivity {
    private Firebase mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_authentication);
        FrameLayout authFrame = (FrameLayout) findViewById(R.id.FrameAuthentication);
        AuthenticationLoginFragment loginFragment = new AuthenticationLoginFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.FrameAuthentication,loginFragment,"Login");
        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        transaction.commit();
    }

    public void onRegisterClick(View v){
        AuthenticationRegisterFragment registerFragment = new AuthenticationRegisterFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.FrameAuthentication,registerFragment,"register");
        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void onForgotClick(View v){
        AuthenticationForgotFragment forgotfragment = new AuthenticationForgotFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.FrameAuthentication,forgotfragment,"register");
        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}

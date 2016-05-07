package com.healthywatch.brands.healthywatch.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.healthywatch.brands.healthywatch.R;
import com.healthywatch.brands.healthywatch.activity.UserDashboardActivity;
import com.healthywatch.brands.healthywatch.app.HWConfig;
import com.healthywatch.brands.healthywatch.helper.SessionHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brandon on 05/05/16.
 */
public class AuthenticationLoginFragment extends Fragment {
    private EditText inputNama,inputTanggal,inputEmail,inputUsername,inputPassword;
    private Button btnLogin;
    private Firebase mref;
    private SessionHelper session;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mref = new Firebase(HWConfig.FIREBASE_URL);
        session = new SessionHelper(getActivity());
        View v = inflater.inflate(R.layout.athentication_login_fragment, container, false);
        inputEmail = (EditText) v.findViewById(R.id.inputEmail);
        inputPassword = (EditText) v.findViewById(R.id.inputPassword);
        btnLogin =(Button) v.findViewById(R.id.btnLogin);
        mref = new Firebase(HWConfig.FIREBASE_URL);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogin();
            }
        });

        return v;
    }

    public void onLogin(){
        final String email = inputEmail.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();
        mref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                // Authenticated successfully with payload authData
                session.setUserId(authData.getUid());
                session.setLogedIn(true);
                Intent intent = new Intent(getActivity(), UserDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Toast.makeText(getActivity(),firebaseError.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }


}

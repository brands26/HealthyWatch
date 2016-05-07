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
public class AuthenticationRegisterFragment extends Fragment {
    private EditText inputNama,inputTanggal,inputEmail,inputUsername,inputPassword;
    private Firebase mref;
    private SessionHelper session;
    private Button register;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.athentication_register_fragment, container, false);
        mref = new Firebase(HWConfig.FIREBASE_URL);
        session = new SessionHelper(getActivity());
        inputNama = (EditText) v.findViewById(R.id.inputNama);
        inputTanggal = (EditText) v.findViewById(R.id.inputTanggal);
        inputEmail = (EditText) v.findViewById(R.id.inputEmail);
        inputUsername = (EditText) v.findViewById(R.id.inputUsername);
        inputPassword = (EditText) v.findViewById(R.id.inputPassword);
        register = (Button) v.findViewById(R.id.btnregister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegister();
            }
        });
        return v;
    }

    public void onRegister(){
        final String nama = inputNama.getText().toString().trim();
        final String tanggal = inputTanggal.getText().toString().trim();
        final String email = inputEmail.getText().toString().trim();
        final String username = inputUsername.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();

        mref.createUser(email,password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                mref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        // Authenticated successfully with payload authData
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("email", email);
                        map.put("username", username);
                        map.put("nama", nama);
                        map.put("tanggal", tanggal);
                        mref.child("users").child(authData.getUid()).setValue(map);
                        session.setUserId(authData.getUid());
                        session.setLogedIn(true);
                        Toast.makeText(getActivity(),"Pendaftaran Berhasil",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getActivity(), UserDashboardActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getActivity().startActivity(i);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast.makeText(getActivity(),firebaseError.toString(),Toast.LENGTH_LONG).show();
                    }

                });
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Toast.makeText(getActivity(),firebaseError.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}

package com.healthywatch.brands.healthywatch.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.healthywatch.brands.healthywatch.R;
import com.healthywatch.brands.healthywatch.app.HWConfig;
import com.healthywatch.brands.healthywatch.helper.SessionHelper;
import com.healthywatch.brands.healthywatch.model.Profil;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private Firebase mRef;
    private SessionHelper session;
    private EditText inputNama, inputTanggal,inputAlamat, inputAlergi, inputPenyakit, inputDarah, inputBerat, inputTinggi;
    private Firebase profilRef;
    private Firebase readProfilRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        session = new SessionHelper(this);
        mRef = new Firebase(HWConfig.FIREBASE_URL);
        profilRef = mRef.child("profil").child(session.getUserId());
        profilRef.keepSynced(true);
        readProfilRef = new Firebase(HWConfig.FIREBASE_URL+"/profil/"+session.getUserId());
        inputNama = (EditText) findViewById(R.id.inputNama);
        inputTanggal = (EditText) findViewById(R.id.inputTanggalLahir);
        inputAlamat = (EditText) findViewById(R.id.inputAlamat);
        inputAlergi = (EditText) findViewById(R.id.inputAlergi);
        inputPenyakit = (EditText) findViewById(R.id.inputPenyakit);
        inputDarah = (EditText) findViewById(R.id.inputGolonganDarah);
        inputBerat = (EditText) findViewById(R.id.inputBeratBadan);
        inputTinggi = (EditText) findViewById(R.id.inputTinggiBadan);
        getDataProfil();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveProfil();
            }
        });
    }

    private void onSaveProfil(){
        Map<String, Object> nickname = new HashMap<String, Object>();
        nickname.put("nama", inputNama.getText().toString().trim());
        nickname.put("tanggalLahir", inputTanggal.getText().toString().trim());
        nickname.put("alamat", inputAlamat.getText().toString().trim());
        nickname.put("alergi", inputAlergi.getText().toString().trim());
        nickname.put("penyakit", inputPenyakit.getText().toString().trim());
        nickname.put("golDarah", inputDarah.getText().toString().trim());
        nickname.put("beratBadan", inputBerat.getText().toString().trim());
        nickname.put("tinggiBadan", inputTinggi.getText().toString().trim());
        profilRef.updateChildren(nickname);
    }
    private void getDataProfil(){
        readProfilRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Profil profil = snapshot.getValue(Profil.class);
                inputNama.setText(profil.getNama());
                inputTanggal.setText(profil.getTanggalLahir());
                inputAlamat.setText(profil.getAlamat());
                inputAlergi.setText(profil.getAlergi());
                inputPenyakit.setText(profil.getPenyakit());
                inputDarah.setText(profil.getGolDarah());
                inputBerat.setText(profil.getBeratBadan());
                inputTinggi.setText(profil.getTinggiBadan());
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(),firebaseError.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        getDataProfil();
    }
}

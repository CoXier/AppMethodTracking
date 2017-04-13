package com.hackerli.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_wear_cloth).setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else{
            Debug.startMethodTracing(getPackageName());
            wearCloth();
            Debug.stopMethodTracing();
        }
    }


    public void wearCloth() {
        putOnCoat();
        putOnPants();
        putOnShoes();
        putOnHat();
    }

    private void putOnPants() {

    }

    private void putOnCoat() {

    }

    private void putOnHat() {
        // Put on hat
    }


    public void putOnShoes() {
        putOnSocks();
        // Put on shoes
    }

    public void putOnSocks() {
        // Put on socks
    }

}

package easypromo.com.easypromo.activity;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import easypromo.com.easypromo.R;
import easypromo.com.easypromo.adapter.TabAdapter;
import easypromo.com.easypromo.config.AcessoDatabase;
import easypromo.com.easypromo.helper.Base64Custom;
import easypromo.com.easypromo.helper.SlidingTabLayout;

public class AdminActivity extends AppCompatActivity {

    // region Variáveis
    private android.support.v7.widget.Toolbar toolbarAdmin;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        configurarToolbar();
        configurarSlidingPage();
    }

    private void configurarToolbar(){
        toolbarAdmin = findViewById(R.id.toolbarAdmin);
        toolbarAdmin.setTitle(R.string.texto_toolbar_admin);
        setSupportActionBar(toolbarAdmin);
    }

    private void configurarSlidingPage(){
        slidingTabLayout = findViewById(R.id.stl_tabs);
        viewPager = findViewById(R.id.vp_pagina);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.primaryLightColor));

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        slidingTabLayout.setViewPager(viewPager);
    }
}
package easypromo.com.easypromo.activity;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import easypromo.com.easypromo.R;
import easypromo.com.easypromo.adapter.TabAdapter;
import easypromo.com.easypromo.helper.SlidingTabLayout;

public class AdminActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbarAdmin;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toolbarAdmin = findViewById(R.id.toolbarAdmin);
        toolbarAdmin.setTitle(R.string.texto_toolbar_admin);
        setSupportActionBar(toolbarAdmin);

        slidingTabLayout = findViewById(R.id.stl_tabs);
        viewPager = findViewById(R.id.vp_pagina);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.primaryLightColor));

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        slidingTabLayout.setViewPager(viewPager);
    }
}
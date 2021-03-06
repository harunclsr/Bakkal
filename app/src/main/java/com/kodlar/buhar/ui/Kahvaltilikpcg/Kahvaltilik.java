package com.kodlar.buhar.ui.Kahvaltilikpcg;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.kodlar.buhar.AnaEkran;
import com.kodlar.buhar.AramaEkrani;
import com.kodlar.buhar.R;
import com.kodlar.buhar.ui.Sepetimpcg.Sepetim;

import android.view.View;
import android.widget.ImageButton;

public class Kahvaltilik extends AppCompatActivity {
    private ImageButton Anasayfadonus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kahvaltilik);
        Anasayfadonus = (ImageButton) findViewById(R.id.Anasayfadonuskahvaltilik);
        KahvaltilikSectionsPagerAdapter kahvaltilikSectionsPagerAdapter = new KahvaltilikSectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(kahvaltilikSectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.SepetButonu);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Anasayfadonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent( Kahvaltilik.this, Sepetim.class);

                startActivity(intent);

            }
        });
    }
}
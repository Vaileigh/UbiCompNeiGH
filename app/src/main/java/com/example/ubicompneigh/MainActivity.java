package com.example.ubicompneigh;

import android.os.Bundle;

import com.example.ubicompneigh.ui.main.Lists;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ubicompneigh.ui.main.SectionsPagerAdapter;
import com.example.ubicompneigh.databinding.ActivityMainBinding;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Lists> arrayList= new ArrayList<>();
    private ArrayList<Lists> notificationList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        swipeRefreshLayout = (SwipeRefreshLayout) binding.simpleSwipeRefreshLayout;
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = getData();
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);


        if(NetworkUtils.isNetworkConnected(this)){
            Toast.makeText(this, "Network Detected", Toast.LENGTH_SHORT).show();
        }
        /*InputStreamReader is = null;
        try {
            is = new InputStreamReader(getAssets().open("bucket_details.csv"));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                Log.d("MyApp",line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();
                sectionsPagerAdapter.clear();
                SectionsPagerAdapter sectionsPagerAdapter = getData();
                ViewPager viewPager = binding.viewPager;

                viewPager.setAdapter(sectionsPagerAdapter);
            }
        });
    }

    private SectionsPagerAdapter getData() {
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(getAssets().open("example_entry_sheet1.csv"));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            String bucketID;
            int waterLvl, waterLoss, waterRequired, waterDrunk;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split( "," );
                bucketID = row[0];
                waterLvl = Integer.parseInt(row[1]);
                waterRequired = Integer.parseInt(row[2]);
                waterDrunk = Integer.parseInt(row[3]);
                waterLoss = Integer.parseInt(row[4]);
                arrayList.add(new Lists(bucketID,waterLvl,waterRequired,waterDrunk, waterLoss));

                if (waterLvl <= 300){
                    notificationList.add(new Lists(bucketID,waterLvl,waterRequired,waterDrunk, waterLoss));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), arrayList, notificationList);
        return sectionsPagerAdapter;
    }
}
package com.example.ubicompneigh;

import android.os.Bundle;

import com.example.ubicompneigh.ui.main.Lists;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    String TAG = "MyApp";
    private ActivityMainBinding binding;
    public ArrayList<Lists> arrayList= new ArrayList<>();
    public ArrayList<Lists> notificationList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    SwipeRefreshLayout swipeRefreshLayout;
    SectionsPagerAdapter sectionsPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        swipeRefreshLayout = (SwipeRefreshLayout) binding.simpleSwipeRefreshLayout;
        setContentView(binding.getRoot());
        addFirebaseDb();
        //sectionsPagerAdapter = getData();
        /*sectionsPagerAdapter = new SectionsPagerAdapter(this,
                getSupportFragmentManager(), arrayList, notificationList);*/
        ViewPager viewPager = binding.viewPager;
        Log.d("MyApp", "onCreate69: "+notificationList.size());
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
                arrayList.clear();
                notificationList.clear();
                //SectionsPagerAdapter sectionsPagerAdapter = getData();
                addFirebaseDb();
                ViewPager viewPager = binding.viewPager;
                viewPager.setAdapter(sectionsPagerAdapter);
            }
        });
    }


    private void addFirebaseDb(){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        int totalBuckets = 2;


        for (int i = 0; i < totalBuckets; i++){
            DatabaseReference myRef = database.getReference(String.valueOf(i));
            //myRef.setValue("Hello, World!");
            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String bucketID = dataSnapshot.child("bucketID").getValue(String.class);
                    Log.d("MyApp", "onDataChange: " + bucketID);
                    dataSnap(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("MyApp", "Failed to read value.", error.toException());
                }
            });
        }
        //sectionsPagerAdapter.clear();
        //SectionsPagerAdapter sectionsPagerAdapter = getData();
        Log.d("MyApp", "addFirebaseDb: " + arrayList.size());
        Log.d("MyApp", "addFirebaseDb: " + notificationList.size());
        if (sectionsPagerAdapter != null){
            sectionsPagerAdapter.clear();
        }
        Log.d("MyApp", "addFirebaseDb: "+ notificationList.size());
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), arrayList, notificationList);
        //ViewPager viewPager = binding.viewPager;
        //viewPager.setAdapter(sectionsPagerAdapter);

    }

    private void dataSnap(DataSnapshot dataSnapshot){
        String bucketID = dataSnapshot.child("bucketID").getValue(String.class);
        if(arrayList.size()==0 ){
            int waterLvl = dataSnapshot.child("waterLvl").getValue(Integer.class);
            int waterLoss = dataSnapshot.child("waterLoss").getValue(Integer.class);
            int waterRequired = dataSnapshot.child("waterRequired").getValue(Integer.class);
            int waterDrunk = dataSnapshot.child("waterDrunk").getValue(Integer.class);
            Log.d("MyApp", "dataSnap: " + bucketID);
            arrayList.add(new Lists(bucketID,waterLvl,waterRequired,waterDrunk, waterLoss));
            if (waterLvl <= 300){
                notificationList.add(new Lists(bucketID,waterLvl,waterRequired,waterDrunk, waterLoss));
            }
        }
        else{
            boolean found = false;
            for (int i = 0; i< arrayList.size(); i++){
                if (arrayList.get(i).getBucketID() == bucketID){
                    found = true;
                }
            }
            if (found){
                for(int j = 0; j < arrayList.size(); j++){
                    if (arrayList.get(j).getBucketID() == bucketID){
                        int waterLvl = dataSnapshot.child("waterLvl").getValue(Integer.class);
                        int waterLoss = dataSnapshot.child("waterLoss").getValue(Integer.class);
                        int waterRequired = dataSnapshot.child("waterRequired").getValue(Integer.class);
                        int waterDrunk = dataSnapshot.child("waterDrunk").getValue(Integer.class);
                        Log.d("MyApp", "onDataChange: " +bucketID);
                        arrayList.get(j).setWaterLoss(waterLoss);
                        arrayList.get(j).setWaterDrunk(waterDrunk);
                        arrayList.get(j).setWaterLvl(waterLvl);
                        arrayList.get(j).setWaterRequired(waterRequired);
                        boolean notificationFound = false;
                        for (int i = 0; i < notificationList.size(); i++ ){
                            if (notificationList.get(i).getBucketID() == bucketID){
                                notificationFound = true;
                            }
                        }
                            if (notificationFound){
                                Log.d(TAG, "dataSnap: 192" + notificationList.size());
                                for (int i = 0; i < notificationList.size(); i++) {
                                    if (waterLvl <= 300){
                                        if (notificationList.get(i).getBucketID() == bucketID) {
                                                notificationList.get(i).setWaterLoss(dataSnapshot.child("waterLoss").getValue(Integer.class));
                                                notificationList.get(i).setWaterDrunk(dataSnapshot.child("waterDrunk").getValue(Integer.class));
                                                notificationList.get(i).setWaterLvl(dataSnapshot.child("waterLvl").getValue(Integer.class));
                                                notificationList.get(i).setWaterRequired(dataSnapshot.child("waterRequired").getValue(Integer.class));
                                        }
                                    }else {
                                        notificationList.remove(i);
                                        Log.d(TAG, "dataSnap: 202" + notificationList.size());
                                    }

                                }

                            }else{
                                if (waterLvl <= 300){
                                    notificationList.add(new Lists(bucketID,waterLvl,waterRequired,waterDrunk, waterLoss));
                                }
                            }
                    }
                    /*
                if (notificationList.size() ==0){
                    int waterLvl = dataSnapshot.child("waterLvl").getValue(Integer.class);
                    int waterLoss = dataSnapshot.child("waterLoss").getValue(Integer.class);
                    int waterRequired = dataSnapshot.child("waterRequired").getValue(Integer.class);
                    int waterDrunk = dataSnapshot.child("waterDrunk").getValue(Integer.class);
                    Log.d("MyApp", "dataSnap: " + bucketID);
                    if (waterLvl <= 300){
                        notificationList.add(new Lists(bucketID,waterLvl,waterRequired,waterDrunk, waterLoss));
                    }
                }else{
                        Log.d("MyApp", "dataSnap: "+ j);
                        boolean notificationFound = false;
                        for (int i = 0; i < notificationList.size(); i++ ){
                            if (notificationList.get(i).getBucketID() == bucketID){
                                notificationFound = true;
                            }
                        }
                        if (notificationFound){
                            for (int i = 0; i < notificationList.size(); i++) {
                                if (notificationList.get(i).getBucketID() == bucketID) {
                                    if (dataSnapshot.child("waterLvl").getValue(Integer.class) <= 300) {
                                        notificationList.get(i).setWaterLoss(dataSnapshot.child("waterLoss").getValue(Integer.class));
                                        notificationList.get(i).setWaterDrunk(dataSnapshot.child("waterDrunk").getValue(Integer.class));
                                        notificationList.get(i).setWaterLvl(dataSnapshot.child("waterLvl").getValue(Integer.class));
                                        notificationList.get(i).setWaterRequired(dataSnapshot.child("waterRequired").getValue(Integer.class));
                                    } else {
                                        notificationList.remove(i);
                                    }
                                }
                            }

                        }else{
                                int waterLvl = dataSnapshot.child("waterLvl").getValue(Integer.class);
                                int waterLoss = dataSnapshot.child("waterLoss").getValue(Integer.class);
                                int waterRequired = dataSnapshot.child("waterRequired").getValue(Integer.class);
                                int waterDrunk = dataSnapshot.child("waterDrunk").getValue(Integer.class);
                                Log.d("MyApp", "dataSnap: " + bucketID);
                                if (waterLvl <= 300){
                                    notificationList.add(new Lists(bucketID,waterLvl,waterRequired,waterDrunk, waterLoss));
                                }
                        }
                    }*/
                }
            }else{
                int waterLvl = dataSnapshot.child("waterLvl").getValue(Integer.class);
                int waterLoss = dataSnapshot.child("waterLoss").getValue(Integer.class);
                int waterRequired = dataSnapshot.child("waterRequired").getValue(Integer.class);
                int waterDrunk = dataSnapshot.child("waterDrunk").getValue(Integer.class);
                Log.d("MyApp", "dataSnap: " + bucketID);
                arrayList.add(new Lists(bucketID,waterLvl,waterRequired,waterDrunk, waterLoss));
                if (waterLvl <= 300){
                    notificationList.add(new Lists(bucketID,waterLvl,waterRequired,waterDrunk, waterLoss));
                }
            }
        }


        Log.d("MyApp", "Value is: " + notificationList.size());
        Log.d("MyApp", "Value is: " + arrayList.size());
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
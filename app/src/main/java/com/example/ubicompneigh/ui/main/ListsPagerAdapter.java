package com.example.ubicompneigh.ui.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ubicompneigh.R;
import com.example.ubicompneigh.ui.main.Lists;

import java.util.ArrayList;
import java.util.List;

public class ListsPagerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Lists> arrayList;
    private TextView bucketId, waterLvl, waterRequired, waterDrunk, waterLoss;
    public ListsPagerAdapter(Context context, ArrayList<Lists> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.fragment_listmain, parent, false);
        bucketId = convertView.findViewById(R.id.bucket_id);
        waterLvl = convertView.findViewById(R.id.water_lvl);
        waterRequired = convertView.findViewById(R.id.water_required);
        waterDrunk = convertView.findViewById(R.id.water_drunk);
        waterLoss = convertView.findViewById(R.id.water_loss);

        bucketId.setText(" " + arrayList.get(position).getBucketID());
        waterLvl.setText(" " + arrayList.get(position).getWaterLvl());
        waterRequired.setText(" " + arrayList.get(position).getWaterRequired());
        waterDrunk.setText(" " + arrayList.get(position).getWaterDrunk());
        waterLoss.setText(" " + arrayList.get(position).getWaterLoss());

        return convertView;
    }

}
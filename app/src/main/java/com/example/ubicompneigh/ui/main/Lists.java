package com.example.ubicompneigh.ui.main;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Lists {
    private String bucketID;
    private String dateTime;
    private int waterLvl;
    private int waterLoss;
    private int waterDrunk;
    private String reasons;
    private int waterRequired;

    public String getBucketID() {
        return bucketID;
    }

    public void setBucketID(String bucketID) {
        this.bucketID = bucketID;
    }

    public int getWaterLvl() {
        return waterLvl;
    }

    public void setWaterLvl(int waterLvl) {
        this.waterLvl = waterLvl;
    }

    public int getWaterLoss() {
        return waterLoss;
    }

    public void setWaterLoss(int waterLoss) {
        this.waterLoss = waterLoss;
    }

    public int getWaterRequired() {
        return waterRequired;
    }

    public int getWaterDrunk() {
        return waterDrunk;
    }

    public void setWaterDrunk(int waterDrunk) {
        this.waterDrunk = waterDrunk;
    }

    public void setWaterRequired(int waterRequired) {
        this.waterRequired = waterRequired;
    }

    public Lists(String bucketID, int waterLvl, int waterRequired, int waterDrunk, int waterLoss) {
        this.bucketID = bucketID;
        this.waterLvl = waterLvl;
        this.waterLoss = waterLoss;
        this.waterDrunk = waterDrunk;
        this.waterRequired = waterRequired;
    }

}

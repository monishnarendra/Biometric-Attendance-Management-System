package com.example.asus.fp_ams_mnmg.Model;

/**
 * Created by Asus on 29-04-2018.
 */

public class Item{
    private String Date;
    private int ArrivalTime;
    private int DepartedTime;

    public Item(String date, int arrivalTime, int departedTime) {
        this.Date = date;
        this.ArrivalTime = arrivalTime;
        this.DepartedTime = departedTime;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getArrivalTime() {
        return ArrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        ArrivalTime = arrivalTime;
    }

    public int getDepartedTime() {
        return DepartedTime;
    }

    public void setDepartedTime(int departedTime) {
        DepartedTime = departedTime;
    }

}

package com.project.commoditiesCurrentPrice.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "RecordTABLE")
public class Record implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "state")
    @SerializedName("state")
    @Expose
    private String state;

    @ColumnInfo(name = "district")
    @SerializedName("district")
    @Expose
    private String district;

    @ColumnInfo(name = "market")
    @SerializedName("market")
    @Expose
    private String market;

    @ColumnInfo(name = "commodity")
    @SerializedName("commodity")
    @Expose
    private String commodity;

    @ColumnInfo(name = "variety")
    @SerializedName("variety")
    @Expose
    private String variety;

    @ColumnInfo(name = "arrival_date")
    @SerializedName("arrival_date")
    @Expose
    private String arrival_date;

    @ColumnInfo(name = "min_price")
    @SerializedName("min_price")
    @Expose
    private String min_price;

    @ColumnInfo(name = "max_price")
    @SerializedName("max_price")
    @Expose
    private String max_price;

    @ColumnInfo(name = "modal_price")
    @SerializedName("modal_price")
    @Expose
    private String modal_price;

    private boolean isSubItemVisible;

    public Record() {

    }

    @Ignore
    public Record(String state, String district, String market, String commodity, String variety, String arrival_date, String min_price, String max_price, String modal_price, boolean isSubItemVisible) {
        this.state = state;
        this.district = district;
        this.market = market;
        this.commodity = commodity;
        this.variety = variety;
        this.arrival_date = arrival_date;
        this.min_price = min_price;
        this.max_price = max_price;
        this.modal_price = modal_price;
        this.isSubItemVisible = isSubItemVisible;
    }

    public String getMax_price() {
        return max_price;
    }

    public void setModal_price(String modal_price) {
        this.modal_price = modal_price;
    }

    public String getModal_price() {
        return modal_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public String getUsername() {
        return state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getArrival_date() {
        return arrival_date;

    }

    public void setArrival_date(String arrival_date) {
        this.arrival_date = arrival_date;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public void setSubItemVisible(boolean isSubItemVisible) {
        this.isSubItemVisible = isSubItemVisible;
    }

    public boolean isSubItemVisible() {
        return isSubItemVisible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


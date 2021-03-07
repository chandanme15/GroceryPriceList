package com.project.commoditiesCurrentPrice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RecordsModel implements Serializable {
    @SerializedName("records")
    @Expose
    private List<Record> records;

    public RecordsModel() {}

    public RecordsModel(List<Record> records) {
        this.records = records;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}



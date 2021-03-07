package com.project.commoditiesCurrentPrice.utils;

import java.io.Serializable;

public class CacheTime implements Serializable {

    private Long time;

    public CacheTime() {
        time = 0L;
    }

    public CacheTime(long time) {
        this.time = time;
    }

    public long getCacheTime() {
        return time;
    }
}

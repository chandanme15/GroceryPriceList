package com.project.commoditiesCurrentPrice.dbRoom;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.project.commoditiesCurrentPrice.model.Record;

@Database(entities = {Record.class}, version = 1, exportSchema = false)
public abstract class RecordsDB extends RoomDatabase {

    public abstract RecordsDao recordsDao();

}

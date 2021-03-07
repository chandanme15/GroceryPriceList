package com.project.commoditiesCurrentPrice.dbRoom;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.project.commoditiesCurrentPrice.model.Record;

@Database(entities = {Record.class}, version = 1, exportSchema = false)
public abstract class RecordsDB extends RoomDatabase {

    public abstract RecordsDao recordsDao();

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}

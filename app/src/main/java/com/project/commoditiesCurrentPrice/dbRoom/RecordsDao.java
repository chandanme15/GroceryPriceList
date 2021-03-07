package com.project.commoditiesCurrentPrice.dbRoom;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.project.commoditiesCurrentPrice.model.Record;

import java.util.List;

@Dao
public interface RecordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecords(List<Record> recordList);

    @Query("DELETE FROM RecordTABLE")
    void deleteAllRecords();

    @Query("SELECT * FROM RecordTABLE")
    List<Record> getRecords();
}

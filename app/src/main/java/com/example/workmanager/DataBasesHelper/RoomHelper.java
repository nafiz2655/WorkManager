package com.example.workmanager.DataBasesHelper;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.workmanager.Model.GetDataModel;

import java.util.List;

@Dao
public interface RoomHelper {

    // Insert only if it doesn't exist
    @Insert()
    void insertDataRoom(GetDataModel getDataModel);

    @Query("SELECT * FROM GetDataModel ORDER BY id DESC")
    List<GetDataModel> readAllData();


    @Query("SELECT * FROM GetDataModel WHERE id = :id LIMIT 1")
    GetDataModel getDataById(int id);  // Method to fetch data by ID

    @Update
    void updateDataRoom(GetDataModel getDataModel);  // Update method

    @Delete
    void deleteData(GetDataModel getDataModel);





}

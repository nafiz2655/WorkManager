package com.example.workmanager.DataBases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.workmanager.DataBasesHelper.RoomHelper;
import com.example.workmanager.Model.GetDataModel;

@Database(entities = {GetDataModel.class},version = 1, exportSchema = false)
public abstract class RoomTable extends RoomDatabase {

    public abstract RoomHelper roomHelper();

    public static RoomTable roomTable(Context context){
        return Room.databaseBuilder(context, RoomTable.class, "Student")
                .allowMainThreadQueries()
                .build();
    }


}


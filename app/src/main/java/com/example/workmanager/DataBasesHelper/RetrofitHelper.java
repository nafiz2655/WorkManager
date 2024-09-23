package com.example.workmanager.DataBasesHelper;

import com.example.workmanager.Model.DeleteDataModel;
import com.example.workmanager.Model.GetDataModel;
import com.example.workmanager.Model.InsertDataModel;
import com.example.workmanager.Model.UpdateDataModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitHelper {

    // Insert Data
    @POST("insertimage.php")
    Call<Void> insertData(@Body InsertDataModel insertDataModel);

    // Get Data
    @GET("getdata.php")
    Call<List<GetDataModel>> getalldata();

    // Delete Data
    @POST("deletedata.php")
    Call<Void> deleteData(@Body DeleteDataModel deleteDataModel);

    // if no image select
    @POST("updatedata.php")
    Call<Void> updatedata(@Body UpdateDataModel updateDataModel);
    // if new image select
    @POST("update2.php")
    Call<Void> updatedataimage(@Body UpdateDataModel updateDataModel);

}

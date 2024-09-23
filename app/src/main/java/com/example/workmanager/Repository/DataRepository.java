package com.example.workmanager.Repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.workmanager.DataBasesHelper.RoomHelper;
import com.example.workmanager.Model.GetDataModel;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository {

    Context context;
    @Inject
    RoomHelper roomHelper;
    @Inject
    Call<List<GetDataModel>> provideGetData;
    @Inject
    boolean isNetworkConnected;
    @Inject
    public DataRepository(Context context, RoomHelper roomHelper, Call<List<GetDataModel>> provideGetData) {
        this.context = context;
        this.roomHelper = roomHelper;
        this.provideGetData = provideGetData;
    }

    // Define a callback interface
    public interface DataCallback {
        void onSuccess(List<GetDataModel> studentList);
        void onFailure(String errorMessage);
    }

    // Fetch data from either network or Room and use the callback to return the data
    public void getData(DataCallback callback) {
        try {
            if (checkInternet()) {
                // Fetch data from Retrofit (API)
                fetchFromNetwork(callback);
            } else {
                // Fetch data from Room (Local DB)
                List<GetDataModel> studentList = roomHelper.readAllData();
                if (studentList != null && !studentList.isEmpty()) {
                    callback.onSuccess(studentList); // Data available locally
                } else {
                    callback.onFailure("No data available locally.");
                }
            }
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
        }
    }

    private void fetchFromNetwork(DataCallback callback) {
        provideGetData.enqueue(new Callback<List<GetDataModel>>() {
            @Override
            public void onResponse(Call<List<GetDataModel>> call, Response<List<GetDataModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GetDataModel> networkDataList = response.body();

                    // Fetch all local data before processing
                    List<GetDataModel> localDataList = roomHelper.readAllData();

                    // Loop through the data received from the API
                    for (GetDataModel networkData : networkDataList) {
                        // Check if the data already exists in the Room database
                        GetDataModel existingData = roomHelper.getDataById(networkData.getId());  // Assuming you have a method like this in RoomHelper

                        if (existingData == null) {
                            // Data does not exist, insert it
                            roomHelper.insertDataRoom(networkData);
                        } else if (!existingData.equals(networkData)) {
                            // Data exists but has been updated, so update it
                            roomHelper.updateDataRoom(networkData);
                        }/*else  if (!networkDataList.contains(networkData)) {
                            // Delete local data if it's no longer present on the server
                            roomHelper.deleteData(networkData);
                        }*/
                    }

                /*    // Identify and delete any local data not in the server response
                   for (GetDataModel localData : localDataList) {
                        if (!networkDataList.contains(localData)) {
                            // Delete local data if it's no longer present on the server
                            roomHelper.deleteData(localData);
                        }
                    }*/

                    // Return all the data (newly added + existing) to the callback
                    callback.onSuccess(roomHelper.readAllData());
                } else {
                    callback.onFailure("Failed to retrieve data from server.");
                }
            }

            @Override
            public void onFailure(Call<List<GetDataModel>> call, Throwable throwable) {
                callback.onFailure(throwable.getMessage()); // Handle failure
            }
        });
    }

    // Method to check the internet connectivity status
    private boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}

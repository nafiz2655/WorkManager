package com.example.workmanager.Hilt.Module;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

import com.example.workmanager.DataBases.RetrofitRequest;
import com.example.workmanager.DataBases.RoomTable;
import com.example.workmanager.DataBasesHelper.RetrofitHelper;
import com.example.workmanager.DataBasesHelper.RoomHelper;
import com.example.workmanager.Model.GetDataModel;

import java.util.Collection;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Call;

@Module
@InstallIn(SingletonComponent.class)
public class SingletonModule {

    @Provides
    @Singleton
    public RetrofitHelper provideRetrofitHelper() {
        return RetrofitRequest.getAPI().create(RetrofitHelper.class);
    }

    @Provides
    @Singleton
    public Call<List<GetDataModel>> provideGetDataCall(RetrofitHelper retrofitHelper) {
        return retrofitHelper.getalldata();
    }

    @Provides
    @Singleton
    public RoomTable roomTable(@ApplicationContext Context context){
        return RoomTable.roomTable(context);
    }

    @Provides
    @Singleton
    public RoomHelper roomHelper(RoomTable roomTable){
        return roomTable.roomHelper();
    }

    @Provides
    @Singleton
    public boolean isNetworkConnected(@ApplicationContext Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }
        return false;
    }


}

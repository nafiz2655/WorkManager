package com.example.workmanager.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.workmanager.Adapter.MyAdapter;
import com.example.workmanager.DataBasesHelper.RetrofitHelper;
import com.example.workmanager.DataBasesHelper.RoomHelper;
import com.example.workmanager.Model.GetDataModel;
import com.example.workmanager.R;
import com.example.workmanager.Repository.DataRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ViewStudentInfo extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private ArrayList<GetDataModel> arrayList;
    private ProgressBar progress;
    private ImageView add;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    Call<List<GetDataModel>> provideGetDataCall;
    @Inject
    RoomHelper roomHelper;
    @Inject
    boolean isNetworkConnected;
    @Inject
    RetrofitHelper retrofitHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_viewstudentinfo);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();
        setupRecyclerView();
        setupClickListeners();

        if (isNetworkConnected){
            Toast.makeText(this, "Internet Connected", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, "No Internet Internet", Toast.LENGTH_SHORT).show();
        }

        // Initial data load
        loadData();
    }

    private void initializeViews() {
        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        progress = findViewById(R.id.progress);
        add = findViewById(R.id.add);
        swipeRefreshLayout = findViewById(R.id.refreshLayout);
    }

    private void setupRecyclerView() {
        myAdapter = new MyAdapter(arrayList, this,isNetworkConnected,retrofitHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }

    private void setupClickListeners() {
        add.setOnClickListener(view -> {
            startActivity(new Intent(ViewStudentInfo.this, InsertStudentInfo.class));
            Toast.makeText(ViewStudentInfo.this, "Redirecting to add data", Toast.LENGTH_SHORT).show();
        });

        // Reload data when the user swipes down
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Clear the old data and reload from the repository
            arrayList.clear();
            loadData();
        });
    }

    public void loadData() {
        // Ensure a new call is created for each data fetch
        progress.setVisibility(View.VISIBLE);

        // Create a new instance of DataRepository for each load
        DataRepository dataRepository = new DataRepository(ViewStudentInfo.this, roomHelper, provideGetDataCall.clone());

        // Fetch the data from the repository
        dataRepository.getData(new DataRepository.DataCallback() {
            @Override
            public void onSuccess(List<GetDataModel> studentList) {
                arrayList.clear(); // Clear the previous list
                arrayList.addAll(studentList); // Add the new data

                // Notify adapter of data changes
                if (myAdapter == null) {
                    myAdapter = new MyAdapter(arrayList, ViewStudentInfo.this);
                    recyclerView.setAdapter(myAdapter);
                } else {
                    myAdapter.notifyDataSetChanged();
                }

                // Hide the progress bar and stop the refresh animation
                progress.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle error
                Toast.makeText(ViewStudentInfo.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

                // Hide the progress bar and stop the refresh animation
                progress.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}

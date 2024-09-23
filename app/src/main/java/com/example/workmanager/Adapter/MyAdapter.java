package com.example.workmanager.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workmanager.DataBasesHelper.RetrofitHelper;
import com.example.workmanager.Model.DeleteDataModel;
import com.example.workmanager.Model.GetDataModel;
import com.example.workmanager.R;
import com.example.workmanager.View.UpdateStudentInfo;

import java.util.ArrayList;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    ArrayList<GetDataModel> arrayList;
    Context context;
    @Inject
    boolean isNetworkConnected;

    @Inject
    RetrofitHelper retrofitHelper;

    public MyAdapter() {
    }

    public MyAdapter(ArrayList<GetDataModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public MyAdapter(ArrayList<GetDataModel> arrayList, Context context, boolean isNetworkConnected) {
        this.arrayList = arrayList;
        this.context = context;
        this.isNetworkConnected = isNetworkConnected;
    }

    public MyAdapter(ArrayList<GetDataModel> arrayList, Context context, boolean isNetworkConnected, RetrofitHelper retrofitHelper) {
        this.arrayList = arrayList;
        this.context = context;
        this.isNetworkConnected = isNetworkConnected;
        this.retrofitHelper = retrofitHelper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,roll,reg,phone,subject,address,tv_uptade,tv_delete;
        CircleImageView profile_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            roll = itemView.findViewById(R.id.roll);
            reg = itemView.findViewById(R.id.reg);
            phone = itemView.findViewById(R.id.phone);
            subject = itemView.findViewById(R.id.subject);
            address = itemView.findViewById(R.id.address);
            profile_image = itemView.findViewById(R.id.profile_image);
            tv_uptade = itemView.findViewById(R.id.tv_uptade);
            tv_delete = itemView.findViewById(R.id.tv_delete);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       if (isNetworkConnected){
           View view = layoutInflater.inflate(R.layout.layout,parent,false);
           return new ViewHolder(view);
       }else {
           View view = layoutInflater.inflate(R.layout.offlinelayout,parent,false);
           return new ViewHolder(view);
       }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GetDataModel dataModel = arrayList.get(position);


        String sId = String.valueOf(dataModel.getId());
        String sname = dataModel.getName();
        String sroll = dataModel.getRoll();
        String srej = dataModel.getReg();
        String sphone = dataModel.getPhone();
        String ssub = dataModel.getSub();
        String sadd = dataModel.getAddress();

        holder.name.setText(sname);
        holder.roll.setText(sroll);
        holder.reg.setText(srej);
        holder.phone.setText(sphone);
        holder.subject.setText(ssub);
        holder.address.setText(sadd);

        String url = "https://maltinamax.quillncart.com/appsdta/test/retrofit/images/"+dataModel.getImage();

        String uri = dataModel.getImage();


        Glide.with(context).load(url)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(holder.profile_image);

        holder.tv_uptade.setOnClickListener( view -> {

            UpdateStudentInfo.ID = sId;
            UpdateStudentInfo.ADD= sadd;
            UpdateStudentInfo.NAME = sname;
            UpdateStudentInfo.ROLL = sroll;
            UpdateStudentInfo.REG = srej;
            UpdateStudentInfo.PHONE = sphone;
            UpdateStudentInfo.SUB = ssub;
            UpdateStudentInfo.URL = uri;
            context.startActivity(new Intent(context, UpdateStudentInfo.class));
        });

        //-----------------------------------
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Integer i_id = Integer.parseInt(sId);


                retrofitHelper.deleteData(new DeleteDataModel(i_id)).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(context, "Data Delete success", Toast.LENGTH_SHORT).show();

                        // Remove the item from the ArrayList
                        arrayList.remove(position);

                        // Notify the adapter about the removed item
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, arrayList.size());
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                        Toast.makeText(context, "Data Update Failed", Toast.LENGTH_SHORT).show();


                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}

package com.example.workmanager.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.workmanager.DataBasesHelper.RetrofitHelper;
import com.example.workmanager.DataBasesHelper.RoomHelper;
import com.example.workmanager.Model.GetDataModel;
import com.example.workmanager.Model.InsertDataModel;
import com.example.workmanager.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@AndroidEntryPoint
public class InsertStudentInfo extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_FILE = 1;

    EditText tv_name,tv_roll,tv_reg,tv_subject,tv_phone,tv_address;
    TextView submit;
    ProgressBar progressbar;
    CircleImageView profile_image;
    Uri mainUri;
    Bitmap bitmap;
    String encodedImageString;
    RelativeLayout pickImage;

    @Inject
    Call<List<GetDataModel>> provideGetData;

    @Inject
    RetrofitHelper retrofitHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insertstudentinfo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv_name = findViewById(R.id.tv_name);
        tv_roll = findViewById(R.id.tv_roll);
        tv_reg = findViewById(R.id.tv_reg);
        tv_subject = findViewById(R.id.tv_subject);
        tv_phone = findViewById(R.id.tv_phone);
        tv_address = findViewById(R.id.tv_address);
        submit = findViewById(R.id.tv_submit);
        progressbar = findViewById(R.id.progressbar);
        profile_image = findViewById(R.id.profile_image);
        pickImage = findViewById(R.id.pickImage);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sName =tv_name.getText().toString();
                String sRoll =tv_roll.getText().toString();
                String sReg =tv_reg.getText().toString();
                String sSub =tv_subject.getText().toString();
                String sPhone =tv_phone.getText().toString();
                String sAddress =tv_address.getText().toString();
                String image = encodedImageString;


                if (encodedImageString !=null){
                    progressbar.setVisibility(View.VISIBLE);
                    InsertData(sName,sRoll,sReg,sSub,sPhone,sAddress,image);
                }else {
                    Toast.makeText(InsertStudentInfo.this, "Please Select Image", Toast.LENGTH_SHORT).show();

                }

            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_FILE);
        }


        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mGetContent.launch("image/*");
            }
        });



    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    //profile_image.setImageURI(uri);
                    mainUri = uri;

                    try {

                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        profile_image.setImageBitmap(bitmap);
                        encodeImagetobitmap(bitmap);

                    }catch (Exception e){
                        Toast.makeText(InsertStudentInfo.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void encodeImagetobitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        encodedImageString = Base64.encodeToString(bytes, Base64.DEFAULT);


    }

    // end oncreate


    private void InsertData(String sName, String sRoll, String sReg, String sSub, String sPhone, String sAddress, String sImage) {

/*
        Helper helper = Services.getAPI().create(Helper.class);

 */
        Call<Void> insertData = retrofitHelper.insertData(new InsertDataModel(sName,sRoll,sReg,sSub,sPhone,sImage,sAddress));

        insertData.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(InsertStudentInfo.this, "Data Insert success", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
                startActivity(new Intent(InsertStudentInfo.this,ViewStudentInfo.class));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Toast.makeText(InsertStudentInfo.this, "Data Insert Failed", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_FILE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted
                } else {
                    // Permission denied
                    // Show a message to the user about why the permission is needed
                }
                return;
            }
        }
    }
}
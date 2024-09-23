package com.example.workmanager.View;

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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.workmanager.DataBases.RetrofitRequest;
import com.example.workmanager.DataBasesHelper.RetrofitHelper;
import com.example.workmanager.Model.UpdateDataModel;
import com.example.workmanager.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class UpdateStudentInfo extends AppCompatActivity {

    EditText tv_name,tv_roll,tv_reg,tv_subject,tv_phone,tv_address;
    TextView submit;
    ProgressBar progressbar;
    CircleImageView profile_image;
    Uri mainUri;
    Bitmap bitmap;
    String encodedImageString;
    RelativeLayout pickImage;


    public static String ID ;
    public static String ROLL = "";
    public static String REG = "";
    public static String PHONE = "";
    public static String SUB = "";
    public static String ADD = "";
    public static String URL = "";
    public static String NAME = "";

    @Inject
    RetrofitHelper retrofitHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_student_info);
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


        tv_name.setText(NAME);
        tv_roll.setText(ROLL);
        tv_reg.setText(REG);
        tv_subject.setText(SUB);
        tv_phone.setText(PHONE);
        tv_address.setText(ADD);


        Toast.makeText(this, ""+ID, Toast.LENGTH_SHORT).show();

        String url = "https://maltinamax.quillncart.com/appsdta/test/retrofit/images/"+URL;
        Glide.with(this).load(url)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(profile_image);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressbar.setVisibility(View.VISIBLE);
                String sName =tv_name.getText().toString();
                String sRoll =tv_roll.getText().toString();
                String sReg =tv_reg.getText().toString();
                String sSub =tv_subject.getText().toString();
                String sPhone =tv_phone.getText().toString();
                String sAddress =tv_address.getText().toString();
                String sImage = URL;
                int id = Integer.parseInt(ID);


                if (encodedImageString != null){
                    InsertDataImage(id,sName,sRoll,sReg,sSub,sPhone,sAddress,encodedImageString);
                }else {
                    InsertData(id,sName,sRoll,sReg,sSub,sPhone,sAddress,sImage);
                }






            }
        });

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mGetContent.launch("image/*");
            }
        });


    }//

    private void InsertDataImage(int id, String sName, String sRoll, String sReg, String sSub, String sPhone, String sAddress, String encodedImageString) {


        retrofitHelper.updatedataimage(new UpdateDataModel(id,sName,sRoll,sReg,sSub,sPhone,encodedImageString,sAddress)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(UpdateStudentInfo.this, "Data Update success", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Toast.makeText(UpdateStudentInfo.this, "Data Update Failed", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);

            }
        });
    }

    private void InsertData(int id, String sName, String sRoll, String sReg, String sSub, String sPhone, String sAddress, String sImage) {




        retrofitHelper.updatedata(new UpdateDataModel(id,sName,sRoll,sReg,sSub,sPhone,sImage,sAddress)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(UpdateStudentInfo.this, "Data Update success", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Toast.makeText(UpdateStudentInfo.this, "Data Update Failed", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);

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
                        Toast.makeText(UpdateStudentInfo.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void encodeImagetobitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        encodedImageString = Base64.encodeToString(bytes, Base64.DEFAULT);
    }



}
package com.example.osos.uploadimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button btnChoose,btnUpload;
    private ImageView imageView;
    private Bitmap bitmap;
    private static final int IMG_REQUEST=777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChoose=(Button)findViewById(R.id.choosebtn);
        btnUpload=(Button)findViewById(R.id.uploadbtn);
        imageView=(ImageView)findViewById(R.id.image);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

    }


    void selectImage(){

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQUEST&&resultCode==RESULT_OK&&data!=null){

            Uri path=data.getData();

            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                btnChoose.setEnabled(false);
                btnUpload.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }

    //mehtod that covert image to array of bytes then to string by Base64
    String imageToString(){

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte [] imageByte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte,Base64.DEFAULT);
    }
    void uploadImage(){

        String image= imageToString();
        ApiInterface apiInterface=ApiClient.getApiClient().create(ApiInterface.class);
        Call<ImageClasss>call=apiInterface.uploadImage("osos Title","image","29",image);

        call.enqueue(new Callback<ImageClasss>() {
            @Override
            public void onResponse(Call<ImageClasss> call, Response<ImageClasss> response) {
                ImageClasss imageClass=response.body();
                Toast.makeText(MainActivity.this, "server respose"+imageClass.getResponse(), Toast.LENGTH_SHORT).show();
                btnChoose.setEnabled(true);
                btnUpload.setEnabled(false);
            }

            @Override
            public void onFailure(Call<ImageClasss> call, Throwable t) {

            }
        });

    }
}


package com.example.osos.uploadimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private Button btnChoose,btnUpload;
    private ImageView imageView;
    private Bitmap bitmap;
    private String filePath;
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

                if (!filePath.equals(""))
                    uploadImageAsFile(filePath);
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
            filePath = data.getData().getPath();

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
    /*
    //upload image as a string
    void uploadImage(){

        String image= imageToString();
        ApiInterface apiInterface=ApiClient.getApiClient().create(ApiInterface.class);
        Call<ImageClass>call=apiInterface.uploadImage("osos Title","image","29",image);

        call.enqueue(new Callback<ImageClass>() {
            @Override
            public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
                ImageClass imageClass=response.body();
                Toast.makeText(MainActivity.this, "server respose"+imageClass.getResponse(), Toast.LENGTH_SHORT).show();
                btnChoose.setEnabled(true);
                btnUpload.setEnabled(false);
            }

            @Override
            public void onFailure(Call<ImageClass> call, Throwable t) {

            }
        });

    }
    */

    //upload image as a file by multipart
    void uploadImageAsFile(String filePath) {

        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface uploadAPIs = retrofit.create(ApiInterface.class);

        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);

        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), fileReqBody);

        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");

        Call call = uploadAPIs.uploadImage(part, description);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                Toast.makeText(MainActivity.this, "server respose", Toast.LENGTH_SHORT).show();
                btnChoose.setEnabled(true);
                btnUpload.setEnabled(false);
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

    }
}


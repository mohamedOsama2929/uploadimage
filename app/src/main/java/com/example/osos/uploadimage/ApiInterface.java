package com.example.osos.uploadimage;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    /*
    //if image as a string not Multipart
    @FormUrlEncoded
    @POST("blogs")
    Call<ImageClass> uploadImage(@Field("title") String title
            ,@Field("content") String content
            ,@Field("type_id")String type_id
            ,@Field("image")String image);
            */


    //image as a file by multipart
    @Multipart
    @POST("blogs")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file, @Part("name") RequestBody requestBody);
}

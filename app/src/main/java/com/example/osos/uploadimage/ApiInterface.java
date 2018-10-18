package com.example.osos.uploadimage;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("blogs")
    Call<ImageClasss> uploadImage(@Field("title") String title
            ,@Field("content") String content
            ,@Field("type_id")String type_id
            ,@Field("image")String image);
}

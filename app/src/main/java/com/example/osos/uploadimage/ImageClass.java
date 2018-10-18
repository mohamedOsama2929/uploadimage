package com.example.osos.uploadimage;

import com.google.gson.annotations.SerializedName;

class ImageClasss {

    @SerializedName("image")
    private String image;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("type_id")
    private String type_id;

    @SerializedName("response")
    private  String response;
    public String getResponse() {
        return response;
    }
}

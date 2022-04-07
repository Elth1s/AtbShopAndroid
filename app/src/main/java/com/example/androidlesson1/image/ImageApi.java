package com.example.androidlesson1.image;

import com.example.androidlesson1.image.dto.ImageDTO;
import com.example.androidlesson1.image.dto.ImageResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ImageApi {
    @POST("/api/Image/upload")
    public Call<ImageResponseDTO> uploadImage(@Body ImageDTO model);
}
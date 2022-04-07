package com.example.androidlesson1.user.network;

import com.example.androidlesson1.user.dto.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {
    @GET("/api/Account/users")
    public Call<List<UserDTO>> users();
}

package com.example.androidlesson1.user.network;

import com.example.androidlesson1.account.dto.AccountResponseDTO;
import com.example.androidlesson1.account.dto.LoginDTO;
import com.example.androidlesson1.user.dto.EditUserDTO;
import com.example.androidlesson1.user.dto.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {
    @GET("/api/Account/users")
    public Call<List<UserDTO>> users();
    @GET("/api/Account/get-user/{id}")
    public Call<UserDTO> getUser(@Path("id") int id);
    @PUT("/api/Account/edit-profile/{id}")
    public Call<AccountResponseDTO> editUser(@Path("id") int id, @Body EditUserDTO model);
}

package com.example.androidlesson1.account.network;


import com.example.androidlesson1.account.dto.AccountResponseDTO;
import com.example.androidlesson1.account.dto.RegisterDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountApi {
    @POST("/api/Account/register")
    public Call<AccountResponseDTO> register(@Body RegisterDTO model);
}

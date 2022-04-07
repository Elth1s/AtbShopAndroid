package com.example.androidlesson1.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.androidlesson1.R;
import com.example.androidlesson1.account.dto.AccountResponseDTO;
import com.example.androidlesson1.account.dto.RegisterDTO;
import com.example.androidlesson1.account.network.AccountService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onRegister(View view){
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("ss@gmail.com");

        AccountService.getInstance()
                .jsonApi()
                .register(registerDTO)
                .enqueue(new Callback<AccountResponseDTO>() {
                    @Override
                    public void onResponse(Call<AccountResponseDTO> call, Response<AccountResponseDTO> response) {
                        AccountResponseDTO data = response.body();
                    }

                    @Override
                    public void onFailure(Call<AccountResponseDTO> call, Throwable t) {
                        String str = t.toString();
                        int a =12;
                    }
                });
    }
}
package com.example.androidlesson1.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.androidlesson1.BaseActivity;
import com.example.androidlesson1.MainActivity;
import com.example.androidlesson1.R;
import com.example.androidlesson1.account.dto.AccountResponseDTO;
import com.example.androidlesson1.account.dto.LoginDTO;
import com.example.androidlesson1.account.dto.RegisterDTO;
import com.example.androidlesson1.account.network.AccountService;
import com.example.androidlesson1.application.HomeApplication;
import com.example.androidlesson1.security.JwtSecurityService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    LoginDTO loginDTO;

    TextInputLayout emailLayout;
    TextInputLayout passwordLayout;

    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginDTO = new LoginDTO();

        emailLayout = findViewById(R.id.emailTextLayout);
        passwordLayout = findViewById(R.id.passwordTextLayout);

        emailEditText = findViewById(R.id.emailTextInput);
        passwordEditText = findViewById(R.id.passwordTextInput);
    }

    public void onLogin(View view){

        if(validate()) {
            AccountService.getInstance()
                    .jsonApi()
                    .login(loginDTO)
                    .enqueue(new Callback<AccountResponseDTO>() {
                        @Override
                        public void onResponse(Call<AccountResponseDTO> call, Response<AccountResponseDTO> response) {
                            AccountResponseDTO data = response.body();
                            JwtSecurityService jwtService = (JwtSecurityService) HomeApplication.getInstance();
                            jwtService.saveJwtToken(data.getToken());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<AccountResponseDTO> call, Throwable t) {
                            String str = t.toString();
                            int a = 12;
                        }
                    });
        }

    }
    boolean validate(){
        boolean isValid=true;
        if(!isEmailValid())
            isValid=false;
        if(!isPasswordValid())
            isValid=false;

        return isValid;
    }

    boolean isEmailValid(){
        String email = emailEditText.getText().toString();

        if(email.isEmpty()) {
            emailLayout.setError("Email is required");
            return false;
        }
        emailLayout.setError(null);
        loginDTO.setEmail(email);
        return true;
    }

    boolean isPasswordValid(){
        String password = passwordEditText.getText().toString();

        if(password.isEmpty()) {
            passwordLayout.setError("Password is required");
            return false;
        }
        passwordLayout.setError(null);
        loginDTO.setPassword(password);
        return true;
    }
}
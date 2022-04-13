package com.example.androidlesson1.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.example.androidlesson1.BaseActivity;
import com.example.androidlesson1.MainActivity;
import com.example.androidlesson1.R;
import com.example.androidlesson1.account.dto.AccountResponseDTO;
import com.example.androidlesson1.account.dto.RegisterDTO;
import com.example.androidlesson1.account.network.AccountService;
import com.example.androidlesson1.application.HomeApplication;
import com.example.androidlesson1.constants.Urls;
import com.example.androidlesson1.image.ImageService;
import com.example.androidlesson1.image.dto.ImageDTO;
import com.example.androidlesson1.image.dto.ImageResponseDTO;
import com.example.androidlesson1.security.JwtSecurityService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    ImageView previewImageView;

    int SELECT_PICTURE = 200;

    RegisterDTO registerDTO;

    TextInputLayout firstNameLayout;
    TextInputLayout secondNameLayout;
    TextInputLayout emailLayout;
    TextInputLayout phoneLayout;
    TextInputLayout passwordLayout;
    TextInputLayout confirmPasswordLayout;

    TextInputEditText firstNameEditText;
    TextInputEditText secondNameEditText;
    TextInputEditText emailEditText;
    TextInputEditText phoneEditText;
    TextInputEditText passwordEditText;
    TextInputEditText confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerDTO = new RegisterDTO();

        previewImageView = findViewById(R.id.previewImageView);

        firstNameLayout = findViewById(R.id.firstNameTextLayout);
        secondNameLayout = findViewById(R.id.secondNameTextLayout);
        emailLayout = findViewById(R.id.emailTextLayout);
        phoneLayout = findViewById(R.id.phoneTextLayout);
        passwordLayout = findViewById(R.id.passwordTextLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordTextLayout);

        firstNameEditText = findViewById(R.id.firstNameTextInput);
        secondNameEditText = findViewById(R.id.secondNameTextInput);
        emailEditText = findViewById(R.id.emailTextInput);
        phoneEditText = findViewById(R.id.phoneTextInput);
        passwordEditText = findViewById(R.id.passwordTextInput);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordTextInput);
    }

    public void onRegister(View view){

        if(validate()) {
            AccountService.getInstance()
                    .jsonApi()
                    .register(registerDTO)
                    .enqueue(new Callback<AccountResponseDTO>() {
                        @Override
                        public void onResponse(Call<AccountResponseDTO> call, Response<AccountResponseDTO> response) {
                            AccountResponseDTO data = response.body();
                            JwtSecurityService jwtService = (JwtSecurityService) HomeApplication.getInstance();
                            jwtService.saveJwtToken(data.getToken());
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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
        if(!isFirstNameValid())
            isValid=false;
        if(!isSecondNameValid())
            isValid=false;
        if(!isEmailValid())
            isValid=false;
        if(!isPhoneValid())
            isValid=false;
        if(!isPasswordValid())
            isValid=false;
        if(!isConfirmPasswordValid())
            isValid=false;

        return isValid;
    }

    boolean isFirstNameValid(){
        String firstName = firstNameEditText.getText().toString();

        if(firstName.isEmpty()) {
            firstNameLayout.setError("First name is required");
            return false;
        }
        firstNameLayout.setError(null);
        registerDTO.setFirstName(firstName);
        return true;
    }

    boolean isSecondNameValid(){
        String secondName = secondNameEditText.getText().toString();

        if(secondName.isEmpty()) {
            secondNameLayout.setError("Second name is required");
            return false;
        }
        secondNameLayout.setError(null);
        registerDTO.setSecondName(secondName);
        return true;
    }

    boolean isEmailValid(){
        String email = emailEditText.getText().toString();

        if(email.isEmpty()) {
            emailLayout.setError("Email is required");
            return false;
        }
        emailLayout.setError(null);
        registerDTO.setEmail(email);
        return true;
    }

    boolean isPhoneValid(){
        String phone = phoneEditText.getText().toString();

        if(phone.isEmpty()) {
            phoneLayout.setError("Phone is required");
            return false;
        }
        phoneLayout.setError(null);
        registerDTO.setPhone(phone);
        return true;
    }

    boolean isPasswordValid(){
        String password = passwordEditText.getText().toString();

        if(password.isEmpty()) {
            passwordLayout.setError("Password is required");
            return false;
        }
        passwordLayout.setError(null);
        registerDTO.setPassword(password);
        return true;
    }

    boolean isConfirmPasswordValid(){
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if(confirmPassword.isEmpty()) {
            confirmPasswordLayout.setError("Confirm password is required");
            return false;
        }
        confirmPasswordLayout.setError(null);
        registerDTO.setConfirmPassword(confirmPassword);
        return true;
    }

    public void onSelectPhoto(View view){
        imageChooser();
    }
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri uri = data.getData();
                if (null != uri) {
                    // update the preview image in the layout
                    previewImageView.setImageURI(uri);

                    Bitmap bitmap= null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // initialize byte stream
                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
                    // compress Bitmap
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    // Initialize byte array
                    byte[] bytes=stream.toByteArray();
                    // get base64 encoded string
                    String sImage= Base64.encodeToString(bytes,Base64.DEFAULT);

                    registerDTO.setPhoto(sImage);

                }
            }
        }
    }
}
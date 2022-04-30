package com.example.androidlesson1.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.androidlesson1.MainActivity;
import com.example.androidlesson1.R;
import com.example.androidlesson1.account.RegisterActivity;
import com.example.androidlesson1.account.dto.AccountResponseDTO;
import com.example.androidlesson1.account.network.AccountService;
import com.example.androidlesson1.application.HomeApplication;
import com.example.androidlesson1.constants.Urls;
import com.example.androidlesson1.security.JwtSecurityService;
import com.example.androidlesson1.user.dto.EditUserDTO;
import com.example.androidlesson1.user.dto.UserDTO;
import com.example.androidlesson1.user.network.UserService;
import com.example.androidlesson1.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity {

    EditUserDTO editUserDTO;
    Integer userId;

    TextInputLayout firstNameLayout;
    TextInputLayout secondNameLayout;
    TextInputLayout emailLayout;
    TextInputLayout phoneLayout;


    TextInputEditText firstNameEditText;
    TextInputEditText secondNameEditText;
    TextInputEditText emailEditText;
    TextInputEditText phoneEditText;
    ImageView previewImageView;

    int SELECT_PICTURE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        editUserDTO = new EditUserDTO();

        firstNameLayout = findViewById(R.id.firstNameTextLayout);
        secondNameLayout = findViewById(R.id.secondNameTextLayout);
        emailLayout = findViewById(R.id.emailTextLayout);
        phoneLayout = findViewById(R.id.phoneTextLayout);

        previewImageView = findViewById(R.id.previewImageView);
        firstNameEditText = findViewById(R.id.firstNameTextInput);
        secondNameEditText = findViewById(R.id.secondNameTextInput);
        emailEditText = findViewById(R.id.emailTextInput);
        phoneEditText = findViewById(R.id.phoneTextInput);

        Bundle b = getIntent().getExtras();
        int value =0;
        if(b !=null)
            value=b.getInt("id");
        userId = new Integer(value);

        CommonUtils.showLoading(this);
        UserService.getInstance()
                .jsonApi()
                .getUser(userId)
                .enqueue(new Callback<UserDTO>() {
                    @Override
                    public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                        UserDTO data = response.body();

                        editUserDTO.setEmail(data.getEmail());
                        editUserDTO.setFirstName(data.getFirstName());
                        editUserDTO.setSecondName(data.getSecondName());
                        editUserDTO.setPhone(data.getPhone());
                        editUserDTO.setPhoto(data.getPhoto());


                        firstNameEditText.setText(data.getFirstName());
                        secondNameEditText.setText(data.getSecondName());
                        emailEditText.setText(data.getEmail());
                        phoneEditText.setText(data.getPhone());

                        String url = Urls.BASE+data.getPhoto();
                        Glide.with(HomeApplication.getAppContext())
                                .load(url)
                                .apply(new RequestOptions().override(400, 400))
                                .into(previewImageView);


                        CommonUtils.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<UserDTO> call, Throwable t) {
                        String str = t.toString();
                        int a = 12;
                    }
                });
    }

    public void onEditUser(View view){

        if(validate()) {
            UserService.getInstance()
                    .jsonApi()
                    .editUser(userId, editUserDTO)
                    .enqueue(new Callback<AccountResponseDTO>() {
                        @Override
                        public void onResponse(Call<AccountResponseDTO> call, Response<AccountResponseDTO> response) {
                            AccountResponseDTO data = response.body();
                            JwtSecurityService jwtService = (JwtSecurityService) HomeApplication.getInstance();
                            jwtService.saveJwtToken(data.getToken());
                            Intent intent = new Intent(EditUserActivity.this, MainActivity.class);
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

        return isValid;
    }

    boolean isFirstNameValid(){
        String firstName = firstNameEditText.getText().toString();

        if(firstName.isEmpty()) {
            firstNameLayout.setError("First name is required");
            return false;
        }
        firstNameLayout.setError(null);
        editUserDTO.setFirstName(firstName);
        return true;
    }

    boolean isSecondNameValid(){
        String secondName = secondNameEditText.getText().toString();

        if(secondName.isEmpty()) {
            secondNameLayout.setError("Second name is required");
            return false;
        }
        secondNameLayout.setError(null);
        editUserDTO.setSecondName(secondName);
        return true;
    }

    boolean isEmailValid(){
        String email = emailEditText.getText().toString();

        if(email.isEmpty()) {
            emailLayout.setError("Email is required");
            return false;
        }
        emailLayout.setError(null);
        editUserDTO.setEmail(email);
        return true;
    }

    boolean isPhoneValid(){
        String phone = phoneEditText.getText().toString();

        if(phone.isEmpty()) {
            phoneLayout.setError("Phone is required");
            return false;
        }
        phoneLayout.setError(null);
        editUserDTO.setPhone(phone);
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

                    editUserDTO.setPhoto(sImage);

                }
            }
        }
    }
}
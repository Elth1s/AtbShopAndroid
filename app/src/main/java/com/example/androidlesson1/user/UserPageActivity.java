package com.example.androidlesson1.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.androidlesson1.R;
import com.example.androidlesson1.account.dto.AccountResponseDTO;
import com.example.androidlesson1.application.HomeApplication;
import com.example.androidlesson1.constants.Urls;
import com.example.androidlesson1.user.card.CardAdapter;
import com.example.androidlesson1.user.dto.UserDTO;
import com.example.androidlesson1.user.network.UserService;
import com.example.androidlesson1.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPageActivity extends AppCompatActivity {

    TextInputEditText firstNameEditText;
    TextInputEditText secondNameEditText;
    TextInputEditText emailEditText;
    TextInputEditText phoneEditText;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        firstNameEditText = findViewById(R.id.firstNameTextInput);
        secondNameEditText = findViewById(R.id.secondNameTextInput);
        emailEditText = findViewById(R.id.emailTextInput);
        phoneEditText = findViewById(R.id.phoneTextInput);
        imageView = findViewById(R.id.imageView);

        Bundle b = getIntent().getExtras();
        int value =0;
        if(b !=null)
            value=b.getInt("id");
        Integer userId = new Integer(value);

        CommonUtils.showLoading(this);
        UserService.getInstance()
                .jsonApi()
                .getUser(userId)
                .enqueue(new Callback<UserDTO>() {
                    @Override
                    public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                        UserDTO data = response.body();

                        firstNameEditText.setText(data.getFirstName());
                        secondNameEditText.setText(data.getSecondName());
                        emailEditText.setText(data.getEmail());
                        phoneEditText.setText(data.getPhone());

                        String url = Urls.BASE+data.getPhoto();
                        Glide.with(HomeApplication.getAppContext())
                                .load(url)
                                .apply(new RequestOptions().override(400, 400))
                                .into(imageView);


                        CommonUtils.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<UserDTO> call, Throwable t) {
                        String str = t.toString();
                        int a = 12;
                    }
                });
    }
}
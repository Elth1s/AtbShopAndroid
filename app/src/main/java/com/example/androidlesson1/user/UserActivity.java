package com.example.androidlesson1.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;

import com.example.androidlesson1.BaseActivity;
import com.example.androidlesson1.MainActivity;
import com.example.androidlesson1.R;
import com.example.androidlesson1.account.LoginActivity;
import com.example.androidlesson1.application.HomeApplication;
import com.example.androidlesson1.security.JwtSecurityService;
import com.example.androidlesson1.user.dto.UserDTO;
import com.example.androidlesson1.user.network.UserService;
import com.example.androidlesson1.utils.CommonUtils;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    JwtSecurityService jwtService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jwtService = (JwtSecurityService) HomeApplication.getInstance();
        String token = jwtService.getToken();
        if(token.isEmpty())
        {
            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else {

            setContentView(R.layout.activity_user);

            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2,
                    LinearLayoutManager.VERTICAL, false));
            CommonUtils.showLoading(this);
            UserService.getInstance()
                    .jsonApi()
                    .users()
                    .enqueue(new Callback<List<UserDTO>>() {
                        @Override
                        public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                            RecyclerView.Adapter mAdapter;
                            mAdapter = new CardAdapter(response.body());
                            mRecyclerView.setAdapter(mAdapter);
                            CommonUtils.hideLoading();
                        }

                        @Override
                        public void onFailure(Call<List<UserDTO>> call, Throwable t) {
                            String str = t.toString();
                            int a = 12;
                        }
                    });


        }
    }
}
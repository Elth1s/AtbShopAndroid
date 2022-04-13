package com.example.androidlesson1;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidlesson1.account.LoginActivity;
import com.example.androidlesson1.account.RegisterActivity;
import com.example.androidlesson1.application.HomeApplication;
import com.example.androidlesson1.security.JwtSecurityService;
import com.example.androidlesson1.user.UserActivity;

public class BaseActivity extends AppCompatActivity {

    // Activity code here

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch (item.getItemId()){
            case R.id.m_login:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.m_register:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                return true;
            case R.id.m_users:
                intent = new Intent(this, UserActivity.class);
                startActivity(intent);
                return true;
            case R.id.m_logout:
                JwtSecurityService jwtService = (JwtSecurityService) HomeApplication.getInstance();
                String token = jwtService.getToken();
                if(token!=null && !token.isEmpty())
                {
                    jwtService.deleteToken();
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

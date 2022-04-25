package com.example.androidlesson1.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.androidlesson1.R;
import com.example.androidlesson1.application.HomeApplication;

public class UserPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        Bundle b = getIntent().getExtras();
        int value =0;
        if(b !=null)
            value=b.getInt("id");
        Integer y = new Integer(value);
        Toast.makeText(HomeApplication.getAppContext(), y.toString(), Toast.LENGTH_SHORT).show();
    }
}
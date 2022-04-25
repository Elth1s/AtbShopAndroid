package com.example.androidlesson1;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.androidlesson1.account.LoginActivity;
import com.example.androidlesson1.account.RegisterActivity;
import com.example.androidlesson1.application.HomeApplication;
import com.example.androidlesson1.security.JwtSecurityService;
import com.example.androidlesson1.user.UserActivity;
import com.example.androidlesson1.utils.ConnectionInternetError;
import com.example.androidlesson1.utils.NavigationHost;

public class BaseActivity extends AppCompatActivity implements NavigationHost, ConnectionInternetError {

    protected Fragment currentFragment;
    private Fragment callbackFragment;

    JwtSecurityService jwtService;
    // Activity code here

    public BaseActivity() {
        HomeApplication homeApp = (HomeApplication)HomeApplication.getAppContext();
        homeApp.setCurrentActivity(this);
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        this.currentFragment = fragment;
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem logoutItem = menu.findItem(R.id.m_logout);
        MenuItem registerItem = menu.findItem(R.id.m_register);
        MenuItem loginItem = menu.findItem(R.id.m_login);

        jwtService = (JwtSecurityService) HomeApplication.getInstance();
        String token = jwtService.getToken();
        if(token.isEmpty())
        {
            loginItem.setVisible(true);
            registerItem.setVisible(true);
            logoutItem.setVisible(false);
        }
        else
        {
            loginItem.setVisible(false);
            registerItem.setVisible(false);
            logoutItem.setVisible(true);
        }

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

    @Override
    public void navigateErrorPage() {
        this.callbackFragment=currentFragment;
        this.navigateTo(new ConnectionInternetErrorFragment(), true);
    }

    @Override
    public void refreshLastPage() {
        this.navigateTo(this.callbackFragment, true);
    }
}

package com.example.androidlesson1.interceptor;

import android.content.Context;

import com.example.androidlesson1.application.HomeApplication;
import com.example.androidlesson1.utils.CommonUtils;
import com.example.androidlesson1.utils.ConnectionInternetError;
import com.example.androidlesson1.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Context context= HomeApplication.getAppContext();
        Request originalRequest = chain.request();

        if (!NetworkUtils.isOnline(context)) {
            HomeApplication beginApplication = (HomeApplication) context;
            CommonUtils.hideLoading();
            ((ConnectionInternetError) beginApplication.getCurrentActivity()).navigateErrorPage();
        }
        Request newRequest = originalRequest.newBuilder()
                .build();
        return chain.proceed(newRequest);
    }
}

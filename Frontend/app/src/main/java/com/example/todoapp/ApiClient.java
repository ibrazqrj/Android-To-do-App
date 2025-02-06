package com.example.todoapp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;
    public static Retrofit getClient(final String accessToken) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request.Builder builder = originalRequest.newBuilder()
                                .header("Authorization", "Bearer " + accessToken);
                        Request newRequest = builder.build();
                        Log.d("ApiClient", "Access Token: " + accessToken);
                        return chain.proceed(newRequest);
                    }
                })
                .build();


        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5085")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }
}

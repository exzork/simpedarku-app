package com.exzork.simpedarku.rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.exzork.simpedarku.activity.MainActivity;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiClient {
    public static final String BASE_URL = "https://simpedarku-dev.exzork.me/api/";
    public static Retrofit retrofit = null;
    public static String bearerToken = null;
    protected static List<Cookie> cookies = new ArrayList<>();
    private static final String[] arr_save_cookie = { "/sanctum/csrf-cookie", "/login", "/register"};


    public static Retrofit getClient() {
        if (retrofit == null) {
            SessionCookieJar cookieJar = new SessionCookieJar();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.cookieJar(cookieJar);
            httpClient.addInterceptor(chain -> {
                Request.Builder requestBuilder = chain.request().newBuilder();
                requestBuilder.addHeader("Accept", "application/json");
                if(Arrays.asList(arr_save_cookie).contains(chain.request().url().encodedPath())){
                    requestBuilder.addHeader("X-XSRF-TOKEN", cookieJar.getCsrf());
                }
                if(bearerToken != null){
                    requestBuilder.addHeader("Authorization", "Bearer " + bearerToken);
                }
                return chain.proceed(requestBuilder.build());
            }).addInterceptor(loggingInterceptor);

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void clearCookie(){
        cookies = new ArrayList<>();
    }

    private static class SessionCookieJar implements CookieJar {


        @Override
        public void saveFromResponse(@NotNull HttpUrl url, @NotNull List<Cookie> cookies) {
            if(Arrays.asList(arr_save_cookie).contains(url.encodedPath())){
                ApiClient.cookies = cookies;
            }
        }


        @Override
        public List<Cookie> loadForRequest(@NotNull HttpUrl url) {
            return cookies;
        }

        public String getCsrf() throws UnsupportedEncodingException {
            for(Cookie cookie : cookies){
                if(cookie.name().equals("XSRF-TOKEN")){
                    return URLDecoder.decode(cookie.value(), "UTF-8");
                }
            }
            return "";
        }
    }
}

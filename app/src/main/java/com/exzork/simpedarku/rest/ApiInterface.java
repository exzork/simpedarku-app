package com.exzork.simpedarku.rest;

import com.exzork.simpedarku.model.ApiResponse;
import com.exzork.simpedarku.model.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiInterface {
    @GET("/sanctum/csrf-cookie")
    Call<ApiResponse> getCsrfToken();

    @POST("/register")
    Call<ApiResponse> register(@Body User user);

    @FormUrlEncoded
    @POST("/login")
    Call<ApiResponse> login(@Field("email") String email, @Field("password") String password);

    @POST("user/logout")
    Call<ApiResponse> logout();

    @GET("user/profile")
    Call<ApiResponse> getUserProfile();

    @PUT("user/profile")
    Call<ApiResponse> updateUserProfile(@Body User user);

    @GET("reports")
    Call<ApiResponse> getReports();

    @Multipart
    @POST("reports")
    Call<ApiResponse> sendReport(@Part("type") RequestBody type, @Part("location") RequestBody location, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part MultipartBody.Part image);

    @Multipart
    @POST("reports")
    Call<ApiResponse> sendReport(@Part("type") RequestBody type, @Part("location") RequestBody location, @Part("title") RequestBody title, @Part("description") RequestBody description);
}

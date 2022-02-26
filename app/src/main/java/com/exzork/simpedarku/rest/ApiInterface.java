package com.exzork.simpedarku.rest;

import com.exzork.simpedarku.model.ApiResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiInterface {
    @GET("/sanctum/csrf-cookie")
    Call<ApiResponse> getCsrfToken();

    @FormUrlEncoded()
    @POST("/register")
    Call<ApiResponse> register(@Field("name") String name, @Field("gender") String gender, @Field("nik") String nik, @Field("address") String address, @Field("blood_type") String blood_type, @Field("emergency_contact") String emergency_contact, @Field("email") String email, @Field("phone") String phone, @Field("password") String password, @Field("password_confirmation") String password_confirmation);

    @FormUrlEncoded
    @POST("/login")
    Call<ApiResponse> login(@Field("email") String email, @Field("password") String password);

    @POST("user/logout")
    Call<ApiResponse> logout();

    @GET("user/profile")
    Call<ApiResponse> getUserProfile();

    @GET("reports")
    Call<ApiResponse> getReports();
}

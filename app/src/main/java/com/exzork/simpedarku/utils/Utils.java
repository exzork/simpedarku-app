package com.exzork.simpedarku.utils;

import android.content.res.Resources;
import com.exzork.simpedarku.model.ApiResponse;
import com.exzork.simpedarku.rest.ApiClient;
import com.exzork.simpedarku.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils {
    public static int getDP(int px){
        return (int) (px * (Resources.getSystem().getDisplayMetrics().density));
    }
    private static final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    public static boolean checkToken(String token){
        ApiClient.bearerToken = token;
        Call<ApiResponse> check = apiService.getUserProfile();
        final int[] statusCode = {0};
        check.enqueue(new Callback<ApiResponse>(){

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                statusCode[0] = response.code();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                statusCode[0] = 0;
            }
        });
        return statusCode[0] == 200;
    }

}

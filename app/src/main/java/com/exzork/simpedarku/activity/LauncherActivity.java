package com.exzork.simpedarku.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.exzork.simpedarku.model.ApiResponse;
import com.exzork.simpedarku.rest.ApiClient;
import com.exzork.simpedarku.rest.ApiInterface;
import com.exzork.simpedarku.rest.CallbackWithRetry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LauncherActivity extends AppCompatActivity {
    private final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {this.getSupportActionBar().hide();} //hide supportactionbar
        catch (NullPointerException e){}


        Intent mainIntent = new Intent(LauncherActivity.this, MainActivity.class);
        Intent loginIntent = new Intent(LauncherActivity.this, LoginActivity.class);

        SharedPreferences sharedPreferences = getSharedPreferences("user-simpedarku", MODE_PRIVATE);


        if (sharedPreferences.getString("apiToken", null) != null) {
            ApiClient.bearerToken = sharedPreferences.getString("apiToken", null);
            Call<ApiResponse> checkToken = apiService.getUserProfile();
            checkToken.enqueue(new CallbackWithRetry<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.code() == 200) {
                        startActivity(mainIntent);
                        finish();
                    }else{
                        startActivity(loginIntent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    if (this.getRetryCount() == this.getTotalRetries()) {
                        Toast.makeText(LauncherActivity.this, "Error : Retry limit reached", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(LauncherActivity.this, "Error : Retrying... (" + (this.getRetryCount()+1) + " out of " + this.getTotalRetries() + ")", Toast.LENGTH_SHORT).show();
                    }
                    super.onFailure(call, t);
                }
            });
        }else{
            startActivity(loginIntent);
            finish();
        }
    }
}
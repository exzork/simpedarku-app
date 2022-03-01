package com.exzork.simpedarku.activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.exzork.simpedarku.R;
import com.exzork.simpedarku.model.ApiResponse;
import com.exzork.simpedarku.model.ErrorResponse;
import com.exzork.simpedarku.model.User;
import com.exzork.simpedarku.rest.ApiClient;
import com.exzork.simpedarku.rest.ApiInterface;
import com.exzork.simpedarku.rest.CallbackWithRetry;
import com.exzork.simpedarku.utils.ErrorParser;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class LoginActivity extends AppCompatActivity {
    private final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {this.getSupportActionBar().hide();} //hide supportactionbar
        catch (NullPointerException e){}

        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);

        SharedPreferences sharedPreferences = getSharedPreferences("user-simpedarku", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login_btn = findViewById(R.id.login_btn);
        Button register_btn = findViewById(R.id.register_btn);
        EditText emailFieldLogin = findViewById(R.id.emailFieldLogin);
        EditText passwordFieldLogin = findViewById(R.id.passwordFieldLogin);


        login_btn.setOnClickListener(view -> {

            Call<ApiResponse> csrf = apiService.getCsrfToken();
            csrf.enqueue(new CallbackWithRetry<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {

                        Call<ApiResponse> login = apiService.login(emailFieldLogin.getText().toString(), passwordFieldLogin.getText().toString());

                        login.enqueue(new Callback<ApiResponse>() {

                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.isSuccessful()) {
                                    ApiResponse apiResponse = response.body();
                                    if (apiResponse.getStatus().equals("success")) {
                                        User user = apiResponse.getData().getUser();
                                        if(user.getApiToken() != null) {
                                            Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                                            editor.putString("apiToken", user.getApiToken()).apply();
                                            ApiClient.clearCookie();
                                            startActivity(mainIntent);
                                            finish();
                                        }else{
                                            ApiClient.clearCookie();
                                        }
                                    }
                                }else{
                                    ErrorResponse errorResponse = ErrorParser.errorResponse(response);
                                    if(errorResponse.getData() != null) {
                                        Toast.makeText(LoginActivity.this, errorResponse.getData().getErrorString(), Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(LoginActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.d("LoginActivity", "onFailure: " + t.getMessage());
                            }
                        });
                    }else{
                        ErrorResponse errorResponse = ErrorParser.errorResponse(response);
                        if(errorResponse.getData() != null) {
                            Toast.makeText(LoginActivity.this, errorResponse.getData().getErrorString(), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    if (this.getRetryCount() == this.getTotalRetries()) {
                        Toast.makeText(LoginActivity.this, "Error : Retry limit reached", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(LoginActivity.this, "Error : Retrying... (" + (this.getRetryCount()+1) + " out of " + this.getTotalRetries() + ")", Toast.LENGTH_SHORT).show();
                    }
                    super.onFailure(call, t);
                }
            });

        });
        register_btn.setOnClickListener(view -> {
            startActivity(registerIntent);
            finish();
        });
    }
}
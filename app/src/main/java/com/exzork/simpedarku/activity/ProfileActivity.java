package com.exzork.simpedarku.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.annotation.NonNull;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.material.internal.ViewUtils.dpToPx;

public class ProfileActivity extends AppCompatActivity {
    private final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private User user;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sharedPreferences = getSharedPreferences("user-simpedarku", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        ApiClient.bearerToken = sharedPreferences.getString("apiToken", null);

        EditText profile_name = findViewById(R.id.profile_name_field);
        EditText profile_nik = findViewById(R.id.profile_nik_field);
        EditText profile_gender = findViewById(R.id.profile_gender_field);
        EditText profile_blood_type = findViewById(R.id.profile_blood_type_field);

        EditText profile_address = findViewById(R.id.profile_address_field);
        EditText profile_email = findViewById(R.id.profile_email_field);
        EditText profile_phone = findViewById(R.id.profile_phone_field);
        EditText profile_emergency_contact = findViewById(R.id.profile_emergency_contact_field);

        EditText profile_password = findViewById(R.id.profile_password_field);
        EditText profile_confirm_password = findViewById(R.id.profile_confirm_password_field);

        EditText profile_current_password = findViewById(R.id.profile_current_password_field);

        Call<ApiResponse> loadProfile = apiService.getUserProfile();
        loadProfile.enqueue(new CallbackWithRetry<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        setUser(apiResponse, profile_name, profile_nik, profile_gender, profile_blood_type, profile_address, profile_email, profile_phone, profile_emergency_contact);
                    } else {
                        ErrorResponse errorResponse = ErrorParser.errorResponse(response);
                        if(errorResponse.getData() != null) {
                            Toast.makeText(ProfileActivity.this, errorResponse.getData().getErrorString(), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ProfileActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    ErrorResponse errorResponse = ErrorParser.errorResponse(response);
                    if (errorResponse.getData() != null) {
                        Toast.makeText(ProfileActivity.this, errorResponse.getData().getErrorString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (this.getRetryCount() == this.getTotalRetries()) {
                    Toast.makeText(ProfileActivity.this, "Error : Retry limit reached", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, "Error : Retrying... (" + (this.getRetryCount() + 1) + " out of " + this.getTotalRetries() + ")", Toast.LENGTH_SHORT).show();
                }
                super.onFailure(call, t);
            }
        });

        Button update_profile = findViewById(R.id.update_profile_btn);
        update_profile.setOnClickListener(view -> {
            user.setAddress(profile_address.getText().toString());
            user.setEmail(profile_email.getText().toString());
            user.setPhone(profile_phone.getText().toString());
            user.setEmergencyContact(profile_emergency_contact.getText().toString());
            user.setCurrentPassword(profile_current_password.getText().toString());
            if (profile_password.getText().toString().equals(profile_confirm_password.getText().toString())) {
                user.setPassword(profile_password.getText().toString());
                user.setPasswordConfirmation(profile_confirm_password.getText().toString());
            }
            update_profile.setText("Sedang memperbarui...");
            update_profile.setEnabled(false);
            Call<ApiResponse> updateProfile = apiService.updateUserProfile(user);
            updateProfile.enqueue(new CallbackWithRetry<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse != null) {
                            setUser(apiResponse, profile_name, profile_nik, profile_gender, profile_blood_type, profile_address, profile_email, profile_phone, profile_emergency_contact);
                            update_profile.setEnabled(true);
                            update_profile.setText(R.string.profile_header_update);
                            Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ErrorResponse errorResponse = ErrorParser.errorResponse(response);
                        if (errorResponse.getData() != null) {
                            Toast.makeText(ProfileActivity.this, errorResponse.getData().getErrorString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    if (this.getRetryCount() == this.getTotalRetries()) {
                        Toast.makeText(ProfileActivity.this, "Error : Retry limit reached", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Error : Retrying... (" + (this.getRetryCount() + 1) + " out of " + this.getTotalRetries() + ")", Toast.LENGTH_SHORT).show();
                    }
                    super.onFailure(call, t);
                }
            });
        });
    }

    private void setUser(ApiResponse apiResponse, EditText profile_name, EditText profile_nik, EditText profile_gender, EditText profile_blood_type, EditText profile_address, EditText profile_email, EditText profile_phone, EditText profile_emergency_contact) {
        if (apiResponse.getData().getUser() != null) {
            user = apiResponse.getData().getUser();
            profile_name.setText(user.getName());
            profile_nik.setText(user.getNik());
            profile_gender.setText(user.getGender());
            profile_blood_type.setText(user.getBloodType());

            profile_address.setText(user.getAddress());
            profile_email.setText(user.getEmail());
            profile_phone.setText(user.getPhone());
            profile_emergency_contact.setText(user.getEmergencyContact());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
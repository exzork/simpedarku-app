package com.exzork.simpedarku.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.exzork.simpedarku.R;
import com.exzork.simpedarku.model.ApiResponse;
import com.exzork.simpedarku.model.ErrorResponse;
import com.exzork.simpedarku.model.Report;
import com.exzork.simpedarku.rest.ApiClient;
import com.exzork.simpedarku.rest.ApiInterface;
import com.exzork.simpedarku.rest.CallbackWithRetry;
import com.exzork.simpedarku.utils.ErrorParser;
import com.exzork.simpedarku.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("user-simpedarku", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        ApiClient.bearerToken = sharedPreferences.getString("apiToken", null);

        TableLayout my_report_table = findViewById(R.id.my_report_table);

        int _10dp = Utils.getDP(10);

        Call<ApiResponse> getReports = apiService.getReports();
        getReports.enqueue(new CallbackWithRetry<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful()){
                    ApiResponse apiResponse = response.body();
                    Log.d("apiResponse", apiResponse.getData().getReports().toString());
                    List<Report> reports = apiResponse.getData().getReports();
                    int i = 0;
                    if(reports.size() > 0) {
                        for (Report report : reports) {
                            TableRow row = new TableRow(MainActivity.this);
                            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                            TextView title = new TextView(MainActivity.this);
                            title.setText(report.getTitle());
                            title.setPadding(_10dp, _10dp, _10dp, _10dp);
                            title.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 2f));

                            TextView status = new TextView(MainActivity.this);
                            status.setText(report.getStatus());
                            status.setPadding(_10dp, _10dp, _10dp, _10dp);
                            status.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                            status.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

                            row.addView(title);
                            row.addView(status);

                            my_report_table.addView(row, i);
                            i++;
                        }
                    }else{
                        TableRow row = new TableRow(MainActivity.this);
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                        TextView title = new TextView(MainActivity.this);
                        title.setText("Tidak ada data laporan anda");
                        title.setPadding(_10dp, _10dp, _10dp, _10dp);
                        title.setTextSize(18);
                        title.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
                        title.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

                        row.addView(title);

                        my_report_table.addView(row,0);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (this.getRetryCount() == this.getTotalRetries()) {
                    Toast.makeText(MainActivity.this, "Error : Retry limit reached", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Error : Retrying... (" + (this.getRetryCount()+1) + " out of " + this.getTotalRetries() + ")", Toast.LENGTH_SHORT).show();
                }
                super.onFailure(call, t);
            }
        });


        Intent reportIntent = new Intent(MainActivity.this, ReportActivity.class);
        Bundle reportBundle = new Bundle();

        Button report_police_btn = findViewById(R.id.report_police_btn);
        report_police_btn.setOnClickListener(view-> {
            reportBundle.putString("report_type", "police");
            reportIntent.putExtras(reportBundle);
            startActivity(reportIntent);
        });

        Button report_fire_btn = findViewById(R.id.report_firefighter_btn);
        report_fire_btn.setOnClickListener(view-> {
            reportBundle.putString("report_type", "firefighter");
            reportIntent.putExtras(reportBundle);
            startActivity(reportIntent);
        });

        Button report_hospital_btn = findViewById(R.id.report_hospital_btn);
        report_hospital_btn.setOnClickListener(view-> {
            reportBundle.putString("report_type", "hospital");
            reportIntent.putExtras(reportBundle);
            startActivity(reportIntent);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.logout_menu) {
            Call<ApiResponse> logout = apiService.logout();
            logout.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        editor.remove("apiToken").apply();
                    }
                }
                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                }
            });
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }else if (item.getItemId() == R.id.profile_menu) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
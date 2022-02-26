package com.exzork.simpedarku.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.exzork.simpedarku.R;
import com.exzork.simpedarku.model.ApiResponse;
import com.exzork.simpedarku.model.ErrorResponse;
import com.exzork.simpedarku.model.Report;
import com.exzork.simpedarku.rest.ApiClient;
import com.exzork.simpedarku.rest.ApiInterface;
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
        getReports.enqueue(new Callback<ApiResponse>() {

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

            }
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
        switch (item.getItemId()) {
            case R.id.notification_menu:
                return true;
            case R.id.profile_menu:
                return true;
            case R.id.logout_menu:
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
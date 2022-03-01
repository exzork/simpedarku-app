package com.exzork.simpedarku.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ScrollView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.exzork.simpedarku.R;
import com.exzork.simpedarku.fragment.ReportFragment;
import com.exzork.simpedarku.rest.ApiClient;
import com.exzork.simpedarku.rest.ApiInterface;

import static com.google.android.material.internal.ViewUtils.dpToPx;

public class ReportActivity extends AppCompatActivity {
    public static final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    public static ActivityResultLauncher<Intent> cameraActivityLauncher;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = getIntent().getExtras();
        String report_type = bundle.getString("report_type");

        final View view = findViewById(R.id.linear_layout);

        ScrollView scrollView = findViewById(R.id.scroll_view);

        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int heightDiff = view.getRootView().getHeight() - view.getHeight();
            if (heightDiff > dpToPx(this, 200)) {
                scrollView.post(() -> {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                });
            }
        });

        cameraActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                    }
                });

        replaceFragment(this, R.id.report_frame, ReportFragment.newInstance(report_type));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void replaceFragment(FragmentActivity activity, int frameId,
                                       Fragment fragment) {
        try {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(frameId, fragment).commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
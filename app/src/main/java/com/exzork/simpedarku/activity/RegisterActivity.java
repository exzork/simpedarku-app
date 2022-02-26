package com.exzork.simpedarku.activity;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.exzork.simpedarku.R;
import com.exzork.simpedarku.fragment.register_1;
import com.exzork.simpedarku.rest.ApiClient;
import com.exzork.simpedarku.rest.ApiInterface;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {this.getSupportActionBar().hide();} //hide supportactionbar
        catch (NullPointerException e){}

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Fragment register_1_fragment = register_1.newInstance();
        replaceFragment(this, R.id.register_frame, register_1_fragment);
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
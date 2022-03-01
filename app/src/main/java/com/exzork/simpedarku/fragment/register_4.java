package com.exzork.simpedarku.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.exzork.simpedarku.R;
import com.exzork.simpedarku.activity.LoginActivity;
import com.exzork.simpedarku.activity.MainActivity;
import com.exzork.simpedarku.activity.RegisterActivity;
import com.exzork.simpedarku.model.ApiResponse;
import com.exzork.simpedarku.model.ErrorResponse;
import com.exzork.simpedarku.model.User;
import com.exzork.simpedarku.rest.ApiClient;
import com.exzork.simpedarku.rest.ApiInterface;
import com.exzork.simpedarku.utils.ErrorParser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link register_4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class register_4 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    public register_4() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment register_4.
     */
    // TODO: Rename and change types and number of parameters
    public static register_4 newInstance() {
        register_4 fragment = new register_4();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register_4, container, false);
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user-simpedarku", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        EditText password_field = rootView.findViewById(R.id.password_field);
        EditText password_confirm_field = rootView.findViewById(R.id.password_confirm_field);

        Button send_data_register = rootView.findViewById(R.id.send_data_register);
        send_data_register.setOnClickListener(view -> {

            String name = sharedPreferences.getString("name_register", "");
            String gender = sharedPreferences.getString("gender_register", "");
            String nik = sharedPreferences.getString("nik_register", "");
            String address = sharedPreferences.getString("address_register", "");
            String blood_type = sharedPreferences.getString("bloodtype_register", "");
            String emergency_contact = sharedPreferences.getString("emergency_contact_register", "");
            String email = sharedPreferences.getString("email_register", "");
            String phone = sharedPreferences.getString("phone_register", "");
            String password = password_field.getText().toString();
            String password_confirm = password_confirm_field.getText().toString();

            if (password_confirm.equals(password)) {

                RegisterActivity.user.setPassword(password);
                RegisterActivity.user.setPasswordConfirmation(password_confirm);

                Call<ApiResponse> csrf = apiService.getCsrfToken();
                csrf.enqueue(new Callback<ApiResponse>() {

                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()){
                            Call<ApiResponse> register = apiService.register(RegisterActivity.user);
                            register.enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                    if (response.isSuccessful()) {
                                        ApiResponse apiResponse = response.body();
                                        if (apiResponse.getStatus().equals("success")) {
                                            User user = apiResponse.getData().getUser();
                                            if (user.getApiToken() != null) {
                                                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                                Toast.makeText(getActivity(), "Logged In", Toast.LENGTH_SHORT).show();
                                                editor.putString("apiToken", user.getApiToken()).apply();
                                                ApiClient.clearCookie();
                                                startActivity(mainIntent);
                                                getActivity().finish();
                                            } else {
                                                ApiClient.clearCookie();
                                            }
                                        }
                                    } else {
                                        ErrorResponse errorResponse = ErrorParser.errorResponse(response);
                                        if (errorResponse.getData() != null) {
                                            Toast.makeText(getActivity(), errorResponse.getData().getErrorString(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable t) {

                                }
                            });
                        }else{
                            Toast.makeText(getActivity(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {

                    }
                });


            } else {
                password_field.setError("Password tidak sama");
            }

        });

        Button back_to_step_3 = rootView.findViewById(R.id.back_to_step_3);
        back_to_step_3.setOnClickListener(view -> RegisterActivity.replaceFragment(getActivity(),R.id.register_frame, register_3.newInstance()));

        return rootView;
    }
}
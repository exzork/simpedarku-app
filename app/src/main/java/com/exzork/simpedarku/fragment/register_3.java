package com.exzork.simpedarku.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.exzork.simpedarku.R;
import com.exzork.simpedarku.activity.MainActivity;
import com.exzork.simpedarku.activity.RegisterActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link register_3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class register_3 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    public register_3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment register_3.
     */
    // TODO: Rename and change types and number of parameters
    public static register_3 newInstance() {
        return new register_3();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user-simpedarku", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_register_3, container, false);

        EditText email_field = rootView.findViewById(R.id.email_field);
        EditText phone_field = rootView.findViewById(R.id.phone_field);

        //initialize value from sharedPreferences
        if(sharedPreferences.getString("email_register", null) != null){
            email_field.setText(sharedPreferences.getString("email_register", null));
        }
        if(sharedPreferences.getString("phone_register", null) != null){
            phone_field.setText(sharedPreferences.getString("phone_register", null));
        }

        Button next_step_3 = rootView.findViewById(R.id.next_step_3);
        next_step_3.setOnClickListener(view -> {
            editor.putString("email_register", email_field.getText().toString());
            editor.putString("phone_register", phone_field.getText().toString());
            editor.apply();

            RegisterActivity.replaceFragment(getActivity(), R.id.register_frame, register_4.newInstance());
        });

        Button back_to_step_2 = rootView.findViewById(R.id.back_to_step_2);
        back_to_step_2.setOnClickListener(view -> RegisterActivity.replaceFragment(getActivity(), R.id.register_frame, register_2.newInstance()));

        return rootView;
    }
}
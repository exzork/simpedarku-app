package com.exzork.simpedarku.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.exzork.simpedarku.R;
import com.exzork.simpedarku.activity.LoginActivity;
import com.exzork.simpedarku.activity.RegisterActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link register_1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class register_1 extends Fragment {


    public register_1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment register_1.
     */
    // TODO: Rename and change types and number of parameters
    public static register_1 newInstance() {
        return new register_1();
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
        View rootView = inflater.inflate(R.layout.fragment_register_1, container, false);

        //form
        EditText name_field = rootView.findViewById(R.id.name_field);
        Spinner gender_field = rootView.findViewById(R.id.gender_field);
        EditText nik_field = rootView.findViewById(R.id.nik_field);


        //initialize value from sharedPreferences
        if (sharedPreferences.getString("name_register",null) != null){
            name_field.setText(sharedPreferences.getString("name_register",null));
        }
        if (sharedPreferences.getString("gender_register",null) != null){
            gender_field.setSelection(((ArrayAdapter<String>)gender_field.getAdapter()).getPosition(sharedPreferences.getString("gender_register",null)));
        }
        if (sharedPreferences.getString("nik_register",null)!=null){
            nik_field.setText(sharedPreferences.getString("nik_register",null));
        }

        Button next_step_1 = rootView.findViewById(R.id.next_step_1);
        next_step_1.setOnClickListener(view -> {

            editor.putString("name_register", name_field.getText().toString());
            editor.putString("gender_register", gender_field.getSelectedItem().toString());
            editor.putString("nik_register",nik_field.getText().toString());
            editor.apply();

            RegisterActivity.replaceFragment(getActivity(), R.id.register_frame ,register_2.newInstance());
        });

        Button back_to_login = rootView.findViewById(R.id.back_to_login);
        back_to_login.setOnClickListener(view -> {
            Intent login_intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(login_intent);
            getActivity().finish();
        });

        return  rootView;
    }
}
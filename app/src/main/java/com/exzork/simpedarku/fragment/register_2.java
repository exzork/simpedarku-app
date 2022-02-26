package com.exzork.simpedarku.fragment;

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
import com.exzork.simpedarku.activity.RegisterActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link register_2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class register_2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    public register_2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment register_2.
     */
    // TODO: Rename and change types and number of parameters
    public static register_2 newInstance() {
        register_2 fragment = new register_2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        View rootView = inflater.inflate(R.layout.fragment_register_2, container, false);

        //form
        EditText address_field = rootView.findViewById(R.id.address_field);
        Spinner bloodtype_field = rootView.findViewById(R.id.bloodtype_field);
        EditText emergency_contact_field = rootView.findViewById(R.id.emergency_contact_field);

        //initialize value from sharedPreferences
        if(sharedPreferences.getString("address_register", null) != null){
            address_field.setText(sharedPreferences.getString("address_register", null));
        }
        if(sharedPreferences.getString("bloodtype_register", null) != null){
            bloodtype_field.setSelection(((ArrayAdapter<String>)bloodtype_field.getAdapter()).getPosition(sharedPreferences.getString("bloodtype_register", null)));
        }
        if(sharedPreferences.getString("emergency_contact_register", null) != null){
            emergency_contact_field.setText(sharedPreferences.getString("emergency_contact_register", null));
        }

        Button next_step_2 = rootView.findViewById(R.id.next_step_2);
        next_step_2.setOnClickListener( view -> {
            editor.putString("address_register", address_field.getText().toString());
            editor.putString("bloodtype_register", bloodtype_field.getSelectedItem().toString());
            editor.putString("emergency_contact_register", emergency_contact_field.getText().toString());
            editor.apply();

            RegisterActivity.replaceFragment(getActivity(),R.id.register_frame,register_3.newInstance());
        });

        Button back_to_step_1 = rootView.findViewById(R.id.back_to_step_1);
        back_to_step_1.setOnClickListener( view -> RegisterActivity.replaceFragment(getActivity(),R.id.register_frame,register_1.newInstance()));

        return rootView;
    }
}
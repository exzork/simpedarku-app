package com.exzork.simpedarku.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.*;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.exzork.simpedarku.R;
import com.exzork.simpedarku.activity.LauncherActivity;
import com.exzork.simpedarku.activity.LoginActivity;
import com.exzork.simpedarku.activity.ReportActivity;
import com.exzork.simpedarku.model.ApiResponse;
import com.exzork.simpedarku.model.ErrorResponse;
import com.exzork.simpedarku.model.Type;
import com.exzork.simpedarku.rest.CallbackWithRetry;
import com.exzork.simpedarku.utils.ErrorParser;
import com.google.common.util.concurrent.ListenableFuture;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TYPE = "";
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture ;
    // TODO: Rename and change types of parameters
    private String type;
    private String photoPath= "";

    public ReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param type report type.
     * @return A new instance of fragment ReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String type) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, 1);
        cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());
        type = getArguments().getString(TYPE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);

        Button take_image_btn = rootView.findViewById(R.id.take_image_btn);
        Button submit_report = rootView.findViewById(R.id.submit_report);

        PackageManager pm = getActivity().getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            take_image_btn.setVisibility(View.GONE);
        }

        ImageView icon_report = rootView.findViewById(R.id.icon_report);
        TextView type_report = rootView.findViewById(R.id.type_report);
        switch (type) {
            case "police":
                icon_report.setImageResource(R.drawable.ic_police_foreground);
                type_report.setText(R.string.police);
                break;
            case "hospital":
                icon_report.setImageResource(R.drawable.ic_doctor_foreground);
                type_report.setText(R.string.hospital);
                break;
            case "firefighter":
                icon_report.setImageResource(R.drawable.ic_fireman_foreground);
                type_report.setText(R.string.fire_station);
        }

        String type_field = "";
        switch (this.type) {
            case "police":
                type_field= "POLISI";
                break;
            case "hospital":
                type_field="RUMAH SAKIT";
                break;
            case "firefighter":
                type_field="PEMADAM KEBAKARAN";
                break;
        }
        EditText location_field = rootView.findViewById(R.id.location_field);
        EditText title_field = rootView.findViewById(R.id.title_field);
        EditText description_field = rootView.findViewById(R.id.description_field);

        take_image_btn.setOnClickListener(view -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (photoFile!=null){
                    Uri photoURI = FileProvider.getUriForFile(getActivity(),"com.exzork.simpedarku", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    ReportActivity.cameraActivityLauncher.launch(takePictureIntent);
                }
            }
        });

        String finalType_field = type_field;
        submit_report.setOnClickListener(view -> {
            RequestBody type = RequestBody.create(finalType_field, MediaType.parse("text/plain"));
            RequestBody location = RequestBody.create(location_field.getText().toString(), MediaType.parse("text/plain"));
            RequestBody title = RequestBody.create(title_field.getText().toString(), MediaType.parse("text/plain"));
            RequestBody description = RequestBody.create(description_field.getText().toString(), MediaType.parse("text/plain"));

            File image_file = new File(photoPath);
            RequestBody rb_image_file = RequestBody.create(image_file, MediaType.parse("image/jpeg"));
            MultipartBody.Part image = MultipartBody.Part.createFormData("image", "temp.jpg", rb_image_file);

            Call<ApiResponse> sendReport = ReportActivity.apiService.sendReport(type, location, title, description, image);
            if (!image_file.exists()) {
                sendReport = ReportActivity.apiService.sendReport(type, location, title, description);
            }

            submit_report.setEnabled(false);
            submit_report.setText(R.string.sending_report);

            sendReport.enqueue(new CallbackWithRetry<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), "Report berhasil dikirim", Toast.LENGTH_SHORT).show();
                    } else {
                        ErrorResponse errorResponse = ErrorParser.errorResponse(response);
                        if (errorResponse.getData() != null) {
                            Toast.makeText(getActivity(), errorResponse.getData().getErrorString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        if (errorResponse.getMessage().equals("Unauthenticated.")) {
                            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                            getActivity().startActivity(loginIntent);
                        }
                    }
                    submit_report.setEnabled(true);
                    submit_report.setText(R.string.action_report);
                    getActivity().finish();
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    if (this.getRetryCount() == this.getTotalRetries()) {
                        Toast.makeText(getActivity(), "Error : Retry limit reached", Toast.LENGTH_LONG).show();
                        submit_report.setEnabled(true);
                        submit_report.setText(R.string.action_report);
                        getActivity().finish();
                    }else{
                        Toast.makeText(getActivity(), "Error : Retrying... (" + (this.getRetryCount()+1) + " out of " + this.getTotalRetries() + ")", Toast.LENGTH_SHORT).show();
                    }
                    super.onFailure(call, t);
                }
            });

        });


        return rootView;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath();
        return image;
    }
}
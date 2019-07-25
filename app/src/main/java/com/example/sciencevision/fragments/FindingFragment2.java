package com.example.sciencevision.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.camerakit.CameraKitView;
import com.example.sciencevision.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindingFragment2 extends Fragment {

    private CameraKitView cameraKitView;
    private Button btnCapture;
    private String firstLabel;
    public FindingFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_finding, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cameraKitView = view.findViewById(R.id.camera);
        btnCapture= view.findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(photoOnClickListener);

    }

    @Override
    public void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }
    @Override
    public void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }
    @Override
    public void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    private View.OnClickListener photoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                @Override
                public void onImage(CameraKitView cameraKitView, byte[] capturedImage) {
                    File savedPhoto = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
                    try {
                        FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());

                        outputStream.write(capturedImage);
                        //Converts Photofile to Bitmap for Firebase
                        Bitmap bitmap = BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length);
                        // CLOUD : THIS COST MONEY DONT BE DUMB
                        // FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getCloudImageLabeler();
                        // ON-DEVICE : THIS IS FREE, USE A LOT
                        FirebaseVisionImage firebaseVisionImage= FirebaseVisionImage.fromBitmap(bitmap);
                        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler();
                        labeler.processImage(firebaseVisionImage)
                                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                                        // Task completed successfully
                                        // This function sets the text of the TextView given as the parameter
                                        // to be the definition of the object in the image.
                                        Toast.makeText(getContext(), labels.get(0).getText(), LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                        Log.d("FindingFragment", e.toString());
                                    }
                                });
                        outputStream.close();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                        Log.e("Photo","Error");
                    }
                }
            });
        }
    };
}

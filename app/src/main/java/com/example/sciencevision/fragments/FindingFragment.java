package com.example.sciencevision.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.camerakit.CameraKitView;

import com.example.sciencevision.DetailActivity;
import com.example.sciencevision.Models.Findings;
import com.example.sciencevision.R;
import com.example.sciencevision.SearchClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.RandomStringUtils;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindingFragment extends Fragment {

    private static final String TAG = FindingFragment.class.getSimpleName();

    private File photoFile;
    private TextView tvDescription;
    private SearchClient searchClient;
    private ParseUser currUser;
    private ListeningExecutorService service;
    private CameraKitView cameraKitView;
    private ImageView ivLoading;
    private Button btnCapture;
    private Button btnBack;
    private Switch sFirebaseToggle;

    private ChipGroup cgLabels;
    private boolean useCloudMLKit = false;


    public FindingFragment() {
        // Required empty public constructor
    }

    @Override
    @NonNull
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finding, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cameraKitView = view.findViewById(R.id.camera);
        btnCapture = view.findViewById(R.id.btnCapture);
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setVisibility(View.GONE);
        ivLoading = view.findViewById(R.id.ivLoading);
        searchClient = new SearchClient();
        currUser = ParseUser.getCurrentUser();
        service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        btnCapture.setOnClickListener(photoOnClickListener);
        cgLabels = view.findViewById(R.id.cgLabels);
        sFirebaseToggle = view.findViewById(R.id.sFirebaseToggle);
        sFirebaseToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                useCloudMLKit = isChecked;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onPause();
        cameraKitView.onStop();
        cameraKitView.onStart();
        cameraKitView.onResume();

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
        btnBack.setVisibility(View.GONE);
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

    private View.OnClickListener photoOnClickListener = new View.OnClickListener() {

        @Override

        public void onClick(View v) {
            Log.d("onClick", "Button Pressed");


            cameraKitView.captureImage(new CameraKitView.ImageCallback() {

                @Override
                public void onImage(CameraKitView cameraKitView, byte[] capturedImage) {

                    Log.d("onClick", "enters camera click");
                    final File savedPhoto = new File(Environment.getExternalStorageDirectory(), RandomStringUtils.randomAlphabetic(10) + ".jpg");
                    try {
                        Log.d("onClick", "Photo taken");
                        FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());

                        outputStream.write(capturedImage);
                        onStop();
                        //Converts Photofile to Bitmap for Firebase
                        Bitmap bitmap = BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length);

                        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
                        FirebaseVisionImageLabeler labeler;
                        if (useCloudMLKit) {
                            // CLOUD : THIS COST MONEY DONT BE DUMB
                            labeler = FirebaseVision.getInstance().getCloudImageLabeler();
                        } else {
                            // ON-DEVICE : THIS IS FREE, USE A LOT
                            labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler();
                        }


                        labeler.processImage(firebaseVisionImage)
                                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                                        // TODO: stuff with adding chips to cgLabels

                                        for (FirebaseVisionImageLabel label : labels) {
                                            Chip chip = new Chip(getContext());
                                            chip.setChipBackgroundColorResource(R.color.colorPrimary);

                                            chip.setText(label.getText());
                                            chip.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Glide.with(getContext()).load("https://i.imgur.com/VHJZxUy.gif").into(ivLoading);
                                                    postFirebaseCalls(chip.getText().toString(), savedPhoto);
                                                    cgLabels.removeAllViews();
                                                    btnBack.setVisibility(View.GONE);
                                                }
                                            });
                                            cgLabels.addView(chip);
                                        }
                                        btnBack.setVisibility(View.VISIBLE);
                                        btnBack.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                cgLabels.removeAllViews();
                                                btnCapture.setOnClickListener(photoOnClickListener);
                                                btnBack.setVisibility(View.GONE);
                                                cameraKitView.onStop();
                                                cameraKitView.onStart();
                                                cameraKitView.onResume();
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                        Log.d(TAG, e.toString());
                                    }
                                });
                        outputStream.close();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                        Log.e("Photo", "Error");
                    }
                }
            });
        }
    };


    private void postFirebaseCalls(String query, File savedPhoto) {
        ListenableFuture<String> getWiki = service.submit(searchClient.getWiki(query));
        ListenableFuture<String> getExperiments = service.submit(searchClient.getExperimentUrl(query));
        ListenableFuture<String> getFunFacts = service.submit(searchClient.getFactsFromGoogle(query));

        List<ListenableFuture<String>> networkCalls = new ArrayList<>();
        networkCalls.add(getWiki);
        networkCalls.add(getExperiments);
        networkCalls.add(getFunFacts);

        ListenableFuture<List<String>> successfulNetworkCalls = Futures.successfulAsList(networkCalls);
        Futures.addCallback(successfulNetworkCalls, new FutureCallback<List<String>>() {
            @Override
            public void onSuccess(@NullableDecl List<String> result) {
                Log.d("FINAL", result.toString());
                final String description = result.get(0);
                final String experiments = result.get(1);
                final String funFacts = result.get(2);
                ListenableFuture<Findings> saveFindingToDatabase = service.submit(Findings.createFinding(ParseUser.getCurrentUser(), query, description, funFacts, new ParseFile(savedPhoto), experiments));
                Futures.addCallback(saveFindingToDatabase, new FutureCallback<Findings>() {
                    @Override
                    public void onSuccess(@NullableDecl Findings result) {
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        Findings finding = new Findings();
                        finding.setFunFact(funFacts);
                        finding.setExperiment(experiments);
                        finding.setDescription(description);
                        finding.setName(query);
                        intent.putExtra("fromCamera", true);
                        intent.putExtra("Url", savedPhoto.getAbsolutePath());
                        intent.putExtra("Finding", finding);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.d("FindingFragment", "Failed to save Finding");
                        t.printStackTrace();
                    }
                }, service);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, service);
    }


}

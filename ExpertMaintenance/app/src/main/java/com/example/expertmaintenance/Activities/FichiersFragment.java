package com.example.expertmaintenance.Activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.expertmaintenance.R;

public class FichiersFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_GALLERY_IMAGE = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 3;
    private static final int REQUEST_STORAGE_PERMISSION = 4;

    private ImageView selectedImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fichier, container, false);

        // Find the ImageView where the photo will be displayed
        selectedImageView = view.findViewById(R.id.selectedImageView);

        // Button to take photo
        Button takePhotoButton = view.findViewById(R.id.button_take_photo);
        takePhotoButton.setOnClickListener(v -> openCamera());

        // Button to choose photo from gallery
        Button choosePhotoButton = view.findViewById(R.id.button_import_photo);
        choosePhotoButton.setOnClickListener(v -> openGallery());

        // Check for permissions if needed
        checkPermissions();

        return view;
    }

    // Check if permissions are granted
    private void checkPermissions() {
        // Check camera permission
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }

        // Check storage permission
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        }
    }

    // Open the camera to capture an image
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(getContext(), "No camera app available", Toast.LENGTH_SHORT).show();
        }
    } 

    // Open the gallery to pick an image
    private void openGallery() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhotoIntent, REQUEST_GALLERY_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            // Handle camera image
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Get the photo as a Bitmap
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                selectedImageView.setImageBitmap(photo);
            }

            // Handle gallery image
            if (requestCode == REQUEST_GALLERY_IMAGE) {
                // Get the Uri of the selected image
                Uri selectedImageUri = data.getData();
                selectedImageView.setImageURI(selectedImageUri);
            }
        }
    }

    // Handle the permissions request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted for camera
            } else {
                Toast.makeText(getContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted for storage
            } else {
                Toast.makeText(getContext(), "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

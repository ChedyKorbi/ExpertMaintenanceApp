package com.example.expertmaintenance.Activities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.expertmaintenance.R;

public class SignatureFragment extends Fragment {

    private SignatureView signatureView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signature, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the SignatureView and Buttons
        signatureView = view.findViewById(R.id.signature_pad);
        Button clearButton = view.findViewById(R.id.clear_button);
        Button saveButton = view.findViewById(R.id.save_button);

        // Set click listeners for the buttons
        clearButton.setOnClickListener(v -> signatureView.clear());
        saveButton.setOnClickListener(v -> signatureView.saveSignature());
    }
}

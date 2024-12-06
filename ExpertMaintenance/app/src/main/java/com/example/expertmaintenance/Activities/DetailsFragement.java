package com.example.expertmaintenance.Activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.expertmaintenance.R;

public class DetailsFragement extends Fragment {
    private Button carteButton;
    private Button historique;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_details, container, false);
        carteButton = view.findViewById(R.id.mapButton);
        historique = view.findViewById(R.id.historyButton);

        carteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the location (latitude and longitude)
                double latitude = 37.7749;
                double longitude = -122.4194;

                String geoUri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(Target Location)";
                Uri uri = Uri.parse(geoUri);

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                mapIntent.setPackage("com.google.android.apps.maps"); // Ensures Google Maps is used if available
                startActivity(mapIntent);
            }
        });

        historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HistoriqueActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}

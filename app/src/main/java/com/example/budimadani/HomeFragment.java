package com.example.budimadani;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the button we just added in the XML
        MaterialButton btnOfficialWebsite = view.findViewById(R.id.btnOfficialWebsite);

        // Set up the click listener
        if (btnOfficialWebsite != null) {
            btnOfficialWebsite.setOnClickListener(v -> {
                try {
                    // Create an Intent to open the web browser
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.budimadani.gov.my/"));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    // Show a toast if the user doesn't have a web browser installed
                    Toast.makeText(requireContext(), "Unable to open browser", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }
}
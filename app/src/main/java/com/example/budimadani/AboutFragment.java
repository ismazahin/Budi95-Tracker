package com.example.budimadani;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {

    // =============================================
    // UPDATED WITH YOUR DETAILS AND GITHUB LINK
    // =============================================
    private static final String AUTHOR_NAME  = "Isma Zahin Bin Amiruddin";
    private static final String MATRIC_NO    = "2025180017";
    private static final String COURSE       = "Mobile Technology (ICT602)";
    private static final String GITHUB_URL   = "https://github.com/ismazahin/Budi95-Tracker.git";
    // =============================================

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        // Find the views
        TextView tvAuthorName = view.findViewById(R.id.tvAuthorName);
        TextView tvMatricNo   = view.findViewById(R.id.tvMatricNo);
        TextView tvCourse     = view.findViewById(R.id.tvCourse);
        TextView tvGithubUrl  = view.findViewById(R.id.tvGithubUrl);

        // Set the text dynamically
        tvAuthorName.setText(AUTHOR_NAME);
        tvMatricNo.setText(MATRIC_NO);
        tvCourse.setText(COURSE);
        tvGithubUrl.setText("🔗 " + GITHUB_URL);

        // Make GitHub URL clickable
        tvGithubUrl.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(requireContext(),
                        "Could not open URL: " + GITHUB_URL,
                        Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
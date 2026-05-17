package com.example.budimadani; // Make sure this matches your exact package name!

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Locale;

public class HomeFragment extends Fragment {

    // Declare all three TextViews
    private TextView tvHomeRon95Price;
    private TextView tvHomeRon97Price;
    private TextView tvHomeDieselPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Link the Java variables to your XML IDs
        tvHomeRon95Price = view.findViewById(R.id.tvHomeRon95Price);
        tvHomeRon97Price = view.findViewById(R.id.tvHomeRon97Price);
        tvHomeDieselPrice = view.findViewById(R.id.tvHomeDieselPrice);

        // --- NEW: Link the button and make it clickable! ---
        View btnOfficialWebsite = view.findViewById(R.id.btnOfficialWebsite);
        btnOfficialWebsite.setOnClickListener(v -> {
            // This is the official Malaysian government portal for Budi Madani
            String url = "https://budimadani.gov.my";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
        // Fetch the live prices when the Home screen opens
        fetchHomeLivePrices();

        return view;
    }

    private void fetchHomeLivePrices() {
        OkHttpClient client = new OkHttpClient();

        // UPDATED URL: Sort by newest date and fetch 5 rows
        // Using your new limit=3 API here as well
        Request request = new Request.Builder()
                .url("https://api.data.gov.my/data-catalogue?id=fuelprice&sort=-date&limit=3")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Do nothing
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);

                        // Loop through to find the real price
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject latestData = jsonArray.getJSONObject(i);
                            double liveRon95 = latestData.getDouble("ron95");

                            // If the number is greater than 1.00, it's the real price!
                            if (liveRon95 > 1.0) {
                                double liveRon97 = latestData.getDouble("ron97");
                                double liveDiesel = latestData.getDouble("diesel");

                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(() -> {
                                        if (tvHomeRon95Price != null) tvHomeRon95Price.setText(String.format(Locale.getDefault(), "RM %.2f / litre", liveRon95));
                                        if (tvHomeRon97Price != null) tvHomeRon97Price.setText(String.format(Locale.getDefault(), "RM %.2f / litre", liveRon97));
                                        if (tvHomeDieselPrice != null) tvHomeDieselPrice.setText(String.format(Locale.getDefault(), "RM %.2f / litre", liveDiesel));
                                    });
                                }
                                break; // Stop looping once the screen is updated!
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
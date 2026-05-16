package com.example.budimadani;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

// New imports for the API
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

public class CalculatorFragment extends Fragment {

    // UI Elements
    private Spinner spinnerPetrolType;
    private RadioGroup rgInputMode;
    private RadioButton rbLitres;
    private TextInputLayout tilInputValue;
    private TextInputEditText etInputValue;
    private MaterialButton btnCalculate, btnReset;
    private CardView cardResult;

    // Table Elements
    private TextView tvResStatus, tvResMarketPrice, tvResSubPrice, tvResVolume, tvResMarketValue, tvResYouPay, tvResSavings;
    private View rowSubPrice, rowSavings;

    // Petrol type constants
    private static final String RON95  = "RON 95";
    private static final String DIESEL = "Diesel";
    private static final String RON97  = "RON 97";

    // Base Rates (No longer 'final' so the API can update them)
    private double RON95_BASE = 3.87;
    private double DIESEL_BASE = 4.87;
    private double RON97_BASE  = 5.35;

    // Subsidized Rate stays fixed
    private static final double RON95_SUB  = 1.99;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);
        initViews(view);
        setupSpinner();
        setupListeners();

        // Fetch the live prices from data.gov.my as soon as the screen loads!
        fetchLiveFuelPrices();

        return view;
    }

    private void initViews(View view) {
        spinnerPetrolType = view.findViewById(R.id.spinnerPetrolType);
        rgInputMode       = view.findViewById(R.id.rgInputMode);
        rbLitres          = view.findViewById(R.id.rbLitres);
        tilInputValue     = view.findViewById(R.id.tilInputValue);
        etInputValue      = view.findViewById(R.id.etInputValue);
        btnCalculate      = view.findViewById(R.id.btnCalculate);
        btnReset          = view.findViewById(R.id.btnReset);
        cardResult        = view.findViewById(R.id.cardResult);

        // Table Data
        tvResStatus       = view.findViewById(R.id.tvResStatus);
        tvResMarketPrice  = view.findViewById(R.id.tvResMarketPrice);
        tvResSubPrice     = view.findViewById(R.id.tvResSubPrice);
        tvResVolume       = view.findViewById(R.id.tvResVolume);
        tvResMarketValue  = view.findViewById(R.id.tvResMarketValue);
        tvResYouPay       = view.findViewById(R.id.tvResYouPay);
        tvResSavings      = view.findViewById(R.id.tvResSavings);

        rowSubPrice       = view.findViewById(R.id.rowSubPrice);
        rowSavings        = view.findViewById(R.id.rowSavings);
    }

    private void setupSpinner() {
        String[] petrolTypes = {RON95, DIESEL, RON97};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                petrolTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPetrolType.setAdapter(adapter);
    }

    private void setupListeners() {
        rgInputMode.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbLitres) {
                tilInputValue.setHint("Enter Litres (L)");
            } else {
                tilInputValue.setHint("Enter Total Amount (RM)");
            }
            etInputValue.setText("");
            cardResult.setVisibility(View.GONE);
        });

        spinnerPetrolType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                cardResult.setVisibility(View.GONE);
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        btnCalculate.setOnClickListener(v -> calculateTransaction());
        btnReset.setOnClickListener(v -> resetForm());
    }

    // ==========================================
    // API NETWORK CALL
    // ==========================================
    private void fetchLiveFuelPrices() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.data.gov.my/data-catalogue?id=fuelprice&limit=1")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Failed to fetch live prices. Using offline rates.", Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        JSONObject latestData = jsonArray.getJSONObject(0);

                        double liveRon95 = latestData.getDouble("ron95");
                        double liveRon97 = latestData.getDouble("ron97");
                        double liveDiesel = latestData.getDouble("diesel");

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                RON95_BASE = liveRon95;
                                RON97_BASE = liveRon97;
                                DIESEL_BASE = liveDiesel;

                                Toast.makeText(getContext(), "Fuel prices updated to today's rates!", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // ==========================================
    // CALCULATION LOGIC
    // ==========================================
    private void calculateTransaction() {
        String inputStr = etInputValue.getText() != null ? etInputValue.getText().toString().trim() : "";

        if (inputStr.isEmpty()) {
            etInputValue.setError("Please enter a value");
            etInputValue.requestFocus();
            return;
        }

        double inputValue;
        try {
            inputValue = Double.parseDouble(inputStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid number format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (inputValue <= 0) {
            etInputValue.setError("Value must be greater than 0");
            return;
        }

        String petrolType = spinnerPetrolType.getSelectedItem().toString();
        double basePrice = 0, subPrice = 0;
        boolean isEligible = false;

        switch (petrolType) {
            case RON95:
                basePrice = RON95_BASE;
                subPrice = RON95_SUB;
                isEligible = true;
                break;
            case DIESEL:
                basePrice = DIESEL_BASE;
                subPrice = DIESEL_BASE; // No subsidy
                isEligible = false;
                break;
            case RON97:
                basePrice = RON97_BASE;
                subPrice = RON97_BASE; // No subsidy
                isEligible = false;
                break;
        }

        double litres = 0;
        double baseCost = 0;
        double subCost = 0;

        if (rbLitres.isChecked()) {
            litres = inputValue;
            baseCost = litres * basePrice;
            subCost = litres * subPrice;
        } else {
            subCost = inputValue;
            litres = subCost / subPrice;
            baseCost = litres * basePrice;
        }

        double savings = baseCost - subCost;

        if (isEligible) {
            tvResStatus.setText("✅ Eligible");
            tvResStatus.setTextColor(0xFF1B5E20); // Green
            rowSubPrice.setVisibility(View.VISIBLE);
            rowSavings.setVisibility(View.VISIBLE);
            tvResMarketValue.setPaintFlags(tvResMarketValue.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tvResStatus.setText("❌ Not Eligible");
            tvResStatus.setTextColor(0xFFD32F2F); // Red
            rowSubPrice.setVisibility(View.GONE);
            rowSavings.setVisibility(View.GONE);
            tvResMarketValue.setPaintFlags(tvResMarketValue.getPaintFlags() & (~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG));
        }

        tvResMarketPrice.setText(String.format(Locale.getDefault(), "RM %.2f / L", basePrice));
        tvResSubPrice.setText(String.format(Locale.getDefault(), "RM %.2f / L", subPrice));
        tvResVolume.setText(String.format(Locale.getDefault(), "%.2f L", litres));
        tvResMarketValue.setText(String.format(Locale.getDefault(), "RM %.2f", baseCost));
        tvResYouPay.setText(String.format(Locale.getDefault(), "RM %.2f", subCost));
        tvResSavings.setText(String.format(Locale.getDefault(), "RM %.2f", savings));

        cardResult.setVisibility(View.VISIBLE);
        cardResult.post(() -> cardResult.requestFocus());
    }

    private void resetForm() {
        spinnerPetrolType.setSelection(0);
        rbLitres.setChecked(true);
        etInputValue.setText("");
        etInputValue.setError(null);
        cardResult.setVisibility(View.GONE);
    }
}
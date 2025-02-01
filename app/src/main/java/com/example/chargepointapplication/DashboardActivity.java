package com.example.chargepointapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepointapplication.Models.ChargePoint;
import com.example.chargepointapplication.adaptors.ChargePointsAdapter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChargePointsAdapter adapter;
    private List<ChargePoint> chargePointsList = new ArrayList<>();
    private DatabaseHelper databaseHelper;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        databaseHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChargePointsAdapter(chargePointsList, this);
        recyclerView.setAdapter(adapter);

        loadChargePoints();

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterChargePoints(newText);
                return true;
            }
        });

        Button importCsvButton = findViewById(R.id.importCsvButton);
        importCsvButton.setOnClickListener(v -> importCSVData());

        Button createChargePointButton = findViewById(R.id.createChargePointButton);
        createChargePointButton.setOnClickListener(v -> showCreateChargePointDialog());
        String role = getIntent().getStringExtra("USER_ROLE");
        if (!role.equals("Admin")) {
            createChargePointButton.setVisibility(View.GONE);
            importCsvButton.setVisibility(View.GONE);
        }
    }

    private void loadChargePoints() {
        chargePointsList.clear();
        chargePointsList.addAll(databaseHelper.getAllChargePoints());
        adapter.notifyDataSetChanged();
    }

    private void filterChargePoints(String query) {
        List<ChargePoint> filteredList = new ArrayList<>();
        for (ChargePoint chargePoint : chargePointsList) {
            if (chargePoint.getReferenceID().toLowerCase().contains(query.toLowerCase()) ||
                    chargePoint.getTown().toLowerCase().contains(query.toLowerCase()) ||
                    chargePoint.getCounty().toLowerCase().contains(query.toLowerCase()) ||
                    chargePoint.getPostcode().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(chargePoint);
            }
        }
        adapter.updateList(filteredList);
    }

    private void importCSVData() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(Intent.createChooser(intent, "Open CSV"), 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(fileUri);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("referenceID")) continue;

                    String[] columns = line.split(",");

                    String referenceID = columns[0];
                    double latitude = Double.parseDouble(columns[1]);
                    double longitude = Double.parseDouble(columns[2]);
                    String town = columns[3];
                    String county = columns[4];
                    String postcode = columns[5];
                    String chargeDeviceStatus = columns[6];
                    String connectorID = columns[7];
                    String connectorType = columns[8];

                    ChargePoint chargePoint = new ChargePoint(referenceID, latitude, longitude, town, county, postcode, chargeDeviceStatus, connectorID, connectorType);

                    databaseHelper.addChargePoint(chargePoint);
                }

                loadChargePoints();
                Toast.makeText(this, "CSV Data Imported Successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error importing CSV data", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showCreateChargePointDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create New Charge Point");

        View view = getLayoutInflater().inflate(R.layout.dialog_create_chargepoint, null);
        builder.setView(view);

        EditText referenceIdEditText = view.findViewById(R.id.referenceIdEditText);
        EditText latitudeEditText = view.findViewById(R.id.latitudeEditText);
        EditText longitudeEditText = view.findViewById(R.id.longitudeEditText);
        EditText townEditText = view.findViewById(R.id.townEditText);
        EditText countyEditText = view.findViewById(R.id.countyEditText);
        EditText postcodeEditText = view.findViewById(R.id.postcodeEditText);
        EditText chargeDeviceStatusEditText = view.findViewById(R.id.chargeDeviceStatusEditText);
        EditText connectorIdEditText = view.findViewById(R.id.connectorIdEditText);
        EditText connectorTypeEditText = view.findViewById(R.id.connectorTypeEditText);

        builder.setPositiveButton("Create", (dialog, which) -> {
            String referenceId = referenceIdEditText.getText().toString();
            double latitude = Double.parseDouble(latitudeEditText.getText().toString());
            double longitude = Double.parseDouble(longitudeEditText.getText().toString());
            String town = townEditText.getText().toString();
            String county = countyEditText.getText().toString();
            String postcode = postcodeEditText.getText().toString();
            String chargeDeviceStatus = chargeDeviceStatusEditText.getText().toString();
            String connectorId = connectorIdEditText.getText().toString();
            String connectorType = connectorTypeEditText.getText().toString();

            ChargePoint chargePoint = new ChargePoint(referenceId, latitude, longitude, town, county, postcode, chargeDeviceStatus, connectorId, connectorType);

            databaseHelper.addChargePoint(chargePoint);

            loadChargePoints();

            Toast.makeText(DashboardActivity.this, "Charge Point Created Successfully", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}

package com.example.chargepointapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.chargepointapplication.Models.ChargePoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private ChargePoint chargePoint;
    private LatLng chargePointLocation;

    @Override
    //onCreate is the first function that is invoked
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This would load activity_maps view
        setContentView(R.layout.activity_maps);


        chargePoint = (ChargePoint) getIntent().getSerializableExtra("chargePoint");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (chargePoint != null) {
            chargePointLocation = new LatLng(chargePoint.getLatitude(), chargePoint.getLongitude());

            mMap.addMarker(new MarkerOptions()
                    .position(chargePointLocation)
                    .title("Charge Point: " + chargePoint.getReferenceID())
                    .snippet("Town: " + chargePoint.getTown() + "\n" +
                            "County: " + chargePoint.getCounty() + "\n" +
                            "Postcode: " + chargePoint.getPostcode())
            );

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chargePointLocation, 15));

            enableLocationIfPermitted();
        }
    }

    private void enableLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(this, "Location permission is required to show your location on the map", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


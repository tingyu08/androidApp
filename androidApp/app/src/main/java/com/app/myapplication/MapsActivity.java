package com.app.myapplication;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    private EditText originEditText;
    private EditText destinationEditText;
    private Button showRouteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        originEditText = findViewById(R.id.originEditText);
        destinationEditText = findViewById(R.id.destinationEditText);
        showRouteButton = findViewById(R.id.showRouteButton);
        showRouteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showRoute();
            }
        });

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 兩個位置的經緯度
        LatLng origin = new LatLng(25.033611, 121.565000); // 起點位置
        LatLng destination = new LatLng(25.047924, 121.517081); // 終點位置

        // 添加路線
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(origin)
                .add(destination)
                .color(Color.RED)
                .width(5);
        mMap.addPolyline(polylineOptions);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void showRoute() {
        String origin = originEditText.getText().toString();
        String destination = destinationEditText.getText().toString();

        LatLng originLatLng = getLocationFromAddress(origin);
        LatLng destinationLatLng = getLocationFromAddress(destination);

        if (originLatLng != null && destinationLatLng != null) {
            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(originLatLng)
                    .add(destinationLatLng)
                    .color(Color.RED)
                    .width(10);
            mMap.addPolyline(polylineOptions);
            float distance = calculateDistance(originLatLng, destinationLatLng);
            float result = distance/1000;
            Toast.makeText(this, "距離：" + result + " 公里", Toast.LENGTH_SHORT).show();
        }
    }

    private float calculateDistance(LatLng origin, LatLng destination) {
        Location originLocation = new Location("origin");
        originLocation.setLatitude(origin.latitude);
        originLocation.setLongitude(origin.longitude);

        Location destinationLocation = new Location("destination");
        destinationLocation.setLatitude(destination.latitude);
        destinationLocation.setLongitude(destination.longitude);

        return originLocation.distanceTo(destinationLocation);
    }

    private LatLng getLocationFromAddress(String address) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(address, 1);
            if (!addresses.isEmpty()) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                return new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
package com.example.brzostek.project1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.brzostek.common.model.Shop;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShopsActivity extends AppCompatActivity {

    private static final int RC_LOC_PERMISSION = 100;

    private RecyclerView rvShops;
    private EditText editName, editDescription, editRadius;
    private TextView txtLat, txtLong;
    private Button btnAdd;

    private DatabaseHelper databaseHelper = new DatabaseHelper();
    private List<Shop> shops;
    private LocationListener locationListener;
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);

        createNotificationChannel();
        geofencingClient = LocationServices.getGeofencingClient(this);

        rvShops = findViewById(R.id.rvShops);
        editName = findViewById(R.id.editName);
        editDescription = findViewById(R.id.editDescription);
        editRadius = findViewById(R.id.editRadius);
        txtLat = findViewById(R.id.txtLat);
        txtLong = findViewById(R.id.txtLong);
        btnAdd = findViewById(R.id.btnAdd);

        initializeClickListeners();
        initializeList();
        updateList();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Default";
            String description = "Default";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("default", name, importance);
            channel.setDescription(description);
            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }
    }

    private void initializeClickListeners() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.addOrEditShop(getCurrentShop());
                editName.setText(null);
                editDescription.setText(null);
                editRadius.setText(null);
                updateList();
            }
        });
    }

    private void initializeList() {
        LinearLayoutManager rlm = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, rlm.getOrientation());
        ShopsAdapter adapter = new ShopsAdapter(this);
        rvShops.setLayoutManager(rlm);
        rvShops.addItemDecoration(dividerItemDecoration);
        rvShops.setAdapter(adapter);
    }

    private void updateList() {
        databaseHelper.getAllShops(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Shop> shops = new ArrayList<>();
                for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {
                    shops.add(shopSnapshot.getValue(Shop.class));
                }
                ShopsActivity.this.shops = shops;
                ((ShopsAdapter) rvShops.getAdapter()).updateShops(shops);
                updateGeofenceAlarms(shops);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("MissingPermission")
    private void updateGeofenceAlarms(List<Shop> shops) {
        List<Geofence> geofences = new ArrayList<>();
        for (Shop shop : shops) {
            Geofence geofence = new Geofence.Builder()
                    .setRequestId(shop.getName())
                    .setCircularRegion(
                            shop.getLatitude(),
                            shop.getLongitude(),
                            shop.getRadius()
                    )
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .build();
            geofences.add(geofence);
        }

        GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                .addGeofences(geofences)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();

        if (geofencePendingIntent == null) {
            Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
            geofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("TEST", "Geofencing success");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TEST", "Geofencing failure");
                        e.printStackTrace();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isPermissionGranted()) {
            populateView();
        } else {
            requestLocationPermission();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationListener();
    }

    private boolean isPermissionGranted() {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        boolean isPermissionGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        if (isPermissionGranted) {
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{permission}, RC_LOC_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_LOC_PERMISSION) {
            boolean isGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (isGranted) {
                populateView();
                updateGeofenceAlarms(shops);
            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressLint("MissingPermission")
    private void populateView() {
        startLocationListener();

        Location location = getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updateLatLongText(location);
    }

    @SuppressLint("MissingPermission")
    private void startLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLatLongText(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO: handle later
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO: handle later
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO: handle later
            }
        };

        LocationManager locationManager = getLocationManager();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2 * 1000, 0, locationListener);
    }

    private void updateLatLongText(Location location) {
        if (location == null) {
            return;
        }
        txtLat.setText(String.format(Locale.getDefault(), "%.6f", location.getLatitude()));
        txtLong.setText(String.format(Locale.getDefault(), "%.6f", location.getLongitude()));
    }

    private void stopLocationListener() {
        if (locationListener == null) {
            return;
        }
        LocationManager locationManager = getLocationManager();
        locationManager.removeUpdates(locationListener);
        locationListener = null;
    }

    private LocationManager getLocationManager() {
        return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private Shop getCurrentShop() {
        return new Shop(
                editName.getText().toString(),
                editDescription.getText().toString(),
                Integer.parseInt(editRadius.getText().toString()),
                Double.parseDouble(txtLat.getText().toString()),
                Double.parseDouble(txtLong.getText().toString())
        );
    }
}

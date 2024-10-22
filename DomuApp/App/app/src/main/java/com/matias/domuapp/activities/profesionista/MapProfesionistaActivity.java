package com.matias.domuapp.activities.profesionista;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.matias.domuapp.R;
import com.matias.domuapp.activities.MainActivity;
import com.matias.domuapp.activities.cliente.MapClienteActivity;
import com.matias.domuapp.controller.LocationController;
import com.matias.domuapp.controller.ProfessionalController;
import com.matias.domuapp.controller.TokenController;
import com.matias.domuapp.controller.UserController;
import com.matias.domuapp.includes.MyToolbar;
import com.matias.domuapp.providers.AuthProvider;
import com.matias.domuapp.providers.GeofireProvider;

import java.util.logging.Logger;
import com.google.firebase.database.DataSnapshot;


public class MapProfesionistaActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private AuthProvider mAuthProvider;
    private GeofireProvider mGeofireProvider;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;
    private Marker mMarker;
    private Button mButtonConnect;
    private boolean mIsConnect = false;
    private ProfessionalController professionalController;
    private UserController userController;
    private LatLng mCurrentLatLng;
    private ValueEventListener mListener;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    System.out.println("Usuario y Lat"+mAuthProvider.getId()+" "+mCurrentLatLng.toString());
                    if(mMarker != null){
                        mMarker.remove();
                    }
                    mMarker= mMap.addMarker(new MarkerOptions().position(
                                            new LatLng(location.getLatitude(),location.getLongitude())
                                    )
                                    .title("tu posición")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.veterinario_icon))
                    );
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(15f)
                                    .build()
                    ));

                    professionalController.updateLocation(mAuthProvider,mCurrentLatLng);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_profesionista);
        MyToolbar.show(this, "Profesionista", false);

        mAuthProvider = new AuthProvider();
        mGeofireProvider = new GeofireProvider();

        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        mButtonConnect = findViewById(R.id.btnConnect);
        professionalController = new ProfessionalController();
        userController = new UserController();
        mButtonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsConnect) {
                    mIsConnect=professionalController.disconnectProfesional(mFusedLocation,mButtonConnect,mIsConnect,mMarker,mLocationCallback,mAuthProvider,MapProfesionistaActivity.this);
                }
                else {
                    startLocation();
                }
            }
        });
        TokenController tokenController = new TokenController(mAuthProvider.getId());
        isProfesionistWorking(mFusedLocation,mButtonConnect,mIsConnect,mMarker,mLocationCallback,mAuthProvider,MapProfesionistaActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mGeofireProvider.isProfesionistWorking(mAuthProvider.getId()).removeEventListener(mListener);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);
        startLocation();

}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (gpsActived()) {
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        //mMap.setMyLocationEnabled(true);
                    } else {
                        showAlertDialogNOGPS();
                    }
                } else {
                    userController.checkLocationPermissions(MapProfesionistaActivity.this);
                }
            } else {
                userController.checkLocationPermissions(MapProfesionistaActivity.this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && gpsActived()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            //mMap.setMyLocationEnabled(true);
        }
        else {
            showAlertDialogNOGPS();
        }
    }

    private void showAlertDialogNOGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor activa tu ubicacion para continuar")
                .setPositiveButton("Configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }

    private boolean gpsActived() {
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isActive = true;
        }
        return isActive;
    }



    private void startLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActived()) {
                    mButtonConnect.setText("Desconectarse");
                    mIsConnect = true;
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    //mMap.setMyLocationEnabled(true);
                }
                else {
                    showAlertDialogNOGPS();
                }
            }
            else {
                userController.checkLocationPermissions(MapProfesionistaActivity.this);
            }
        } else {
            if (gpsActived()) {
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                //mMap.setMyLocationEnabled(true);
            }
            else {
                showAlertDialogNOGPS();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profesionista_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            mIsConnect=professionalController.logoutProfesional(mFusedLocation,mButtonConnect,mIsConnect,mMarker,mLocationCallback,mAuthProvider,MapProfesionistaActivity.this);
        }
        if (item.getItemId() == R.id.action_update) {
            Intent intent = new Intent(MapProfesionistaActivity.this, UpdateProfileProfesionistActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.action_history) {
            Intent intent = new Intent(MapProfesionistaActivity.this, HistoryBookingProfesionistActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void isProfesionistWorking(final FusedLocationProviderClient mFusedLocation, final Button mButtonConnect, final Boolean mIsConnect,
                                      final Marker mMarker, final LocationCallback mLocationCallback, final AuthProvider mAuthProvider, Context context) {
        System.out.println("Profesionist is working");
        mListener = mGeofireProvider.isProfesionistWorking(mAuthProvider.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //disconnect();
                    professionalController.disconnectProfesional(mFusedLocation,mButtonConnect,mIsConnect,mMarker,mLocationCallback,mAuthProvider,MapProfesionistaActivity.this);
                    System.out.println("Reporte de vientos");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
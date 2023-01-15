package com.matias.domuapp.activities.cliente;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.Status;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.matias.domuapp.R;
import com.matias.domuapp.activities.MainActivity;
import com.matias.domuapp.activities.profesionista.MapProfesionistaActivity;
import com.matias.domuapp.controller.ClientController;
import com.matias.domuapp.controller.TokenController;
import com.matias.domuapp.controller.UserController;
import com.matias.domuapp.includes.MyToolbar;
import com.matias.domuapp.providers.AuthProvider;
import com.matias.domuapp.providers.GeofireProvider;
import com.matias.domuapp.providers.ProfesionistaProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapClienteActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private AuthProvider mAuthProvider;
    private GeofireProvider mGeofireProvider;
    //private TokenProvider mTokenProvider;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;
    private Marker mMarker;
    private ClientController clientController;
    private UserController userController;
    private LatLng mCurrentLatLng;
    private Button mButtonRequestProfesionist;
    private List<Marker> mProfesionistMarkers = new ArrayList<>();
    private boolean mIsFirstTime = true;
    private PlacesClient mPlaces;
    private AutocompleteSupportFragment mAutocomplete;
    private AutocompleteSupportFragment mAutocompleteDestination;

    private String mOrigin;
    private String mPrice;
    private LatLng mOriginLatLng;
    private String mDestination;
    private LatLng mDestinationLatLng;
    private TokenController tokenController;
    private GoogleMap.OnCameraIdleListener mCameraListener;
    private LatLng driverLatLng ;
    private ProfesionistaProvider profesionistaProvider;
    private String servicio;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

            for(Location location: locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    if (mMarker != null) {
                        mMarker.remove();
                    }

                    mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                    // OBTENER LA LOCALIZACION DEL USUARIO EN TIEMPO REAL
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(15f)
                                    .build()
                    ));
                    /*
                    mMarker = mMap.addMarker(new MarkerOptions().position(
                                            new LatLng(location.getLatitude(), location.getLongitude())
                                    )
                                    .title("Tu posicion")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_pin_blue))
                    );*/

                    if (mIsFirstTime) {
                        mIsFirstTime = false;
                        getActiveProfesionist();
                        limitSearch();
                    }
                }
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_cliente);
        MyToolbar.show(this, "Cliente", false);

        mAuthProvider = new AuthProvider();
        mGeofireProvider = new GeofireProvider();
        clientController = new ClientController();
        userController = new UserController();
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        Intent intentBefore= getIntent();
        Bundle bundle = intentBefore.getExtras();
        if(bundle!=null)
        {
            servicio =(String) bundle.get("Servicio");
        }

        mButtonRequestProfesionist = findViewById(R.id.btnRequestProfesionist);
        if (!Places.isInitialized()) { //AIzaSyColM7tqEOPJre54InK5gj0wFIk7DVJku0
            Places.initialize(getApplicationContext(), "AIzaSyAWZmeSv-qeL_jUoDDVLtzTlLWlyCMwUzU");
        }
        mPlaces = Places.createClient(this);
        instanceAutocompleteOrigin();
        instanceAutocompleteDestination();
        onCameraMove();
        mButtonRequestProfesionist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //requestProfesionist();
                clientController.requestProfesionist(MapClienteActivity.this,mOriginLatLng, mDestinationLatLng, mPrice, mOrigin,mDestination,servicio);
            }
        });
        tokenController = new TokenController(mAuthProvider.getId());
    }
    private void onCameraMove(){
            mCameraListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                try {
                    Geocoder geocoder = new Geocoder(MapClienteActivity.this);
                    mOriginLatLng = mMap.getCameraPosition().target;
                    List<Address> addressList = geocoder.getFromLocation(mOriginLatLng.latitude, mOriginLatLng.longitude, 1);
                    String city = addressList.get(0).getLocality();
                    String country = addressList.get(0).getCountryName();
                    String address = addressList.get(0).getAddressLine(0);
                    mOrigin = address + " " + city;
                    mAutocomplete.setText(address + " " + city);
                    System.out.println("Ubicacion " + address + " " + city);
                } catch (Exception e) {
                    Log.d("Error: ", "Mensaje error: " + e.getMessage());
                }
            }
        };
    }

    private void instanceAutocompleteOrigin() {
        mAutocomplete = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.placeAutocompleteOrigin);
        mAutocomplete.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        mAutocomplete.setHint("Lugar de servicio");
        mAutocomplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mOrigin = place.getName();
                mOriginLatLng = place.getLatLng();
                Log.d("","InstanceAutoCompleteOrigin");

                Log.d("PLACE", "Name: " + mOrigin);
                Log.d("PLACE", "Lat: " + mOriginLatLng.latitude);
                Log.d("PLACE", "Lng: " + mOriginLatLng.longitude);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }

    private void instanceAutocompleteDestination() {
        mAutocompleteDestination = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.placeAutocompleteDestination);
        mAutocompleteDestination.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        mAutocompleteDestination.setHint("Destino");
        mAutocompleteDestination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mDestination = place.getName();
                mDestinationLatLng = place.getLatLng();
                Log.d("","InstanceAutoCompleteDestination");

                Log.d("PLACE", "Name: " + mDestination);
                Log.d("PLACE", "Lat: " + mDestinationLatLng.latitude);
                Log.d("PLACE", "Lng: " + mDestinationLatLng.longitude);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MapClienteActivity.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
        }
        mMap.setOnCameraIdleListener(mCameraListener);

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
                    } else {
                        showAlertDialogNOGPS();
                    }
                } else {
                    userController.checkLocationPermissions(MapClienteActivity.this);
                }
            } else {
                userController.checkLocationPermissions(MapClienteActivity.this);
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
        }
        else if (requestCode == SETTINGS_REQUEST_CODE && !gpsActived()){
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
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(true);
                }
                else {
                    showAlertDialogNOGPS();
                }
            }
            else {
                userController.checkLocationPermissions(MapClienteActivity.this);
            }
        } else {
            if (gpsActived()) {
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            }
            else {
                showAlertDialogNOGPS();
            }
        }
    }

    private void getActiveProfesionist() {
        mGeofireProvider.getActiveProfesionist(mCurrentLatLng, 7).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                // AÑADIREMOS LOS MARCADORES DE LOS PROFESIONISTAS QUE SE CONECTEN EN LA APLICACION

                for (Marker marker : mProfesionistMarkers) {
                    if (marker.getTag() != null) {
                        if (marker.getTag().equals(key)) {
                            return;
                        }
                    }
                }
                driverLatLng = new LatLng(location.latitude, location.longitude);
                profesionistaProvider = new ProfesionistaProvider();
                profesionistaProvider.getProfesionist(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String obtainedService=dataSnapshot.child("servicio").getValue().toString();
                            if(obtainedService.compareTo(servicio)==0 && obtainedService.compareTo("Veterinario")==0){
                                Marker marker = mMap.addMarker(new MarkerOptions().position(driverLatLng).title("Veterinario disponible").icon(BitmapDescriptorFactory.fromResource(R.drawable.veterinario_icon)));
                                //mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(getApplicationContext())));
                                marker.setTag(dataSnapshot.child("id").getValue().toString());
                                mProfesionistMarkers.add(marker);
                            } else if (obtainedService.compareTo(servicio)==0 && obtainedService.compareTo("Estilista")==0){
                                Marker marker = mMap.addMarker(new MarkerOptions().position(driverLatLng).title("Estilista disponible").icon(BitmapDescriptorFactory.fromResource(R.drawable.estilista)));
                                //mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(getApplicationContext())));
                                marker.setTag(dataSnapshot.child("id").getValue().toString());
                                mProfesionistMarkers.add(marker);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onKeyExited(String key) {
                for (Marker marker : mProfesionistMarkers) {
                    if (marker.getTag() != null) {
                        if (marker.getTag().equals(key)) {
                            marker.remove();
                            mProfesionistMarkers.remove(marker);
                            return;
                        }
                    }
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                // ACTUALIZAR LA POSICION DE CADA PROFESIONISTA
                for (Marker marker : mProfesionistMarkers) {
                    if (marker.getTag() != null) {
                        if (marker.getTag().equals(key)) {
                            marker.setPosition(new LatLng(location.latitude, location.longitude));
                        }
                    }
                }
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private static final String TAG = "CustomInfoWindowAdapter";
        private LayoutInflater inflater;

        public CustomInfoWindowAdapter(LayoutInflater inflater){
            this.inflater = inflater;
        }

        @Override
        public View getInfoContents(final Marker m) {
            //Carga layout personalizado.
            View v = inflater.inflate(R.layout.infowindow_layout, null);
            String[] info = m.getTitle().split("&");
            String url = m.getSnippet();
            ((TextView)v.findViewById(R.id.info_window_nombre)).setText("Lina Cortés");
            ((TextView)v.findViewById(R.id.info_window_placas)).setText("Vacunas: $100 \nConsulta: $150" +
                    "\n" + "Curación: $100");

            return v;
        }

        @Override
        public View getInfoWindow(Marker m) {
            return null;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profesionista_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void limitSearch() {
        LatLng northSide = SphericalUtil.computeOffset(mCurrentLatLng, 4000, 0);
        LatLng southSide = SphericalUtil.computeOffset(mCurrentLatLng, 4000, 180);
        mAutocomplete.setCountry("MEX");
        mAutocomplete.setLocationBias(RectangularBounds.newInstance(southSide, northSide));
        mAutocompleteDestination.setCountry("MEX");
        mAutocompleteDestination.setLocationBias(RectangularBounds.newInstance(southSide, northSide));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            clientController.logout(mAuthProvider,MapClienteActivity.this);
        }
        if (item.getItemId() == R.id.action_update) {
            Intent intent = new Intent(MapClienteActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.action_history) {
            Intent intent = new Intent(MapClienteActivity.this, HistoryBookingClientActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
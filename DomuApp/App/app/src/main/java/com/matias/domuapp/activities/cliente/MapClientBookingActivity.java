package com.matias.domuapp.activities.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.matias.domuapp.activities.profesionista.MapProfesionistBookingActivity;
import com.matias.domuapp.controller.NotificationController;
import com.matias.domuapp.controller.TokenController;
import com.matias.domuapp.controller.UserController;
import com.matias.domuapp.providers.AuthProvider;
import com.matias.domuapp.providers.ClientBookingProvider;
import com.matias.domuapp.providers.GeofireProvider;
import com.matias.domuapp.providers.GoogleApiProvider;
import com.matias.domuapp.providers.ProfesionistaProvider;
//import com.matias.domuapp.providers.TokenProvider;
import com.matias.domuapp.R;
import com.matias.domuapp.utils.DecodePoints;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MapClientBookingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private AuthProvider mAuthProvider;
    private GeofireProvider mGeofireProvider;
    //private TokenProvider mTokenProvider;
    private ClientBookingProvider mClientBookingProvider;
    private ProfesionistaProvider mProfesionistProvider;
    private Marker mMarkerProfesionist;
    private boolean mIsFirstTime = true;
    private String mOrigin;
    private LatLng mOriginLatLng;
    private Button mButtonCancelRequest;
    private String mDestination;
    private LatLng mDestinationLatLng;
    private LatLng mProfesionistLatLng;
    private TextView mTextViewClientBooking;
    private TextView mTextViewEmailClientBooking;
    private TextView mTextViewOriginClientBooking;
    private TextView mTextViewDestinationClientBooking;
    private TextView mTextViewStatusBooking;
    private ImageView mImageViewBooking;
    private GoogleApiProvider mGoogleApiProvider;
    private List<LatLng> mPolylineList;
    private PolylineOptions mPolylineOptions;
    private ValueEventListener mListener;
    private String mIdProfesionist;
    private ValueEventListener mListenerStatus;
    private UserController userController;
    NotificationController notificationController;
    private String  mIdProfesionistFound = "";
    private TokenController tokenController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_client_booking);

        mAuthProvider = new AuthProvider();
        notificationController = new NotificationController();
        mGeofireProvider = new GeofireProvider();
        //mTokenProvider = new TokenProvider();
        mClientBookingProvider = new ClientBookingProvider();
        mButtonCancelRequest = findViewById(R.id.btnCancelRequest);
        mGoogleApiProvider = new GoogleApiProvider(MapClientBookingActivity.this);
        mProfesionistProvider = new ProfesionistaProvider();
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));
        }

        mTextViewClientBooking = findViewById(R.id.textViewProfesionistBooking);
        mTextViewEmailClientBooking = findViewById(R.id.textViewEmailProfesionistBooking);
        mTextViewStatusBooking = findViewById(R.id.textViewStatusBooking);
        mTextViewOriginClientBooking = findViewById(R.id.textViewOriginProfesionistBooking);
        mTextViewDestinationClientBooking = findViewById(R.id.textViewDestinationProfesionistBooking);
        mImageViewBooking = findViewById(R.id.imageViewClientBooking);
        mButtonCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelRequest();
            }
        });
        getStatus();
        getClientBooking();

        //mPlaces = Places.createClient(this);
    }

    private void cancelRequest() {
        mClientBookingProvider.delete(mAuthProvider.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notificationController.sendNotificationCancel(MapClientBookingActivity.this,mIdProfesionistFound,tokenController);
            }
        });
    }

    private void getStatus() {
        mListenerStatus =  mClientBookingProvider.getStatus(mAuthProvider.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String status = dataSnapshot.getValue().toString();
                    if (status.equals("accept")) {
                        mTextViewStatusBooking.setText("Estado: Aceptado");
                        //startBooking();
                    }
                    if (status.equals("start")) {
                        mTextViewStatusBooking.setText("Estado: Servicio Iniciado");
                        startBooking();
                    }
                    else if (status.equals("finish")) {
                        mTextViewStatusBooking.setText("Estado: Servicio Finalizado");
                        finishBooking();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void finishBooking() {
        Intent intent = new Intent(MapClientBookingActivity.this, CalificationProfesionistActivity.class);
        startActivity(intent);
        finish();
    }

    private void rutaBooking() {
        //mMap.clear();
        //comentar estas lineas de abajo
        mMap.addMarker(new MarkerOptions().position(mDestinationLatLng).title("Ubicación").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_ubicacion)));
        drawRoute(mDestinationLatLng);
    }
    private void startBooking() {
        mMap.clear();
        //comentar estas lineas de abajo
        //mMap.addMarker(new MarkerOptions().position(mDestinationLatLng).title("Ubicación").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_ubicacion)));
        //drawRoute(mDestinationLatLng);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mGeofireProvider.getProfesionistLocation(mIdProfesionist).removeEventListener(mListener);
        }
        if (mListenerStatus != null) {
            mClientBookingProvider.getStatus(mAuthProvider.getId()).removeEventListener(mListenerStatus);
        }
    }

    private void getClientBooking() {
        mClientBookingProvider.getClientBooking(mAuthProvider.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String destination = dataSnapshot.child("destination").getValue().toString();
                    String origin = dataSnapshot.child("origin").getValue().toString();
                    String idProfesionist = dataSnapshot.child("idProfesionist").getValue().toString();
                    mIdProfesionist = idProfesionist;
                    double destinatioLat = Double.parseDouble(dataSnapshot.child("destinationLat").getValue().toString());
                    double destinatioLng= Double.parseDouble(dataSnapshot.child("destinationLng").getValue().toString());

                    double originLat = Double.parseDouble(dataSnapshot.child("originLat").getValue().toString());
                    double originLng= Double.parseDouble(dataSnapshot.child("originLng").getValue().toString());
                    mOriginLatLng = new LatLng(originLat, originLng);
                    mDestinationLatLng = new LatLng(destinatioLat, destinatioLng);
                    mTextViewOriginClientBooking.setText("Ubicacion en: " + origin);
                    mTextViewDestinationClientBooking.setText("Servicio: ");
                    //mMap.addMarker(new MarkerOptions().position(mOriginLatLng).title("Ubicación Profesionista").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_ubicacion)));
                    //getProfesionist(idProfesionist);
                    userController = new UserController();
                    userController.getUser(idProfesionist,"cliente", MapClientBookingActivity.this, mImageViewBooking,
                            mTextViewClientBooking, mTextViewEmailClientBooking,mTextViewDestinationClientBooking);
                    getProfesionistLocation(idProfesionist);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }

    private void getProfesionist(String idProfesionist) {
        /*mProfesionistProvider.getProfesionist(idProfesionist).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String image = "";
                    if (dataSnapshot.hasChild("image")) {
                        image = dataSnapshot.child("image").getValue().toString();
                        Picasso.with(MapClientBookingActivity.this).load(image).into(mImageViewBooking);
                    }
                    mTextViewClientBooking.setText(name);
                    mTextViewEmailClientBooking.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }



    private void getProfesionistLocation(String idProfesionist) {
        mListener = mGeofireProvider.getProfesionistLocation(idProfesionist).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double lat = Double.parseDouble(dataSnapshot.child("0").getValue().toString());
                    double lng = Double.parseDouble(dataSnapshot.child("1").getValue().toString());
                    mProfesionistLatLng = new LatLng(lat, lng);
                    if (mMarkerProfesionist!= null) {
                        mMarkerProfesionist.remove();
                    }
                    mMarkerProfesionist = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lng))
                            .title("Tu profesionista")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.veterinario_icon)));
                    mMap.setInfoWindowAdapter(new MapClientBookingActivity.CustomInfoWindowAdapter(LayoutInflater.from(getApplicationContext())));

                    if (mIsFirstTime) {
                        mIsFirstTime = false;
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                new CameraPosition.Builder()
                                        .target(mProfesionistLatLng)
                                        .zoom(15f)
                                        .build()
                        ));
                        drawRoute(mOriginLatLng);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    private void drawRoute(LatLng latLng) {
        mGoogleApiProvider.getDirections(mProfesionistLatLng, latLng).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {

                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    JSONObject route = jsonArray.getJSONObject(0);
                    JSONObject polylines = route.getJSONObject("overview_polyline");
                    String points = polylines.getString("points");
                    mPolylineList = DecodePoints.decodePoly(points);
                    mPolylineOptions = new PolylineOptions();
                    mPolylineOptions.color(Color.DKGRAY);
                    mPolylineOptions.width(13f);
                    mPolylineOptions.startCap(new SquareCap());
                    mPolylineOptions.jointType(JointType.ROUND);
                    mPolylineOptions.addAll(mPolylineList);
                    mMap.addPolyline(mPolylineOptions);

                    JSONArray legs =  route.getJSONArray("legs");
                    JSONObject leg = legs.getJSONObject(0);
                    JSONObject distance = leg.getJSONObject("distance");
                    JSONObject duration = leg.getJSONObject("duration");
                    String distanceText = distance.getString("text");
                    String durationText = duration.getString("text");


                } catch(Exception e) {
                    Log.d("Error", "Error encontrado " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            return;
        }
    }
}

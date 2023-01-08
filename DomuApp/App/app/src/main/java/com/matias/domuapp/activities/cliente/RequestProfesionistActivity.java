package com.matias.domuapp.activities.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.matias.domuapp.R;
import com.matias.domuapp.controller.NotificationController;
import com.matias.domuapp.controller.TokenController;
import com.matias.domuapp.models.ClientBooking;
import com.matias.domuapp.models.FCMBody;
import com.matias.domuapp.models.FCMResponse;
import com.matias.domuapp.providers.AuthProvider;
import com.matias.domuapp.providers.ClientBookingProvider;
import com.matias.domuapp.providers.GeofireProvider;
import com.matias.domuapp.providers.GoogleApiProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestProfesionistActivity extends AppCompatActivity {
    private LottieAnimationView mAnimation;
    private TextView mTextViewLookingFor;
    private Button mButtonCancelRequest;
    private GeofireProvider mGeofireProvider;

    private String mExtraOrigin;
    private String mExtraDestination;
    private double mExtraOriginLat;
    private double mExtraOriginLng;
    private double mExtraDestinationLat;
    private double mExtraDestinationLng;
    private LatLng mOriginLatLng;
    private LatLng mDestinationLatLng;

    private double mRadius = 0.1;
    private boolean mProfesionistFound = false;
    private String  mIdProfesionistFound = "";
    private LatLng mProfesionistFoundLatLng;
    //private NotificationProvider mNotificationProvider;
    //private TokenProvider mTokenProvider;
    private TokenController tokenController;
    private ClientBookingProvider mClientBookingProvider;
    private AuthProvider mAuthProvider;
    private GoogleApiProvider mGoogleApiProvider;
    NotificationController notificationController;
    private ValueEventListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_profesionist);

        mAnimation = findViewById(R.id.animation);
        mTextViewLookingFor = findViewById(R.id.textViewLookingFor);
        mButtonCancelRequest = findViewById(R.id.btnCancelRequest);

        mAnimation.playAnimation();

        gettingInformationBeforeIntent();

        //mGeofireProvider = new GeofireProvider("active_profesionist");
        mGeofireProvider = new GeofireProvider();

        //mTokenProvider = new TokenProvider();
        //mNotificationProvider = new NotificationProvider();
        mClientBookingProvider = new ClientBookingProvider();
        mAuthProvider = new AuthProvider();
        mGoogleApiProvider = new GoogleApiProvider(RequestProfesionistActivity.this);
        notificationController = new NotificationController();
        mButtonCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelRequest();
            }
        });
        getClosestProfesionist();
    }

    private void gettingInformationBeforeIntent(){
        mExtraOrigin = getIntent().getStringExtra("origin");
        mExtraDestination = getIntent().getStringExtra("destination");
        mExtraOriginLat = getIntent().getDoubleExtra("origin_lat", 0);
        mExtraOriginLng = getIntent().getDoubleExtra("origin_lng", 0);
        mExtraDestinationLat = getIntent().getDoubleExtra("destination_lat", 0);
        mExtraDestinationLng = getIntent().getDoubleExtra("destination_lng", 0);
        mOriginLatLng = new LatLng(mExtraOriginLat, mExtraOriginLng);
        mDestinationLatLng= new LatLng(mExtraDestinationLat, mExtraDestinationLng);
    }

    private void cancelRequest() {
        mClientBookingProvider.delete(mAuthProvider.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notificationController.sendNotificationCancel(RequestProfesionistActivity.this,mIdProfesionistFound,tokenController);
            }
        });
    }


    private void getClosestProfesionist() {
        mGeofireProvider.getActiveProfesionist(mOriginLatLng,  mRadius).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!mProfesionistFound) {
                    mProfesionistFound = true;
                    mIdProfesionistFound = key;
                    mProfesionistFoundLatLng = new LatLng(location.latitude, location.longitude);
                    mTextViewLookingFor.setText("PROFESIONISTA ENCONTRADO\nESPERANDO RESPUESTA");
                    createClientBooking();
                    Log.d("getClosestProfesionist", "Entrando");
                    Log.d("PROFESIONISTA", "ID: " + mIdProfesionistFound);
                }
            }
            @Override
            public void onKeyExited(String key) {}
            @Override
            public void onKeyMoved(String key, GeoLocation location) {}
            @Override
            public void onGeoQueryReady() {
                // INGRESA CUANDO TERMINA LA BUSQUEDA DEL PROFESIONISTA EN UN RADIO DE 0.1 KM
                if (!mProfesionistFound) {
                    mRadius = mRadius + 0.1f;
                    // NO ENCONTRO NINGUN PROFESIONISTA
                    if (mRadius > 5) {
                        mTextViewLookingFor.setText("NO SE ENCONTRO UN PROFESIONISTA DISPONIBLE");
                        Toast.makeText(RequestProfesionistActivity.this, "NO SE ENCONTRO UN PROFESIONISTA DISPONIBLE", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        getClosestProfesionist();
                    }
                }
            }
            @Override
            public void onGeoQueryError(DatabaseError error) {
            }
        });
    }

    private void createClientBooking() {
        mGoogleApiProvider.getDirections(mOriginLatLng, mProfesionistFoundLatLng).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    JSONObject route = jsonArray.getJSONObject(0);
                    JSONObject polylines = route.getJSONObject("overview_polyline");
                    String points = polylines.getString("points");
                    JSONArray legs =  route.getJSONArray("legs");
                    JSONObject leg = legs.getJSONObject(0);
                    JSONObject distance = leg.getJSONObject("distance");
                    JSONObject duration = leg.getJSONObject("duration");
                    String distanceText = distance.getString("text");
                    String durationText = duration.getString("text");
                    tokenController = new TokenController(mAuthProvider.getId());

                    notificationController.sendNotification(durationText,distanceText,tokenController,mIdProfesionistFound,RequestProfesionistActivity.this,
                            mAuthProvider, mExtraOrigin, mExtraDestination,mExtraOriginLat, mExtraOriginLng, mExtraDestinationLat, mExtraDestinationLng,mClientBookingProvider);

                    //sendNotification(durationText, distanceText);
                } catch(Exception e) {
                    Log.d("Error", "Error encontrado " + e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {}
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mClientBookingProvider.getStatus(mAuthProvider.getId()).removeEventListener(mListener);
        }
    }



}
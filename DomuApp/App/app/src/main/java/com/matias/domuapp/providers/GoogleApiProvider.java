package com.matias.domuapp.providers;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.matias.domuapp.R;
import com.matias.domuapp.retrofit.IGoogleApi;
import com.matias.domuapp.retrofit.RetrofitClient;

import java.util.Date;

import retrofit2.Call;

public class GoogleApiProvider {
    private Context context;


    public GoogleApiProvider(Context context) {
        this.context = context;
    }

    public Call<String> getDirections(LatLng originLatLng, LatLng destinationLatLng) {
        String baseUrl = "https://maps.googleapis.com";

        String query = "/maps/api/directions/json?mode=driving&transit_routing_preferences=less_driving&"
                + "origin=" + originLatLng.latitude + "," + originLatLng.longitude + "&"
                + "destination=" + destinationLatLng.latitude + "," + destinationLatLng.longitude + "&"
                + "departure_time=" + (new Date().getTime() + (60*60*1000)) + "&"
                + "traffic_model=best_guess&"
                + "key=" + context.getResources().getString(R.string.google_maps_key);
        return RetrofitClient.getClient(baseUrl).create(IGoogleApi.class).getDirections(baseUrl + query);
    }
}

package com.matias.domuapp.providers;

import com.matias.domuapp.models.FCMBody;
import com.matias.domuapp.models.FCMResponse;
import com.matias.domuapp.retrofit.IFCMApi;
import com.matias.domuapp.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {

    private String url = "https://fcm.googleapis.com";

    public NotificationProvider() {
    }

    public Call<FCMResponse> sendNotification(FCMBody body) {
        return RetrofitClient.getClientObject(url).create(IFCMApi.class).send(body);
    }
}

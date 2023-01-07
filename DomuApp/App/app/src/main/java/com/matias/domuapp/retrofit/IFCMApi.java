package com.matias.domuapp.retrofit;

import com.matias.domuapp.models.FCMBody;
import com.matias.domuapp.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA7SvP2t8:APA91bEARVlwdHbF2I06NVuWniDRD0_FXiJ-dxBi6gGDdeGn6oFBi2k73UKY1FuFkL3o6xhtoFf8J_mD_jdnLLFtr1WmH2sCRg4m56ZAWEtfcwcKPlZy_yYTKvSec-D4G24i-o6EwoEm"
    })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);

}

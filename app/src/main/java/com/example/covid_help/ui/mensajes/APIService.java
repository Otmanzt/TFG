package com.example.covid_help.ui.mensajes;

import com.example.covid_help.ui.notificacion.Emisor;
import com.example.covid_help.ui.notificacion.MyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAARj1ogzQ:APA91bFDNV2nfhfUry-qk0NNSgRB22rKZvRtoZ8FqiVUiP7yvMsPoQ3VcYS5abRlKVf5j2lxKXO1y7YLDp2L9x-87yX4wTfXoidWKOYbXY9TnsdUapuOpL0LM3tQIKQywSI6PtmtlZPx"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Emisor body);
}

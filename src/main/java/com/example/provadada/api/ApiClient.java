package com.example.provadada.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Se il tuo sito locale è in http://localhost/wordpress, usa 10.0.2.2 per il simulatore
    private static final String BASE_URL = "http://10.0.2.2/wordpress/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

package com.example.provadada.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Inserisci qui l'URL del tuo sito; ad esempio, se il sito è in produzione, qualcosa tipo "https://www.tuosito.com/"
    private static final String BASE_URL = "https://tuosito.com/";
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

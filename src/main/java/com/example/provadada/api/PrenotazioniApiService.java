package com.example.provadada.api;

import com.example.provadada.models.Prenotazione;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

public interface PrenotazioniApiService {

    // GET endpoint: Recupera prenotazioni pending per una data specifica
    @GET("wp-json/dada/v1/prenotazioni/")
    Call<List<Prenotazione>> getPrenotazioni(@Query("data") String data);

    // POST endpoint: Aggiorna lo status di una prenotazione con ID specifico
    @FormUrlEncoded
    @POST("wp-json/dada/v1/prenotazioni/{id}")
    Call<Void> updatePrenotazioneStatus(@Path("id") int id, @Field("status") String status);
}

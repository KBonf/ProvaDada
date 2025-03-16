package com.example.provadada;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import com.example.provadada.models.Prenotazione;

public interface PrenotazioniService {
    // Effettua una richiesta GET all'endpoint "prenotazioni" passando la data come parametro di query.
    @GET("prenotazioni")
    Call<List<Prenotazione>> getPrenotazioni(@Query("data") String data);
}

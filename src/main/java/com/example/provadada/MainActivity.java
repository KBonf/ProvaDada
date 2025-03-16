package com.example.provadada;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.example.provadada.api.ApiClient;
import com.example.provadada.api.PrenotazioniApiService;
import com.example.provadada.models.Prenotazione;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private PrenotazioniApiService apiService;
    private List<Prenotazione> prenotazioniList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inizializza Retrofit e il servizio API
        apiService = ApiClient.getClient().create(PrenotazioniApiService.class);

        // Quando si clicca sul pulsante "Carica Prenotazioni Pending", esegui il polling
        findViewById(R.id.btnLoadPrenotazioni).setOnClickListener(view -> loadPrenotazioni());

        // Esempi di pulsanti per aggiornare lo status (usando ID 1 come esempio)
        findViewById(R.id.btnAccept).setOnClickListener(view -> updateStatus(1, "accepted"));
        findViewById(R.id.btnReject).setOnClickListener(view -> updateStatus(1, "rejected"));
    }

    private void loadPrenotazioni() {
        // Imposta la data da interrogare (modifica in base alle tue esigenze)
        String data = "2023-04-15"; // Oppure recuperala dinamicamente
        Log.d("MainActivity", "Richiedo prenotazioni per data: " + data);
        Call<List<Prenotazione>> call = apiService.getPrenotazioni(data);
        call.enqueue(new Callback<List<Prenotazione>>() {
            @Override
            public void onResponse(Call<List<Prenotazione>> call, Response<List<Prenotazione>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    prenotazioniList = response.body();
                    Log.d("MainActivity", "Ricevute " + prenotazioniList.size() + " prenotazioni");
                    // Qui, ad esempio, aggiorna la UI (RecyclerView, ListView, ecc.)
                } else {
                    Log.e("MainActivity", "Errore nella risposta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Prenotazione>> call, Throwable t) {
                Log.e("MainActivity", "Errore nel caricamento: " + t.getMessage());
            }
        });
    }

    private void updateStatus(int id, String status) {
        Call<Void> call = apiService.updatePrenotazioneStatus(id, status);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Prenotazione aggiornata a: " + status, Toast.LENGTH_SHORT).show();
                    loadPrenotazioni();
                } else {
                    Toast.makeText(MainActivity.this, "Errore nell'aggiornamento: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Aggiornamento fallito: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.provadada;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    // Potresti avere una lista di prenotazioni in pending, ad esempio per mostrarle in una RecyclerView
    private List<Prenotazione> prenotazioniList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Assicurati di avere un layout activity_main.xml

        // Inizializza Retrofit
        apiService = ApiClient.getClient().create(PrenotazioniApiService.class);

        // Bottone per caricare le prenotazioni pending
        Button btnLoad = findViewById(R.id.btnLoadPrenotazioni);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPrenotazioni();
            }
        });

        // Bottone per aggiornare lo status della prenotazione
        // Qui usiamo l'ID 1 come esempio; in un caso reale, selezionerai l'elemento dalla lista
        Button btnAccept = findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus(1, "accepted"); // Cambia 1 con l'ID reale selezionato
            }
        });
        Button btnReject = findViewById(R.id.btnReject);
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus(1, "rejected"); // Cambia 1 con l'ID reale selezionato
            }
        });
    }

    private void loadPrenotazioni() {
        // Per esempio, usa la data odierna
        String data = "2023-04-15"; // Sostituisci con la data desiderata o ottienila dinamicamente
        Call<List<Prenotazione>> call = apiService.getPrenotazioni(data);
        call.enqueue(new Callback<List<Prenotazione>>() {
            @Override
            public void onResponse(Call<List<Prenotazione>> call, Response<List<Prenotazione>> response) {
                if (response.isSuccessful()) {
                    prenotazioniList = response.body();
                    Log.d("MainActivity", "Ricevute " + prenotazioniList.size() + " prenotazioni");
                    // Qui, aggiorna la tua UI (per esempio, aggiorna una RecyclerView)
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
                    // Dopo l'aggiornamento, potresti voler ricaricare la lista
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

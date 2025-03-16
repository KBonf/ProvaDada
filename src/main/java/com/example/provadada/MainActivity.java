package com.example.provadada;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.provadada.models.Prenotazione;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etData;
    private Button btnCarica;
    private ListView listViewPrenotazioni;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> prenotazioniList;

    private Retrofit retrofit;
    private PrenotazioniService prenotazioniService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Imposta il layout definito in activity_main.xml
        setContentView(R.layout.activity_main);

        // Collega gli elementi grafici
        etData = findViewById(R.id.etData);
        btnCarica = findViewById(R.id.btnCarica);
        listViewPrenotazioni = findViewById(R.id.listViewPrenotazioni);

        // Inizializza la lista e l'adapter per la ListView
        prenotazioniList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, prenotazioniList);
        listViewPrenotazioni.setAdapter(adapter);

        // Inizializza Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("10.0.2.2") // Sostituisci con l'URL del tuo backend
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        prenotazioniService = retrofit.create(PrenotazioniService.class);

        // Listener del pulsante per caricare le prenotazioni
        btnCarica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = etData.getText().toString();
                if (data.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Inserisci una data valida", Toast.LENGTH_SHORT).show();
                } else {
                    caricaPrenotazioni(data);
                }
            }
        });
    }

    // Metodo per caricare le prenotazioni tramite Retrofit
    private void caricaPrenotazioni(String data) {
        Log.d("MainActivity", "Richiedo prenotazioni per data: " + data);
        Call<List<Prenotazione>> call = prenotazioniService.getPrenotazioni(data);
        call.enqueue(new Callback<List<Prenotazione>>() {
            @Override
            public void onResponse(Call<List<Prenotazione>> call, Response<List<Prenotazione>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Prenotazione> responsePrenotazioni = response.body();
                    prenotazioniList.clear();
                    // Assumiamo che Prenotazione abbia un metodo toString() che restituisce le info desiderate
                    for (Prenotazione p : responsePrenotazioni) {
                        prenotazioniList.add(p.toString());
                    }
                    adapter.notifyDataSetChanged();
                    Log.d("MainActivity", "Ricevute " + responsePrenotazioni.size() + " prenotazioni");
                } else {
                    String errorResponse = "";
                    try {
                        if (response.errorBody() != null) {
                            errorResponse = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        errorResponse = "Errore durante la lettura dell'errorBody";
                    }
                    Toast.makeText(MainActivity.this, "Errore nella risposta del server", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Errore nella risposta: " + errorResponse);
                }
            }

            @Override
            public void onFailure(Call<List<Prenotazione>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Errore: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Errore chiamata Retrofit", t);
            }
        });
    }
}

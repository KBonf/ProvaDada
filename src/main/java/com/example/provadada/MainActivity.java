package com.example.provadada;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.provadada.models.Prenotazione;
import com.example.provadada.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etData;
    private Button btnCarica;
    private ListView listViewPrenotazioni;
    private PrenotazioneAdapter adapter;
    private ArrayList<Prenotazione> prenotazioniList;

    private Retrofit retrofit;
    private PrenotazioniService prenotazioniService;

    // Handler e Runnable per il polling (aggiornamento periodico)
    private Handler pollingHandler = new Handler();
    private Runnable pollingRunnable = new Runnable() {
        @Override
        public void run() {
            String data = etData.getText().toString();
            if (!TextUtils.isEmpty(data)) {
                caricaPrenotazioni(data);
            }
            // Ripeti ogni 60 secondi (60000 millisecondi)
            pollingHandler.postDelayed(this, 60000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Collega gli elementi grafici
        etData = findViewById(R.id.etData);
        btnCarica = findViewById(R.id.btnCarica);
        listViewPrenotazioni = findViewById(R.id.listViewPrenotazioni);

        // Imposta la data odierna nel formato "yyyy-MM-dd"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        etData.setText(sdf.format(new Date()));

        // Inizializza la lista e l'adapter personalizzato per la ListView
        prenotazioniList = new ArrayList<>();
        adapter = new PrenotazioneAdapter(this, prenotazioniList);
        listViewPrenotazioni.setAdapter(adapter);

        // Inizializza Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/wordpress/wp-json/dada/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        prenotazioniService = retrofit.create(PrenotazioniService.class);

        // Listener del pulsante per caricare le prenotazioni manualmente
        btnCarica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = etData.getText().toString();
                if (TextUtils.isEmpty(data)) {
                    Toast.makeText(MainActivity.this, "Inserisci una data valida", Toast.LENGTH_SHORT).show();
                } else {
                    caricaPrenotazioni(data);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Avvia il polling quando l'Activity è in primo piano
        pollingHandler.post(pollingRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Interrompi il polling per evitare perdite di memoria
        pollingHandler.removeCallbacks(pollingRunnable);
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
                    prenotazioniList.addAll(responsePrenotazioni);
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

    // Adapter personalizzato che infla il layout_prenotazione_row.xml per ogni riga
    private class PrenotazioneAdapter extends ArrayAdapter<Prenotazione> {

        // Set per tracciare gli elementi espansi (per ogni posizione)
        private HashSet<Integer> expandedPositions = new HashSet<>();

        public PrenotazioneAdapter(Context context, ArrayList<Prenotazione> prenotazioni) {
            super(context, 0, prenotazioni);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_prenotazione_row, parent, false);
            }

            Prenotazione prenotazione = getItem(position);

            // Riferimenti agli elementi del layout
            LinearLayout llRowHeader = convertView.findViewById(R.id.llRowHeader);
            final LinearLayout llActions = convertView.findViewById(R.id.llActions);

            TextView tvNome = convertView.findViewById(R.id.tvNome);
            TextView tvNumeroPersone = convertView.findViewById(R.id.tvNumeroPersone);
            TextView tvData = convertView.findViewById(R.id.tvData);
            TextView tvOrario = convertView.findViewById(R.id.tvOrario);
            TextView tvStatus = convertView.findViewById(R.id.tvStatus);
            TextView tvTavoli = convertView.findViewById(R.id.tvTavoli);

            // Imposta i valori
            tvNome.setText(prenotazione.getNome());
            tvNumeroPersone.setText(String.valueOf(prenotazione.getNumeroPersone()));
            tvData.setText(prenotazione.getData());
            tvOrario.setText(prenotazione.getOrario());
            tvStatus.setText(prenotazione.getStatus());
            tvTavoli.setText(prenotazione.getTavoli());

            // Imposta il colore dello sfondo dello status
            String status = prenotazione.getStatus();
            if(status.equalsIgnoreCase("pending")) {
                tvStatus.setBackgroundColor(Color.parseColor("#FFFFE0")); // giallino
            } else if(status.equalsIgnoreCase("accepted")) {
                tvStatus.setBackgroundColor(Color.parseColor("#90EE90")); // verdino
            } else if(status.equalsIgnoreCase("rejected")) {
                tvStatus.setBackgroundColor(Color.parseColor("#F08080")); // rosso chiaro
            } else {
                tvStatus.setBackgroundColor(Color.TRANSPARENT);
            }

            // Imposta lo stato iniziale dell'area azioni in base al set
            if(expandedPositions.contains(position)) {
                llActions.setVisibility(View.VISIBLE);
            } else {
                llActions.setVisibility(View.GONE);
            }

            // Imposta il click sul header per espandere/collassare
            llRowHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(expandedPositions.contains(position)) {
                        expandedPositions.remove(position);
                        llActions.setVisibility(View.GONE);
                    } else {
                        expandedPositions.add(position);
                        llActions.setVisibility(View.VISIBLE);
                    }
                }
            });

            // Listener per i pulsanti di azione
            Button btnAccetta = convertView.findViewById(R.id.btnAccetta);
            Button btnRifiuta = convertView.findViewById(R.id.btnRifiuta);

            btnAccetta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Prenotazione accettata: " + getItem(position).getNome(), Toast.LENGTH_SHORT).show();
                    // Qui puoi inviare una chiamata POST per aggiornare lo status
                }
            });

            btnRifiuta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Prenotazione rifiutata: " + getItem(position).getNome(), Toast.LENGTH_SHORT).show();
                    // Qui puoi inviare una chiamata POST per aggiornare lo status
                }
            });

            return convertView;
        }
    }
}

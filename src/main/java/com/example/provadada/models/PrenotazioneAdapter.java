package com.example.provadada.models;

import com.example.provadada.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class PrenotazioneAdapter extends ArrayAdapter<Prenotazione> {

    public PrenotazioneAdapter(Context context, List<Prenotazione> prenotazioni) {
        super(context, 0, prenotazioni);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_prenotazione_row, parent, false);
        }

        Prenotazione prenotazione = getItem(position);

        TextView tvNome = convertView.findViewById(R.id.tvNome);
        TextView tvNumeroPersone = convertView.findViewById(R.id.tvNumeroPersone);
        TextView tvData = convertView.findViewById(R.id.tvData);
        TextView tvOrario = convertView.findViewById(R.id.tvOrario);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);
        TextView tvTavoli = convertView.findViewById(R.id.tvTavoli);

        // Imposta i valori dei campi (assicurati che la classe Prenotazione abbia questi metodi)
        tvNome.setText(prenotazione.getNome());
        tvNumeroPersone.setText(String.valueOf(prenotazione.getNumeroPersone()));
        tvData.setText(prenotazione.getData());
        tvOrario.setText(prenotazione.getOrario());
        tvStatus.setText(prenotazione.getStatus());
        tvTavoli.setText(prenotazione.getTavoli());

        return convertView;
    }
}

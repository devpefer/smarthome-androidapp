package com.example.smarthome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlanoActivityAdapter extends RecyclerView.Adapter<PlanoActivityAdapter.ViewHolderDatos> {
    private ArrayList<AireAcondicionado> misSitios;

    public static class ViewHolderDatos extends RecyclerView.ViewHolder {

        public TextView tvNombreLoc;
        public TextView tvPower;
        public TextView tvSetTemp;
        public TextView tvFan;
        public TextView tvTurbo;
        public ViewHolderDatos(View v) {
            super(v);
            tvNombreLoc = v.findViewById(R.id.tvNombreDispositivo);
            tvPower = v.findViewById(R.id.tvPower);
            tvSetTemp = v.findViewById(R.id.tvSetTemp);
            tvFan = v.findViewById(R.id.tvFan);
            tvTurbo = v.findViewById(R.id.tvTurbo);
        }
    }

    public PlanoActivityAdapter(ArrayList misSitios) {
        this.misSitios = misSitios;
    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerplano, null, false);

        return new ViewHolderDatos(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {

        holder.tvNombreLoc.setText(misSitios.get(position).getMac());

        if(misSitios.get(position).getPow() == "0") {

            holder.tvPower.setText("Estado: Apagado");

        } else {

            holder.tvPower.setText("Estado: Encendido");

        }

        String temp = "Temp: " + misSitios.get(position).getSetTem();

        holder.tvSetTemp.setText(temp);
    }


    @Override
    public int getItemCount() {
        return misSitios.size();
    }
}

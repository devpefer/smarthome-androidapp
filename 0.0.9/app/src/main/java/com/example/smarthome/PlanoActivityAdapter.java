package com.example.smarthome;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlanoActivityAdapter extends RecyclerView.Adapter<PlanoActivityAdapter.ViewHolderDatos>  {
    private ArrayList<AireAcondicionado> misSitios;
    private Context context;



    public static class ViewHolderDatos extends RecyclerView.ViewHolder implements View.OnClickListener {

        public LinearLayout lytRecyclerPlano;
        public TextView tvNombreLoc;
        public TextView tvPower;
        public TextView tvSetTemp;
        public TextView tvFan;
        public TextView tvTurbo;
        public Context context;
        public ViewHolderDatos(Context context, View v) {
            super(v);
            lytRecyclerPlano = v.findViewById(R.id.lytRecyclerPlano);
            tvNombreLoc = v.findViewById(R.id.tvNombreDispositivo);
            tvPower = v.findViewById(R.id.tvPower);
            tvSetTemp = v.findViewById(R.id.tvSetTemp);
            tvFan = v.findViewById(R.id.tvFan);
            tvTurbo = v.findViewById(R.id.tvTurbo);

            lytRecyclerPlano.setOnClickListener(this);
            this.context = context;
        }

        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.lytRecyclerPlano) {

                Intent intent = new Intent(context, RemoteCtrlActivity.class);
                context.startActivity(intent);

            }

        }
    }

    public PlanoActivityAdapter(Context context, ArrayList<AireAcondicionado> misSitios) {
        this.misSitios = misSitios;
        this.context = context;

    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerplano, null, false);

        return new ViewHolderDatos(context,v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {

        holder.tvNombreLoc.setText(misSitios.get(position).getNombre());

        if(misSitios.get(position).getPow().equals("0")) {

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

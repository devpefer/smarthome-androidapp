package com.example.smarthome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BuscarActivityAdapter extends RecyclerView.Adapter<BuscarActivityAdapter.ViewHolderDatos> {
    private ArrayList misDispositivos;
    private MQTTUtils mqttUtils;



    public static class ViewHolderDatos extends RecyclerView.ViewHolder implements View.OnClickListener  {

        public TextView tvNombreDelDispositivo;
        public EditText etNombreDelDispositivo;
        public Button btnAnyadirDispositivo;
        public RelativeLayout lytAnyadirDispositivo;
        private MQTTUtils mqttUtils;
        public ViewHolderDatos(View v) {
            super(v);
            etNombreDelDispositivo = v.findViewById(R.id.etNombreDelDispositivo);
            tvNombreDelDispositivo = v.findViewById(R.id.tvNombreDispositivo);
            btnAnyadirDispositivo = v.findViewById(R.id.btnAnyadirDispositivo);
            lytAnyadirDispositivo = v.findViewById(R.id.lytNombreDispositivo);
            tvNombreDelDispositivo.setOnClickListener(this);
            btnAnyadirDispositivo.setOnClickListener(this);
            this.mqttUtils = mqttUtils;
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.tvNombreDispositivo) {

                if(lytAnyadirDispositivo.getVisibility() == View.GONE) {
                    lytAnyadirDispositivo.setVisibility(View.VISIBLE);
                }else {
                    lytAnyadirDispositivo.setVisibility(View.GONE);
                }
            }else if(v.getId() == R.id.btnAnyadirDispositivo) {

                if(!PlanoActivity.getMacs().contains(tvNombreDelDispositivo.toString().toLowerCase())) {
                    PlanoActivity.getMacs().add(tvNombreDelDispositivo.getText().toString().toLowerCase());
                }

            }
        }
    }

    public BuscarActivityAdapter(ArrayList misDispositivos) {
        this.misDispositivos = misDispositivos;
        this.mqttUtils = mqttUtils;
    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerbuscar, null, false);

        return new ViewHolderDatos(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {

        holder.tvNombreDelDispositivo.setText(misDispositivos.get(position).toString());

    }


    @Override
    public int getItemCount() {
        return misDispositivos.size();
    }


}

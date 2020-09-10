package com.example.smarthome;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlanoActivityAdapter extends RecyclerView.Adapter<PlanoActivityAdapter.ViewHolderDatos>  {
    private ArrayList<MQTTDevice> misSitios;
    private Context context;



    public static class ViewHolderDatos extends RecyclerView.ViewHolder implements View.OnClickListener {

        public LinearLayout lytRecyclerPlano;

        //AC Properties
        public TextView tvNombreLoc;
        public TextView tvPower;
        public TextView tvMode;
        public TextView tvSwing;
        public TextView tvHealth;
        public TextView tvDisplay;
        public TextView tvSetTemp;
        public TextView tvFan;
        public TextView tvTurbo;

        //Sonoff Properties
        public TextView tvEstado;

        public Context context;
        public ArrayList<MQTTDevice> arrayAires;
        public ViewHolderDatos(Context context, View v, ArrayList<MQTTDevice> arrayAires) {
            super(v);

            lytRecyclerPlano = v.findViewById(R.id.lytRecyclerPlano);
            tvNombreLoc = v.findViewById(R.id.tvNombreDispositivo);
            tvPower = v.findViewById(R.id.tvPower);
            tvMode = v.findViewById(R.id.tvMode);
            tvSwing = v.findViewById(R.id.tvSwing);
            tvHealth = v.findViewById(R.id.tvHealth);
            tvDisplay = v.findViewById(R.id.tvDisplay);
            tvSetTemp = v.findViewById(R.id.tvSetTemp);
            tvFan = v.findViewById(R.id.tvFan);
            tvTurbo = v.findViewById(R.id.tvTurbo);


            lytRecyclerPlano.setOnClickListener(this);
            this.context = context;
            this.arrayAires = arrayAires;
        }

        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.lytRecyclerPlano) {

                Intent intent = new Intent(context, RemoteCtrlActivity.class);
                int position = getAdapterPosition();
                intent.putExtra("mac",arrayAires.get(position).getMac());
                context.startActivity(intent);

            }

        }
    }

    public PlanoActivityAdapter(Context context, ArrayList<MQTTDevice> misSitios) {
        this.misSitios = misSitios;
        this.context = context;


    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerplano, null, false);

        return new ViewHolderDatos(context,v, misSitios);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {

        holder.tvNombreLoc.setText(misSitios.get(position).getName());

        if (misSitios.get(position) instanceof AireAcondicionado) {

            holder.tvPower.setVisibility(View.VISIBLE);
            holder.tvSetTemp.setVisibility(View.VISIBLE);
            holder.tvTurbo.setVisibility(View.VISIBLE);
            holder.tvFan.setVisibility(View.VISIBLE);
            holder.tvMode.setVisibility(View.VISIBLE);
            holder.tvSwing.setVisibility(View.VISIBLE);
            holder.tvHealth.setVisibility(View.VISIBLE);
            holder.tvDisplay.setVisibility(View.VISIBLE);

            if (misSitios.get(position).getPow().equals("0")) {

                holder.tvPower.setText("Estado: Apagado");

            } else if (misSitios.get(position).getPow().equals("1")) {

                holder.tvPower.setText("Estado: Encendido");

            }

            String temp = "Temp: " + misSitios.get(position).getSetTem();

            holder.tvSetTemp.setText(temp);

            String turbo = "Turbo: ";

            if (misSitios.get(position).getTur().equals("0")) {

                turbo = "Turbo: Apagado";

            } else if (misSitios.get(position).getTur().equals("1")) {

                turbo = "Turbo: Encendido";

            }

            holder.tvTurbo.setText(turbo);

            String fan = "Velocidad aire: ";

            if (misSitios.get(position).getWdSpd().equals("0")) {

                fan = "Velocidad aire: Auto";

            } else if (misSitios.get(position).getWdSpd().equals("1")) {

                fan = "Velocidad aire: Lenta";

            } else if (misSitios.get(position).getWdSpd().equals("3")) {

                fan = "Velocidad aire: Media";

            } else if (misSitios.get(position).getWdSpd().equals("5")) {

                fan = "Velocidad aire: Rápida";

            }

            holder.tvFan.setText(fan);

            String mode = "Modo: ";

            if (misSitios.get(position).getMod().equals("0")) {

                mode = "Modo: Recircular aire";

            } else if (misSitios.get(position).getMod().equals("1")) {

                mode = "Modo: Frío";

            } else if (misSitios.get(position).getMod().equals("2")) {

                mode = "Modo: Húmedo";

            } else if (misSitios.get(position).getMod().equals("3")) {

                mode = "Modo: Auto";

            } else if (misSitios.get(position).getMod().equals("5")) {

                mode = "Modo: Calor";

            }

            holder.tvMode.setText(mode);

            String swing = "Mov. aspas: ";

            if (misSitios.get(position).getSwUpDn().equals("0")) {

                swing = "Mov. aspas: No";

            } else if (misSitios.get(position).getSwUpDn().equals("1")) {

                swing = "Mov. aspas: Sí";
            }
            holder.tvSwing.setText(swing);

            String health = "Health: " + misSitios.get(position).getHealth();

            if (misSitios.get(position).getHealth().equals("0")) {

                health = "Health: No";

            } else if (misSitios.get(position).getHealth().equals("1")) {

                health = "Health: Sí";
            }
            holder.tvHealth.setText(health);

            String display = "Pantalla: ";

            if (misSitios.get(position).getLig().equals("0")) {

                display = "Pantalla: Apagada";

            } else if (misSitios.get(position).getLig().equals("1")) {

                display = "Pantalla: Encendida";
            }

            holder.tvDisplay.setText(display);

        }

        else if (misSitios.get(position) instanceof Sonoff) {



        }
    }


    @Override
    public int getItemCount() {
        return misSitios.size();
    }

}

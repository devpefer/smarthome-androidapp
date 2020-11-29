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

public class PlayerActivityAdapter extends RecyclerView.Adapter<PlayerActivityAdapter.ViewHolderDatos> {
    private ArrayList archivos;
    private Context context;




    public static class ViewHolderDatos extends RecyclerView.ViewHolder implements View.OnClickListener  {

        public TextView tvNombreArchivo;
        public LinearLayout lytPlayer;
        public Context context;

        public ViewHolderDatos(Context context, View v) {
            super(v);
            this.context = context;
            tvNombreArchivo = v.findViewById(R.id.tvNombreArchivo);
            lytPlayer = v.findViewById(R.id.lytRecyclerPlayer);
            lytPlayer.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.lytRecyclerPlayer) {

                for(int i = 0; i < PlayerActivity.getArchivos().size(); i++){

                    if(PlayerActivity.getArchivos().get(i).equals(tvNombreArchivo.getText().toString())) {
                        DeviceListActivity.getMqttAndroidClient().publishMessage("pyrpi/youtube/reproducir", tvNombreArchivo.getText().toString());
                        Log.i("PLAYER",tvNombreArchivo.getText().toString() + PlayerActivity.getArchivos().get(i));
                    }


                }



            }
        }
    }

    public PlayerActivityAdapter(Context context,ArrayList archivos) {
        this.archivos = archivos;
        this.context = context;
    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerplayer, null, false);

        return new ViewHolderDatos(context,v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {

        holder.tvNombreArchivo.setText(archivos.get(position).toString());

    }


    @Override
    public int getItemCount() {
        return archivos.size();
    }


}

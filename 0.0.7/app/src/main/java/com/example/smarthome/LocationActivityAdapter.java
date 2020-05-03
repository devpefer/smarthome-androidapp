package com.example.smarthome;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationActivityAdapter extends RecyclerView.Adapter<LocationActivityAdapter.ViewHolderDatos> {
    private ArrayList misSitios;
    private Context context;




    public static class ViewHolderDatos extends RecyclerView.ViewHolder implements View.OnClickListener  {

        public TextView tvNombreLocation;
        public Context context;

        public ViewHolderDatos(Context context, View v) {
            super(v);
            this.context = context;
            tvNombreLocation = v.findViewById(R.id.tvNombreLocation);
            tvNombreLocation.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.tvNombreLocation) {

                Intent intent = new Intent(context, PlanoActivity.class);
                intent.putExtra("nombrelugar",tvNombreLocation.getText());

                for(int i = 0; i < LocationActivity.getLocations().size(); i++){

                    if(LocationActivity.getLocations().get(i).getNombre().equals(tvNombreLocation.getText().toString())) {

                        intent.putExtra("serverURI", "tcp://" + LocationActivity.getLocations().get(i).getServerURI() + ":1883");

                    }
                }

                context.startActivity(intent);

            }
        }
    }

    public LocationActivityAdapter(Context context,ArrayList misSitios) {
        this.misSitios = misSitios;
        this.context = context;
    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerlocation, null, false);

        return new ViewHolderDatos(context,v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {

        holder.tvNombreLocation.setText(misSitios.get(position).toString());

    }


    @Override
    public int getItemCount() {
        return misSitios.size();
    }


}

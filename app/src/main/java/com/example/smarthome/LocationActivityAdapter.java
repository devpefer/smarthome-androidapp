package com.example.smarthome;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationActivityAdapter extends RecyclerView.Adapter<LocationActivityAdapter.ViewHolderDatos> {
    private ArrayList misSitios;
    private Context context;




    public static class ViewHolderDatos extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, PopupMenu.OnMenuItemClickListener  {

        public TextView tvNombreLocation;
        public LinearLayout lytLocation;
        public Context context;

        public ViewHolderDatos(Context context, View v) {
            super(v);
            this.context = context;
            tvNombreLocation = v.findViewById(R.id.tvNombreLocation);
            lytLocation = v.findViewById(R.id.lytRecyclerLocation);
            lytLocation.setOnClickListener(this);
            lytLocation.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.lytRecyclerLocation) {

                Intent intent = new Intent(context, DeviceListActivity.class);


                for(int i = 0; i < LocationActivity.getLocations().size(); i++){

                    if(LocationActivity.getLocations().get(i).getNombre().equals(tvNombreLocation.getText().toString())) {

                        intent.putExtra("nombreLugar",tvNombreLocation.getText());
                        intent.putExtra("serverURI",LocationActivity.getLocations().get(i).getServerURI());
                        intent.putExtra("clientID",LocationActivity.getLocations().get(i).getClientId());
                        context.startActivity(intent);
                    }
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == R.id.lytRecyclerLocation) {
                showPopupEditLocation(v);
            }
            return true;
        }

        public void showPopupEditLocation(View v) {
            PopupMenu popup = new PopupMenu(context, v);
            popup.setOnMenuItemClickListener(this);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_locationproperties, popup.getMenu());
            popup.show();

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.menuLocationEliminar:
                    for(int i = 0; i < LocationActivity.getLocations().size(); i++){

                        if(LocationActivity.getLocations().get(i).getNombre().equals(tvNombreLocation.getText().toString())) {
                            LocationActivity.deleteLocation(LocationActivity.getLocations().get(i));
                        }
                    }
                    break;

            }

            return true;
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

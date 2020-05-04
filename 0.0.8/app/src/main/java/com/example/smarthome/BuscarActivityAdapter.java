package com.example.smarthome;

import android.content.Context;
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
    private Context context;


    public static class ViewHolderDatos extends RecyclerView.ViewHolder implements View.OnClickListener  {

        public TextView tvNombreDelDispositivo;
        public EditText etNombreDelDispositivo;
        public Button btnAnyadirDispositivo;
        public RelativeLayout lytAnyadirDispositivo;
        public Context context;
        public ViewHolderDatos(Context context, View v) {
            super(v);
            etNombreDelDispositivo = v.findViewById(R.id.etNombreDelDispositivo);
            tvNombreDelDispositivo = v.findViewById(R.id.tvNombreDispositivo);
            btnAnyadirDispositivo = v.findViewById(R.id.btnAnyadirDispositivo);
            lytAnyadirDispositivo = v.findViewById(R.id.lytNombreDispositivo);
            tvNombreDelDispositivo.setOnClickListener(this);
            btnAnyadirDispositivo.setOnClickListener(this);
            this.context = context;
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

                for(int i = 0; i < PlanoActivity.getAircoParams().size();i++) {

                    /*
                    if (!PlanoActivity.getAircoParams().get(i).getMac().contains(tvNombreDelDispositivo.toString())) {
                        AireAcondicionado aireAcondicionado = new AireAcondicionado();
                        PlanoActivity.getAircoParams().add(aireAcondicionado);
                        ((BuscarActivity) context).finish();
                    }

                     */
                }

            }

        }
    }

    public BuscarActivityAdapter(Context context, ArrayList misDispositivos) {
        this.misDispositivos = misDispositivos;
        this.context = context;
    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerbuscar, null, false);

        return new ViewHolderDatos(context,v);
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

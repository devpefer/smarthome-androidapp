package com.example.smarthome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class BuscarActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TOPIC = "ewpe-smart/#";
    private RelativeLayout lytAnyadirDispositivo;
    private static ArrayList<String> macs = new ArrayList<>();

    private static Button btnAnyadirTasmota;
    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_buscar);

        lytAnyadirDispositivo = findViewById(R.id.lytNombreDispositivo);

        btnAnyadirTasmota = findViewById(R.id.btnAnyadirTasmota);
        btnAnyadirTasmota.setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerListaDispositivos);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new BuscarActivityAdapter(this, macs);
        recyclerView.setAdapter(mAdapter);

    }

    protected void onResume(){

        super.onResume();

    }

    protected void onPause() {

        super.onPause();


    }

    public static ArrayList<String> getMacs() {
        return macs;
    }

    public static void setMacs(ArrayList<String> macs) {
        BuscarActivity.macs = macs;
    }

    public static RecyclerView.Adapter getmAdapter() {
        return mAdapter;
    }

    public static void setmAdapter(RecyclerView.Adapter mAdapter) {
        BuscarActivity.mAdapter = mAdapter;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnAnyadirTasmota:
                Sonoff sonoff = new Sonoff();
                askTasmotaName(sonoff);
                PlanoActivity.anyadirDispositivo(sonoff,sonoff.getMac(),sonoff.getMac());

                break;

        }
    }

    private void askTasmotaName(final MQTTDevice sonoff) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nombre del dispositivo");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sonoff.setMac(input.getText().toString());
                sonoff.setName(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}

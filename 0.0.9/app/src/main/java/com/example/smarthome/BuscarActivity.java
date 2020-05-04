package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class BuscarActivity extends AppCompatActivity {

    private final String TOPIC = "ewpe-smart/#";
    private RelativeLayout lytAnyadirDispositivo;

    private String serverURI;
    private String clientId;

    private MQTTUtils mqttClient;
    private static ArrayList<String> macs;

    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_buscar);

        serverURI = getIntent().getExtras().getString("serverURI");
        clientId = getIntent().getExtras().getString("clientId");

        mqttClient = new MQTTUtils(getApplicationContext(),serverURI,clientId);
        mqttClient.conectar(getApplicationContext(), TOPIC);
        lytAnyadirDispositivo = findViewById(R.id.lytNombreDispositivo);


        macs = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerListaDispositivos);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new BuscarActivityAdapter(this, macs);
        recyclerView.setAdapter(mAdapter);

    }

    protected void onPause() {

        super.onPause();

        mqttClient.desconectar();

    }

    public static ArrayList<String> getMacs() {
        return macs;
    }

    public static void setMacs(ArrayList<String> macs) {
        BuscarActivity.macs = macs;
    }

    public static void refrescarLista(String mac) {

        if(!macs.contains(mac)) {
            macs.add(mac);
        }

        mAdapter.notifyDataSetChanged();

    }



}

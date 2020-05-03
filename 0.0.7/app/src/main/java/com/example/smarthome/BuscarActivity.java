package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class BuscarActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnBuscarDispositivo;
    private RelativeLayout lytAnyadirDispositivo;

    private String serverURI;
    private String clientId;

    private MQTTUtils mqttClient;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
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
        mqttClient.conectar(getApplicationContext());
        btnBuscarDispositivo = findViewById(R.id.btnBuscarDispositivos);
        lytAnyadirDispositivo = findViewById(R.id.lytNombreDispositivo);

        btnBuscarDispositivo.setOnClickListener(this);


        recyclerView = findViewById(R.id.recyclerListaDispositivos);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new BuscarActivityAdapter(this,mqttClient.getMacs());
        recyclerView.setAdapter(mAdapter);

    }

    protected void onPause() {

        super.onPause();

        mqttClient.desconectar();

    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btnBuscarDispositivos) {

            mAdapter = new BuscarActivityAdapter(this,mqttClient.getMacs());
            recyclerView.setAdapter(mAdapter);
        }

    }

}

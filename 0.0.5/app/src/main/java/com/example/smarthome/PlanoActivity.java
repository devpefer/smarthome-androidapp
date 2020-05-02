package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;


import java.util.ArrayList;

public class PlanoActivity extends AppCompatActivity implements View.OnClickListener {

    private String serverURI = "tcp://192.168.0.106:1883";
    private String clientId = "";
    private final String TOPIC = "ewpe-smart/#";
    private MQTTUtils mqttAndroidClient;

    private AireAcondicionado aircoParams;

    private static String aireHab1 = "";
    private static ArrayList<String> macs;

    private static String suscripcion;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList datos = new ArrayList();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_plano);

        mqttAndroidClient = new MQTTUtils(this,serverURI,clientId);
        mqttAndroidClient.conectar(getApplicationContext());

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new PlanoActivityAdapter(datos);
        recyclerView.setAdapter(mAdapter);



        for (int i = 0; i < 50; i++) {

            datos.add("Aire " + i+1);

        }



    }

    @Override
    protected void onResume() {
        super.onResume();

        mqttAndroidClient.conectar(getApplicationContext());
    }

    protected void onPause() {
        super.onPause();

        mqttAndroidClient.desconectar();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.layout.recyclerplano:
                Intent intent = new Intent(this,RemoteCtrlActivity.class);
                intent.putExtra("serverURI",serverURI);
                intent.putExtra("clientId",clientId);
                startActivity(intent);

                break;

        }

    }


}

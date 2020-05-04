package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;


import java.util.ArrayList;

public class PlanoActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private String nombrelugar;
    private String serverURI;
    private String clientId = "";
    private final String TOPIC = "ewpe-smart/#";
    private MQTTUtils mqttAndroidClient;

    private static String aireHab1 = "";
    private static ArrayList<AireAcondicionado> aircoParams;

    private static String suscripcion;

    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList datos = new ArrayList();

    private TextView tvMisSitios;
    private TextView tvNombreDispositivo;
    private TextView tvPower;
    private TextView tvSetTemp;
    private TextView tvFan;
    private TextView tvTurbo;

    private ImageButton ibAnyadirDispositivo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_plano);

        nombrelugar = getIntent().getExtras().getString("nombrelugar");
        serverURI = getIntent().getExtras().getString("serverURI");

        Log.i("IP",serverURI);

        tvMisSitios = findViewById(R.id.tvMisSitios);
        tvMisSitios.setText("Mis dispositivos en " + nombrelugar);

        tvNombreDispositivo = findViewById(R.id.tvNombreDispositivo);
        tvPower = findViewById(R.id.tvPower);
        tvSetTemp = findViewById(R.id.tvSetTemp);
        tvFan = findViewById(R.id.tvFan);
        tvTurbo = findViewById(R.id.tvTurbo);

        ibAnyadirDispositivo = findViewById(R.id.ibAnyadirDispositivo);

        ibAnyadirDispositivo.setOnClickListener(this);

        mqttAndroidClient = new MQTTUtils(this,serverURI,clientId);
        mqttAndroidClient.conectar(getApplicationContext(),TOPIC);

        aircoParams = new ArrayList<>();

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new PlanoActivityAdapter(aircoParams);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mqttAndroidClient.conectar(getApplicationContext(),TOPIC);
        mAdapter = new PlanoActivityAdapter(aircoParams);
        recyclerView.setAdapter(mAdapter);
    }

    protected void onPause() {
        super.onPause();

        mqttAndroidClient.desconectar();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ibAnyadirDispositivo:

                showPopupRemote(v);
                break;


        }

    }

    public void showPopupRemote (View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_remote_ctrl, popup.getMenu());
        popup.show();

    }

    @Override
    public boolean onMenuItemClick (MenuItem item){
        if (item.getItemId() == R.id.menuBuscar) {
            Intent intent = new Intent(PlanoActivity.this, BuscarActivity.class);
            intent.putExtra("serverURI",serverURI);
            intent.putExtra("clientId", clientId);
            startActivity(intent);
        }

        return true;
    }

    public static ArrayList<AireAcondicionado> getAircoParams() {
        return PlanoActivity.aircoParams;
    }

    public static void setAircoParams(ArrayList<AireAcondicionado> aircoParams) {
        PlanoActivity.aircoParams = aircoParams;
    }

    public static String getAireHab1() {
        return aireHab1;
    }

    public static void setAireHab1(String aireHab1) {
        PlanoActivity.aireHab1 = aireHab1;
    }

    public static void refrescarLista() {

        mAdapter = new PlanoActivityAdapter(aircoParams);
        recyclerView.setAdapter(mAdapter);

    }
}

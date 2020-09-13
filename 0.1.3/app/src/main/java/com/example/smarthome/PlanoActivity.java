package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlanoActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {


    private String serverURI;
    private String clientID;

    private static MQTTUtils mqttAndroidClient;

    private static String nombrelugar;


    private SharedPreferences prefs;
    private Gson gson;

    private static ArrayList<MQTTDevice> devices;
    private static ArrayList<AireAcondicionado> AirCoParams;
    private static ArrayList<Sonoff> SonoffParams;

    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

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

        serverURI = getIntent().getExtras().getString("serverURI");
        clientID = getIntent().getExtras().getString("clientID");

        nombrelugar = getIntent().getExtras().getString("nombreLugar");

        devices = new ArrayList<>();
        AirCoParams = new ArrayList<>();
        SonoffParams = new ArrayList<>();

        mqttAndroidClient = new MQTTUtils(getApplicationContext(), serverURI, "");
        mqttAndroidClient.conectar("ewpe-smart/#");

        tvMisSitios = findViewById(R.id.tvMisSitios);
        tvMisSitios.setText("Mis dispositivos en " + nombrelugar);

        tvNombreDispositivo = findViewById(R.id.tvNombreDispositivo);
        tvPower = findViewById(R.id.tvPower);
        tvSetTemp = findViewById(R.id.tvSetTemp);
        tvFan = findViewById(R.id.tvFan);
        tvTurbo = findViewById(R.id.tvTurbo);

        ibAnyadirDispositivo = findViewById(R.id.ibAnyadirDispositivo);

        ibAnyadirDispositivo.setOnClickListener(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        gson = new Gson();
        String objetosAires = prefs.getString("listaAires"+nombrelugar,null);
        String objetosSonoff = prefs.getString("listaSonoff"+nombrelugar,null);

        Type listTypeAires = new TypeToken<ArrayList<AireAcondicionado>>(){}.getType();
        Type listTypeSonoff = new TypeToken<ArrayList<Sonoff>>(){}.getType();

        AirCoParams = gson.fromJson(objetosAires, listTypeAires);
        SonoffParams = gson.fromJson(objetosSonoff, listTypeSonoff);

        if (!(AirCoParams == null) && (!AirCoParams.isEmpty())) {
            devices.addAll(AirCoParams);

        }

        if (!(SonoffParams == null) && (!SonoffParams.isEmpty())) {
            devices.addAll(SonoffParams);

        }
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new PlanoActivityAdapter(this, devices);
        recyclerView.setAdapter(mAdapter);

    }

    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    protected void onPause() {
        super.onPause();

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
            intent.putExtra("nombreLugar", nombrelugar);
            startActivity(intent);
        }

        return true;
    }

    public static ArrayList<MQTTDevice> getDevices() {
        return PlanoActivity.devices;
    }

    public static RecyclerView.Adapter getmAdapter() {
        return mAdapter;
    }

    public static void anyadirDispositivo(MQTTDevice device, String mac, String nombre) {

        if(device instanceof AireAcondicionado) {

            if(AirCoParams == null) {
                AirCoParams = new ArrayList<>();
            }
            AirCoParams.add((AireAcondicionado) device);

            SharedPreferences prefs;

            Gson gson = new Gson();

            String arrayListDisp = gson.toJson(AirCoParams);


            prefs = PreferenceManager.getDefaultSharedPreferences(LocationActivity.getContext());

            SharedPreferences.Editor prefsEditor = prefs.edit();

            prefsEditor.putString("listaAires"+nombrelugar,arrayListDisp);
            prefsEditor.apply();

            Log.i("LISTAAIRE",mac + nombre + prefs.getString("listaAires"+nombrelugar,null));

        } else if (device instanceof Sonoff) {

            if(SonoffParams == null) {
                SonoffParams = new ArrayList<>();
            }

            SonoffParams.add((Sonoff) device);

            SharedPreferences prefs;

            Gson gson = new Gson();

            String arrayListDisp = gson.toJson(SonoffParams);


            prefs = PreferenceManager.getDefaultSharedPreferences(LocationActivity.getContext());

            SharedPreferences.Editor prefsEditor = prefs.edit();

            prefsEditor.putString("listaSonoff"+nombrelugar,arrayListDisp);
            prefsEditor.apply();

            Log.i("LISTASONOFF",mac + nombre + prefs.getString("listaSonoff"+nombrelugar,null));

        }
        devices.add(device);

        mAdapter.notifyDataSetChanged();



    }

    public static MQTTUtils getMqttAndroidClient() {
        return mqttAndroidClient;
    }

}

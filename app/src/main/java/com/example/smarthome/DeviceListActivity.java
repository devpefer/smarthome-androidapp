package com.example.smarthome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthome.ewpe.AireAcondicionado;
import com.example.smarthome.raspberrypi.RPi;
import com.example.smarthome.tasmota.IRAireAcondicionado;
import com.example.smarthome.tasmota.Sonoff;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DeviceListActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {


    private String serverURI;
    private String clientID;

    private static MQTTUtils mqttAndroidClient;

    private static String nombrelugar;


    private SharedPreferences prefs;
    private Gson gson;

    private static ArrayList<MQTTDevice> devices;
    private static ArrayList<AireAcondicionado> AirCoParams;
    private static ArrayList<Sonoff> SonoffParams;
    private static ArrayList<IRAireAcondicionado> irAirCoParams;
    private static ArrayList<RPi> RPiParams;
    private static Context context;

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
        setContentView(R.layout.activity_devicelist);

        serverURI = getIntent().getExtras().getString("serverURI");
        clientID = getIntent().getExtras().getString("clientID");

        nombrelugar = getIntent().getExtras().getString("nombreLugar");

        devices = new ArrayList<>();
        AirCoParams = new ArrayList<>();
        SonoffParams = new ArrayList<>();
        irAirCoParams = new ArrayList<>();
        RPiParams = new ArrayList<>();

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
        String objetosIRAires = prefs.getString("listaIRAires"+nombrelugar,null);
        String objetosRPi = prefs.getString("listaRPi"+nombrelugar,null);

        Type listTypeAires = new TypeToken<ArrayList<AireAcondicionado>>(){}.getType();
        Type listTypeSonoff = new TypeToken<ArrayList<Sonoff>>(){}.getType();
        Type listTypeIRAires = new TypeToken<ArrayList<IRAireAcondicionado>>(){}.getType();
        Type listTypeRPi = new TypeToken<ArrayList<RPi>>(){}.getType();

        AirCoParams = gson.fromJson(objetosAires, listTypeAires);
        SonoffParams = gson.fromJson(objetosSonoff, listTypeSonoff);
        irAirCoParams = gson.fromJson(objetosIRAires, listTypeIRAires);
        RPiParams = gson.fromJson(objetosRPi, listTypeRPi);

        if (!(AirCoParams == null) && (!AirCoParams.isEmpty())) {
            devices.addAll(AirCoParams);

        }

        if (!(SonoffParams == null) && (!SonoffParams.isEmpty())) {
            devices.addAll(SonoffParams);

        }

        if (!(irAirCoParams == null) && (!irAirCoParams.isEmpty())) {
            devices.addAll(irAirCoParams);

        }

        if (!(RPiParams == null) && (!RPiParams.isEmpty())) {
            devices.addAll(RPiParams);

        }

        mqttAndroidClient = new MQTTUtils(getApplicationContext(), serverURI, "");
        mqttAndroidClient.conectar();

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new DeviceListActivityAdapter(this, devices);
        recyclerView.setAdapter(mAdapter);

        context = getApplicationContext();

    }

    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mqttAndroidClient.getMqttAndroidClient().isConnected()) {
            mqttAndroidClient.conectar();
        }

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
            Intent intent = new Intent(DeviceListActivity.this, BuscarActivity.class);
            intent.putExtra("nombreLugar", nombrelugar);
            startActivity(intent);
        }

        return true;
    }

    public static ArrayList<MQTTDevice> getDevices() {
        return DeviceListActivity.devices;
    }

    public static ArrayList<Sonoff> getSonoffParams() {
        return SonoffParams;
    }

    public static void setSonoffParams(ArrayList<Sonoff> sonoffParams) {
        SonoffParams = sonoffParams;
    }

    public static ArrayList<RPi> getRPiParams() {
        return RPiParams;
    }

    public static void setRPiParams(ArrayList<RPi> RPiParams) {
        DeviceListActivity.RPiParams = RPiParams;
    }

    public static ArrayList<AireAcondicionado> getAirCoParams() {
        return AirCoParams;
    }

    public static ArrayList<IRAireAcondicionado> getIrAirCoParams() {
        return irAirCoParams;
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

        } else if (device instanceof IRAireAcondicionado) {

            if(irAirCoParams == null) {
                irAirCoParams = new ArrayList<>();
            }

            irAirCoParams.add((IRAireAcondicionado) device);

            SharedPreferences prefs;

            Gson gson = new Gson();

            String arrayListDisp = gson.toJson(irAirCoParams);

            prefs = PreferenceManager.getDefaultSharedPreferences(LocationActivity.getContext());

            SharedPreferences.Editor prefsEditor = prefs.edit();

            prefsEditor.putString("listaIRAires"+nombrelugar,arrayListDisp);
            prefsEditor.apply();

        } else if (device instanceof RPi) {

            if(RPiParams == null) {
                RPiParams = new ArrayList<>();
            }

            RPiParams.add((RPi) device);

            SharedPreferences prefs;

            Gson gson = new Gson();

            String arrayListDisp = gson.toJson(RPiParams);


            prefs = PreferenceManager.getDefaultSharedPreferences(LocationActivity.getContext());

            SharedPreferences.Editor prefsEditor = prefs.edit();

            prefsEditor.putString("listaRPi"+nombrelugar,arrayListDisp);
            prefsEditor.apply();

        }

        devices.add(device);

        mAdapter.notifyDataSetChanged();



    }

    public static void deleteDevice(MQTTDevice mqttDevice) {

        SharedPreferences prefs;

        Gson gson = new Gson();

        prefs = PreferenceManager.getDefaultSharedPreferences(DeviceListActivity.getContext());

        SharedPreferences.Editor prefsEditor = prefs.edit();

        if (mqttDevice instanceof AireAcondicionado) {

            AirCoParams.remove(mqttDevice);
            String arrayListAires = gson.toJson(getAirCoParams());
            prefsEditor.putString("listaAires"+nombrelugar,arrayListAires);

        } else if (mqttDevice instanceof RPi) {

            RPiParams.remove(mqttDevice);
            String arrayListRPi = gson.toJson(getRPiParams());
            prefsEditor.putString("listaRPi"+nombrelugar,arrayListRPi);

        } else if (mqttDevice instanceof Sonoff) {

            SonoffParams.remove(mqttDevice);
            String arrayListSonoff = gson.toJson(getSonoffParams());
            prefsEditor.putString("listaSonoff"+nombrelugar,arrayListSonoff);

        } else if (mqttDevice instanceof IRAireAcondicionado) {

            irAirCoParams.remove(mqttDevice);
            String arrayListIRAires = gson.toJson(getIrAirCoParams());
            prefsEditor.putString("listaAires"+nombrelugar,arrayListIRAires);

        }

        prefsEditor.apply();

        devices.remove(mqttDevice);
        mAdapter.notifyDataSetChanged();
    }

    public static Context getContext() {
        return context;
    }

    public static MQTTUtils getMqttAndroidClient() {
        return mqttAndroidClient;
    }

}

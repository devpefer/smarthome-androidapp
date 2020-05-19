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
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PlanoActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private String serverURI;
    private String clientID;

    private static MQTTUtils mqttAndroidClient;

    private static String nombrelugar;


    private SharedPreferences prefs;
    private Gson gson;

    private static String aireHab1 = "";
    private static ArrayList<AireAcondicionado> aircoParams;

    private static String suscripcion;

    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static ArrayList<String> macsyNombres;

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

        macsyNombres = new ArrayList<>();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        gson = new Gson();
        String objetos = prefs.getString("listaDispositivos"+nombrelugar,"");

        Type listType = new TypeToken<ArrayList<AireAcondicionado>>(){}.getType();

        aircoParams = gson.fromJson(objetos, listType);


        if(aircoParams == null ) {

            aircoParams = new ArrayList<>();

            Gson gson = new Gson();

            String arrayListLocations = gson.toJson(aircoParams);


            prefs = PreferenceManager.getDefaultSharedPreferences(this);

            SharedPreferences.Editor prefsEditor = prefs.edit();

            prefsEditor.putString("listaDispositivos"+nombrelugar,arrayListLocations);
            prefsEditor.apply();

        }


        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new PlanoActivityAdapter(this, aircoParams);
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

    public static ArrayList<AireAcondicionado> getAircoParams() {
        return PlanoActivity.aircoParams;
    }

    public static RecyclerView.Adapter getmAdapter() {
        return mAdapter;
    }

    public static void refrescarLista(AireAcondicionado aireAcondicionado) {


        for (int i = 0; i < aircoParams.size(); i++) {

            if (aircoParams.get(i).getMac().equals(aireAcondicionado.getMac())) {

                aircoParams.get(i).setAir(aireAcondicionado.getAir());
                aircoParams.get(i).setBlo(aireAcondicionado.getBlo());
                aircoParams.get(i).setHealth(aireAcondicionado.getHealth());
                aircoParams.get(i).setLig(aireAcondicionado.getLig());
                aircoParams.get(i).setMod(aireAcondicionado.getMod());
                aircoParams.get(i).setPow(aireAcondicionado.getPow());
                aircoParams.get(i).setQuiet(aireAcondicionado.getQuiet());
                aircoParams.get(i).setSetTem(aireAcondicionado.getSetTem());
                aircoParams.get(i).setSvSt(aireAcondicionado.getSvSt());
                aircoParams.get(i).setSwhSlp(aireAcondicionado.getSwhSlp());
                aircoParams.get(i).setSwingLfRig(aireAcondicionado.getSwingLfRig());
                aircoParams.get(i).setSwUpDn(aireAcondicionado.getSwUpDn());
                aircoParams.get(i).setTemRec(aireAcondicionado.getTemRec());
                aircoParams.get(i).setTemUn(aireAcondicionado.getTemUn());
                aircoParams.get(i).setTur(aireAcondicionado.getTur());
                aircoParams.get(i).setWdSpd(aireAcondicionado.getWdSpd());

            }
        }



        mAdapter.notifyDataSetChanged();

    }

    public static void anyadirDispositivo(TextView mac, TextView nombre) {

        AireAcondicionado aireAcondicionado = new AireAcondicionado();
        aireAcondicionado.setMac(mac.getText().toString());
        aireAcondicionado.setNombre(nombre.getText().toString());
        aireAcondicionado.setAir("");
        aireAcondicionado.setBlo("");
        aireAcondicionado.setHealth("");
        aireAcondicionado.setLig("");
        aireAcondicionado.setMod("");
        aireAcondicionado.setPow("");
        aireAcondicionado.setQuiet("");
        aireAcondicionado.setSetTem("");
        aireAcondicionado.setSvSt("");
        aireAcondicionado.setSwhSlp("");
        aireAcondicionado.setSwingLfRig("");
        aireAcondicionado.setSwUpDn("");
        aireAcondicionado.setTemRec("");
        aireAcondicionado.setTemUn("");
        aireAcondicionado.setTur("");
        aireAcondicionado.setWdSpd("");

        aircoParams.add(aireAcondicionado);

        mAdapter.notifyDataSetChanged();

        SharedPreferences prefs;

        Gson gson = new Gson();

        String arrayListDisp = gson.toJson(aircoParams);


        prefs = PreferenceManager.getDefaultSharedPreferences(LocationActivity.getContext());

        SharedPreferences.Editor prefsEditor = prefs.edit();

        prefsEditor.putString("listaDispositivos"+nombrelugar,arrayListDisp);
        prefsEditor.apply();

    }

    public static MQTTUtils getMqttAndroidClient() {
        return mqttAndroidClient;
    }

}

package com.example.smarthome;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthome.raspberrypi.RPi;
import com.example.smarthome.tasmota.IRAireAcondicionado;
import com.example.smarthome.tasmota.IRRemote;
import com.example.smarthome.tasmota.Sonoff;

import java.util.ArrayList;

public class BuscarActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout lytAnyadirDispositivo;
    private static ArrayList<String> macs = new ArrayList<>();
    private static Spinner lista;
    private static Button btnAnyadirTasmota;
    private static EditText etTasmota;
    private static Button btnAnyadirRPi;
    private static EditText etRPi;
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

        lista = findViewById(R.id.spnTasmota);

        btnAnyadirTasmota = findViewById(R.id.btnAnyadirTasmota);
        btnAnyadirTasmota.setOnClickListener(this);
        btnAnyadirRPi = findViewById(R.id.btnAnyadirRPi);
        btnAnyadirRPi.setOnClickListener(this);

        etTasmota = findViewById(R.id.etTasmota);
        etRPi = findViewById(R.id.etRPi);

        spinnerTamota();

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

                if (lista.getSelectedItem().toString().toLowerCase().equals("sonoff")) {

                    Sonoff sonoff = new Sonoff();
                    sonoff.setMac(etTasmota.getText().toString());
                    sonoff.setName(etTasmota.getText().toString());
                    DeviceListActivity.anyadirDispositivo(sonoff, sonoff.getMac(), sonoff.getName());

                } else if (lista.getSelectedItem().toString().toLowerCase().equals("ir ac")) {

                    IRAireAcondicionado aireAcondicionado = new IRAireAcondicionado();
                    aireAcondicionado.setMac(etTasmota.getText().toString());
                    aireAcondicionado.setName(etTasmota.getText().toString());
                    DeviceListActivity.anyadirDispositivo(aireAcondicionado,aireAcondicionado.getMac(),aireAcondicionado.getName());

                } else if (lista.getSelectedItem().toString().toLowerCase().equals("ir tv")) {

                    IRRemote remote = new IRRemote();
                    remote.setMac(etTasmota.getText().toString());
                    remote.setName(etTasmota.getText().toString());
                    DeviceListActivity.anyadirDispositivo(remote,remote.getMac(),remote.getNombre());

                }
                break;

            case R.id.btnAnyadirRPi:
                RPi rpi = new RPi();
                rpi.setMac(etRPi.getText().toString());
                rpi.setName(etRPi.getText().toString());
                DeviceListActivity.anyadirDispositivo(rpi,rpi.getMac(),rpi.getName());

                break;

        }
    }

    public void spinnerTamota() {

        String[] items = new String[]{"Sonoff", "IR AC", "IR TV"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        lista.setAdapter(adapter);

    }
}

package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class BuscarActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TOPIC = "ewpe-smart/#";
    private RelativeLayout lytAnyadirDispositivo;
    private static ArrayList<String> macs = new ArrayList<>();

    private static Button btnAnyadirTasmota;
    private static EditText etSonoff;
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

        btnAnyadirTasmota = findViewById(R.id.btnAnyadirTasmota);
        btnAnyadirTasmota.setOnClickListener(this);
        btnAnyadirRPi = findViewById(R.id.btnAnyadirRPi);
        btnAnyadirRPi.setOnClickListener(this);

        etSonoff = findViewById(R.id.etSonoff);
        etRPi = findViewById(R.id.etRPi);

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
                sonoff.setMac(etSonoff.getText().toString());
                sonoff.setName(etSonoff.getText().toString());
                DeviceListActivity.anyadirDispositivo(sonoff,sonoff.getMac(),sonoff.getName());

                break;

            case R.id.btnAnyadirRPi:
                RPi rpi = new RPi();
                rpi.setMac(etRPi.getText().toString());
                rpi.setName(etRPi.getText().toString());
                DeviceListActivity.anyadirDispositivo(rpi,rpi.getMac(),rpi.getName());

                break;

        }
    }
}

package com.example.smarthome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPlay;
    private Button btnPause;
    private Button btnNext;
    private Button btnPrev;
    private Button btnRefresh;
    private ImageButton ibDescargar;
    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private static ArrayList<String> archivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_player);

        archivos = new ArrayList<>();

        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnRefresh = findViewById(R.id.btnRefresh);
        ibDescargar = findViewById(R.id.ibDescargar);

        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        ibDescargar.setOnClickListener(this);

        DeviceListActivity.getMqttAndroidClient().publishMessage("pyrpi/youtube/refrescarLista", "");

        recyclerView = findViewById(R.id.recyclerPlayer);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new PlayerActivityAdapter(this, archivos);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {

            case R.id.btnPlay:
                //DeviceListActivity.getMqttAndroidClient().publishMessage("pyrpi/youtube/reproducir", "Dj Nas D - Why You Wanna Stop Me.mp4");
                break;

            case R.id.btnPause:
                DeviceListActivity.getMqttAndroidClient().publishMessage("pyrpi/youtube/parar", "");
                break;

            case R.id.btnRefresh:
                DeviceListActivity.getMqttAndroidClient().publishMessage("pyrpi/youtube/refrescarLista", "");
                break;

            case R.id.ibDescargar:
                descargar();
                break;

        }
    }

    public static ArrayList<String> getArchivos() {
        return archivos;
    }

    public static RecyclerView.Adapter getmAdapter() {
        return mAdapter;
    }

    public static void setmAdapter(RecyclerView.Adapter mAdapter) {
        PlayerActivity.mAdapter = mAdapter;
    }

    private void descargar() {
        final EditText txtUrl = new EditText(this);

        txtUrl.setHint("URL de YouTube");

        new AlertDialog.Builder(this)
                .setTitle("Descargar de YouTube")
                .setMessage("Pega la URL de YouTube")
                .setView(txtUrl)
                .setPositiveButton("Descargar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        DeviceListActivity.getMqttAndroidClient().publishMessage("pyrpi/youtube/descargar", txtUrl.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }
}

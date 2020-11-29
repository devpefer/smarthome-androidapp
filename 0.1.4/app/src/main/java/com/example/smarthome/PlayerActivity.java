package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPlay;
    private Button btnPause;
    private Button btnNext;
    private Button btnPrev;
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

        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);

        archivos.add("Dj Nas D - Why You Wanna Stop Me.mp4");
        archivos.add("Fugees - Vocab.mp4");
        archivos.add("Lauryn Hill - Doo-Wop.mp4");

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
                DeviceListActivity.getMqttAndroidClient().publishMessage("pyrpi/youtube/reproducir", "Dj Nas D - Why You Wanna Stop Me.mp4");
                break;

            case R.id.btnPause:
                DeviceListActivity.getMqttAndroidClient().publishMessage("pyrpi/youtube/parar", "");
                break;
        }
    }

    public static ArrayList<String> getArchivos() {
        return archivos;
    }
}

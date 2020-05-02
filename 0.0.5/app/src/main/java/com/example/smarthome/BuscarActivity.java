package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;

import java.util.ArrayList;

public class BuscarActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private MqttAndroidClient mqttAndroidClient;

    private ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        listview = findViewById(R.id.lstListaDispositivos);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, RemoteCtrlActivity.getMacs());

        //listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);

    }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*
        RemoteCtrlActivity.setAireHab1(RemoteCtrlActivity.getMacs().get(position).toString());
        finish();

    }

 */
    }
}
package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;

import java.util.ArrayList;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, PopupMenu.OnMenuItemClickListener {

    private ListView listview;

    private static ArrayList<String> locationsNombres;
    private static ArrayList<Location> locations;
    private ImageButton ibAnyadir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        listview = findViewById(R.id.lstListaLocalizacion);
        ibAnyadir = findViewById(R.id.ibAnyadir);

        ibAnyadir.setOnClickListener(this);

        locations = new ArrayList<>();
        locationsNombres = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locationsNombres);

        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        for(int i = 0; i < locationsNombres.size(); i++) {

            if(locationsNombres.get(i).equals(locationsNombres.get(position))) {

                Intent intent = new Intent(LocationActivity.this, PlanoActivity.class);
                startActivity(intent);

            }

        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ibAnyadir:

                showPopupPrincipal(v);

                break;
        }

    }

    public void showPopupPrincipal(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_location, popup.getMenu());
        popup.show();

    }

    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menuNuevo:
                Intent intent = new Intent(LocationActivity.this, AnyadirLocActivity.class);
                startActivity(intent);
                break;

        }

        return true;
    }

    public static ArrayList<Location> getLocations() {
        return locations;
    }

    public static ArrayList<String> getLocationsNombres() {
        return locationsNombres;
    }

}

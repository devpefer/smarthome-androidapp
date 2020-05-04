package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private static ArrayList<String> locationsNombres;
    private static ArrayList<Location> locations;
    private ImageButton ibAnyadir;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        ibAnyadir = findViewById(R.id.ibAnyadir);
        ibAnyadir.setOnClickListener(this);

        locations = new ArrayList<>();
        locationsNombres = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerListaLocations);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new LocationActivityAdapter(this,locationsNombres);
        recyclerView.setAdapter(mAdapter);

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

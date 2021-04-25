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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;



public class LocationActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private SharedPreferences prefs;
    private Gson gson;
    private static ArrayList<Location> locations;
    private static ArrayList<String> locationsNombres;
    private ImageButton ibAnyadir;

    private RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        ibAnyadir = findViewById(R.id.ibAnyadir);
        ibAnyadir.setOnClickListener(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        gson = new Gson();
        String objetos = prefs.getString("listaLocations","");

        Type listType = new TypeToken<ArrayList<Location>>(){}.getType();

        locations = gson.fromJson(objetos, listType);

        if(locations == null ) {

            locations = new ArrayList<>();

            Gson gson = new Gson();

            String arrayListLocations = gson.toJson(LocationActivity.getLocations());


            prefs = PreferenceManager.getDefaultSharedPreferences(this);

            SharedPreferences.Editor prefsEditor = prefs.edit();

            prefsEditor.putString("listaLocations",arrayListLocations);
            prefsEditor.apply();

        }

        locationsNombres = new ArrayList<>();

        for(int i = 0; i < locations.size(); i++) {

           locationsNombres.add(locations.get(i).getNombre());

        }



        recyclerView = findViewById(R.id.recyclerListaLocations);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new LocationActivityAdapter(this,locationsNombres);
        recyclerView.setAdapter(mAdapter);

        context = getApplicationContext();

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


    public static Context getContext() {
        return context;
    }

    public static ArrayList<String> getLocationsNombres() {
        return locationsNombres;
    }

    public static void setLocationsNombres(ArrayList<String> locationsNombres) {
        LocationActivity.locationsNombres = locationsNombres;
    }

    public static RecyclerView.Adapter getmAdapter() {
        return mAdapter;
    }

    public static void anyadirLocation(Location location) {

        SharedPreferences prefs;

        locations.add(location);
        locationsNombres.add(location.getNombre());



        Gson gson = new Gson();

        String arrayListLocations = gson.toJson(LocationActivity.getLocations());


        prefs = PreferenceManager.getDefaultSharedPreferences(LocationActivity.getContext());

        SharedPreferences.Editor prefsEditor = prefs.edit();

        prefsEditor.putString("listaLocations",arrayListLocations);
        prefsEditor.putString("listaLocationsNombres",arrayListLocations);
        prefsEditor.apply();

        mAdapter.notifyDataSetChanged();
    }

    public static void deleteLocation(Location location) {

        SharedPreferences prefs;

        locations.remove(location);
        locationsNombres.remove(location.getNombre());

        Gson gson = new Gson();

        String arrayListLocations = gson.toJson(getLocations());

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        SharedPreferences.Editor prefsEditor = prefs.edit();

        prefsEditor.putString("listaLocations",arrayListLocations);
        prefsEditor.putString("listaLocationsNombres",arrayListLocations);
        prefsEditor.remove("listaAires"+location.getNombre());
        prefsEditor.remove("listaRPi"+location.getNombre());
        prefsEditor.remove("listaSonoff"+location.getNombre());
        prefsEditor.apply();

        mAdapter.notifyDataSetChanged();
    }

}

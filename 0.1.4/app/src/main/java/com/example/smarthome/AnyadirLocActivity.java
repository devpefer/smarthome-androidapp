package com.example.smarthome;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AnyadirLocActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etAnyadirLoc;
    private EditText etBrokerIP;
    private EditText etClientId;
    private Button btnAnyadirLoc;

    private String nombre;
    private String brokerIP;
    private String clientId;

    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_anyadir_loc);

        etAnyadirLoc = findViewById(R.id.etAnyadirLoc);
        etBrokerIP = findViewById(R.id.etBrokerIP);
        etClientId = findViewById(R.id.etClientId);

        btnAnyadirLoc = findViewById(R.id.btnAnyadirLoc);

        btnAnyadirLoc.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnAnyadirLoc:

                if(!etAnyadirLoc.getText().toString().equals("")) {
                    nombre = etAnyadirLoc.getText().toString();
                }

                if(!etBrokerIP.getText().toString().equals("")) {
                    brokerIP = "tcp://" + etBrokerIP.getText().toString() + ":1883";
                }

                if(!etClientId.getText().toString().equals("")) {
                    clientId = etClientId.getText().toString();
                } else {

                    clientId = "";

                }

                location = new Location(nombre,brokerIP,clientId);

                if(!etAnyadirLoc.equals("") || !etAnyadirLoc.equals(" ")) {

                    LocationActivity.anyadirLocation(location);
                    finish();

                } else {

                    Toast.makeText(this, "No se puede añadir.", Toast.LENGTH_SHORT).show();

                }
                break;

        }

    }
}

package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AnyadirLocActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etAnyadirLoc;
    private Button btnAnyadirLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir_loc);

        etAnyadirLoc = findViewById(R.id.etAnyadirLoc);
        btnAnyadirLoc = findViewById(R.id.btnAnyadirLoc);

        btnAnyadirLoc.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnAnyadirLoc:

                if(!etAnyadirLoc.equals("") || !etAnyadirLoc.equals(" ")) {
                    LocationActivity.getNames().add(etAnyadirLoc.getText().toString());
                    finish();

                } else {

                    Toast.makeText(this, "No se puede añadir.", Toast.LENGTH_SHORT).show();

                }
                break;

        }

    }
}

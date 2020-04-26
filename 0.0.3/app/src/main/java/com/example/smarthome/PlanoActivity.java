package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PlanoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivPlano;
    private ImageView ivAire;
    private String mac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plano);

        ivPlano = findViewById(R.id.ivPlano);
        ivAire = findViewById(R.id.ivAire);

        ivAire.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ivAire:

                Intent intent = new Intent(PlanoActivity.this, RemoteCtrlActivity.class);
                startActivity(intent);

        }

    }
}

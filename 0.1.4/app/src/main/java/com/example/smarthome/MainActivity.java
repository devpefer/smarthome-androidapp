package com.example.smarthome;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String USERNAME = "devpefer";
    private final String PASSWORD = "pass";
    private EditText etUser;
    private EditText etPassword;
    private Button btnEntrar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        btnEntrar = findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnEntrar:
                etUser.setText("devpefer");
                etPassword.setText("pass");

                if ((etUser.getText().toString().equals(USERNAME)) && (etPassword.getText().toString().equals(PASSWORD))) {

                    Intent intent = new Intent(MainActivity.this, LocationActivity.class);

                    startActivity(intent);

                    etPassword.setText("");

                } else {

                    Toast.makeText(this, "Usuario o contraseña incorretos.", Toast.LENGTH_SHORT).show();

                }


                break;

        }

    }

    public void solicitarPermisos() {

        String[] permisos = new String[]{Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET};

        for (int i = 0; i < permisos.length; i++) {

            if (ContextCompat.checkSelfPermission(this, permisos[i]) == PackageManager.PERMISSION_DENIED) {

                Log.i("PERMISOS", "PERMISO " + permisos[i] + " NO CONCEDIDO");
                Utilidades.solicitarPermiso(permisos[i], "La aplicacion necesita permiso", i, this);

            } else {

                Log.i("PERMISOS", "PERMISO " + permisos[i] + " CONCEDIDO");

            }

        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if(requestCode == 0) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            } else {
                Log.i("BAD", "Sin su permiso no se pueden leer los contactos.");
                Toast.makeText(this, "Sin permiso no se pueden leer los contactos", Toast.LENGTH_SHORT).show();

            }
        }

        if(requestCode == 1) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            } else {
                Log.i("BAD", "Sin su permiso no se pueden leer los contactos.");
                Toast.makeText(this, "Sin permiso no se pueden leer los contactos", Toast.LENGTH_SHORT).show();

            }
        }

        if(requestCode == 2) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            } else {
                Log.i("BAD", "Sin su permiso no se pueden leer los contactos.");
                Toast.makeText(this, "Sin permiso no se pueden leer los contactos", Toast.LENGTH_SHORT).show();

            }

        }

    }

}
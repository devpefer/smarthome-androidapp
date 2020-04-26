package com.example.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String USERNAME = "devpefer";
    private final String PASSWORD = "pass";
    private EditText etUser;
    private EditText etPassword;
    private Button btnEntrar;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
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
}
package com.example.smarthome;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int TEMP_MIN = 17;
    private final int TEMP_MAX = 30;

    private MqttAndroidClient mqttAndroidClient;

    private TextView tvPower;
    private TextView tvTemperatura;
    private Button btnEncender;
    private Button btnApagar;
    private Button btnTemperatura;
    private Button btnReconectar;
    private EditText etTemperatura;


    private Gson gson;
    private AireAcondicionado aircoParams;
    private String temperaturaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvPower = findViewById(R.id.tvPower);
        tvTemperatura = findViewById(R.id.tvTemperatura);
        btnEncender = findViewById(R.id.btnEncender);
        btnApagar = findViewById(R.id.btnApagar);
        btnTemperatura = findViewById(R.id.btnTemperatura);
        etTemperatura = findViewById(R.id.etTemperatura);
        btnReconectar = findViewById(R.id.btnReconectar);

        btnEncender.setOnClickListener(this);
        btnApagar.setOnClickListener(this);
        btnTemperatura.setOnClickListener(this);
        btnReconectar.setOnClickListener(this);

        btnEncender.setEnabled(false);
        btnApagar.setEnabled(false);
        btnTemperatura.setEnabled(false);

        String[] permisos = new String[]{Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET};

        for(int i = 0; i < permisos.length; i++) {

            if(ContextCompat.checkSelfPermission(this, permisos[i]) == PackageManager.PERMISSION_DENIED) {

                Log.i("PERMISOS", "PERMISO " + permisos[i] + " NO CONCEDIDO");
                Utilidades.solicitarPermiso(permisos[i], "La aplicacion necesita permiso", i, this);

            } else {

                Log.i("PERMISOS", "PERMISO " + permisos[i] + " CONCEDIDO");

            }

        }

        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), "tcp://192.168.0.106:1883", "");

        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.i("AIRE", "connection lost");

                btnEncender.setEnabled(false);
                btnApagar.setEnabled(false);
                btnTemperatura.setEnabled(false);

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.i("AIRE", "topic: " + topic + ", msg: " + new String(message.getPayload()));
                gson = new Gson();
                aircoParams = gson.fromJson(new String(message.getPayload()), AireAcondicionado.class);

                if(aircoParams != null) {

                    if (Integer.parseInt(aircoParams.getPow()) == 0) {

                        tvPower.setText("Estado: Apagado");

                    } else {

                        tvPower.setText("Estado: Encendido");

                    }

                    temperaturaActual = "Temp. actual: " + aircoParams.getSetTem() + "ºC";

                    tvTemperatura.setText(temperaturaActual);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.i("AIRE", "msg delivered");
            }
        });

        conectar();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mqttAndroidClient.isConnected()) {
            conectar();
        }
    }

    public void conectar() {

        try {
            mqttAndroidClient.connect(null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("AIRE", "connect succeed");

                    btnEncender.setEnabled(true);
                    btnApagar.setEnabled(true);
                    btnTemperatura.setEnabled(true);
                    subscribeTopic("ewpe-smart/f4911e43aabc/#");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("AIRE", "connect failed");

                    btnEncender.setEnabled(false);
                    btnApagar.setEnabled(false);
                    btnTemperatura.setEnabled(false);
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(String topic, String payload) {
        try {


            if (!mqttAndroidClient.isConnected()) {
                conectar();
            }


            MqttMessage message = new MqttMessage();
            message.setPayload(payload.getBytes());
            message.setQos(0);
            mqttAndroidClient.publish(topic, message,null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("AIRE", "publish succeed! ") ;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("AIRE", "publish failed!") ;
                }
            });
        } catch (MqttException e) {
            Log.e("AIRE", e.toString());
            e.printStackTrace();
        }
    }

    public void subscribeTopic(String topic) {
        try {
            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("AIRE", "subscribed succeed");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("AIRE", "subscribed failed");
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnEncender:

                if(aircoParams != null) {

                    if(Integer.parseInt(aircoParams.getPow()) == 0) {

                        publishMessage("ewpe-smart/f4911e43aabc/set", "{\"Pow\": 1}");
                        Toast.makeText(this, "El aire acondicionado se ha encendido", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(this, "El aire acondicionado ya está encendido", Toast.LENGTH_SHORT).show();

                    }

                }
                break;

            case R.id.btnApagar:

                if(aircoParams != null) {

                    if(Integer.parseInt(aircoParams.getPow()) == 1) {

                        publishMessage("ewpe-smart/f4911e43aabc/set", "{\"Pow\": 0}");
                        Toast.makeText(this, "El aire acondicionado se ha apagado", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(this, "El aire acondicionado ya está apagado", Toast.LENGTH_SHORT).show();

                    }

                }
                break;

            case R.id.btnTemperatura:

                int temp = 0;

                try {

                    temp = Integer.parseInt(etTemperatura.getText().toString());

                    if ((temp >= TEMP_MIN) && (temp <= TEMP_MAX)) {

                        publishMessage("ewpe-smart/f4911e43aabc/set", "{\"SetTem\": " + temp + "}");
                        Toast.makeText(this, "Temperatura fijada en " + temp + " grados", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(this, "Valor de temperatura no válido", Toast.LENGTH_SHORT).show();

                    }

                } catch (NumberFormatException e) {

                    Toast.makeText(this, "El valor debe ser numérico y sin decimales", Toast.LENGTH_SHORT).show();

            }
                break;

            case R.id.btnReconectar:

                if (!mqttAndroidClient.isConnected()) {
                    conectar();
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

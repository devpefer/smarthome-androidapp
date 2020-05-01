package com.example.smarthome;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
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

import java.util.ArrayList;

public class RemoteCtrlActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private final int TEMP_MIN = 17;
    private final int TEMP_MAX = 30;
    private static String aireHab1 = "";

    private MqttAndroidClient mqttAndroidClient;

    private ImageView ivPower;
    private TextView tvTemperatura;
    private ImageView ivSubirTemp;
    private ImageView ivBajarTemp;
    private ImageView ivFan;
    private ImageView ivFanAutoDisplay;
    private ImageView ivFanMinDisplay;
    private ImageView ivFanMedDisplay;
    private ImageView ivFanMaxDisplay;
    private ImageView ivTurbo;
    private ImageView ivAir;
    private ImageView ivHealth;
    private ImageView ivLight;
    private ImageView ivTurboDisplay;
    private ImageView ivAirDisplay;
    private ImageView ivHealthDisplay;
    private ImageView ivLightDisplay;

    private Gson gson;
    private AireAcondicionado aircoParams;
    private String temperaturaActual;
    private ImageButton ibRemote;

    private ListView listview;

    private int temp;
    private int cola;

    private static ArrayList<String> macs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_ctrl);

        ivPower = findViewById(R.id.ivPower);
        ivPower.setOnClickListener(this);

        ivFan = findViewById(R.id.ivFanSpeed);
        ivFanAutoDisplay = findViewById(R.id.ivFanSpeedAutoDisplay);
        ivFanMinDisplay = findViewById(R.id.ivFanSpeedMinDisplay);
        ivFanMedDisplay = findViewById(R.id.ivFanSpeedMedDisplay);
        ivFanMaxDisplay = findViewById(R.id.ivFanSpeedMaxDisplay);
        ivTurbo = findViewById(R.id.ivTurbo);
        ivAir = findViewById(R.id.ivAir);
        ivHealth = findViewById(R.id.ivHealth);
        ivLight = findViewById(R.id.ivLight);

        ivTurboDisplay = findViewById(R.id.ivTurboDisplay);
        ivAirDisplay = findViewById(R.id.ivAirDisplay);
        ivHealthDisplay = findViewById(R.id.ivHealthDisplay);
        ivLightDisplay = findViewById(R.id.ivLightDisplay);


        tvTemperatura = findViewById(R.id.tvTemperatura);
        ivBajarTemp = findViewById(R.id.ivBajarTemp);
        ivSubirTemp = findViewById(R.id.ivSubirTemp);
        ivBajarTemp.setOnClickListener(this);
        ivSubirTemp.setOnClickListener(this);

        ivFan.setOnClickListener(this);
        ivTurbo.setOnClickListener(this);
        ivAir.setOnClickListener(this);
        ivHealth.setOnClickListener(this);
        ivLight.setOnClickListener(this);


        ibRemote = findViewById(R.id.ibRemote);

        ibRemote.setOnClickListener(this);

        listview = findViewById(R.id.lstListaLocalizacion);
        macs = new ArrayList<String>();

/*
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

*/
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



            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.i("AIRE", "topic: " + topic + ", msg: " + new String(message.getPayload()));
                String mac;
                mac = topic;
                mac = mac.replace("ewpe-smart/", "");
                mac = mac.replace("/status", "");
                Log.i("UNIDAD", mac);
                if (!macs.contains(mac) && !mac.equals("devices/list")) {

                    macs.add(mac);

                }

                refrescarParams(message);

                }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.i("AIRE", "msg delivered");
            }
        });

        if (!mqttAndroidClient.isConnected()) {
            conectar();
        }
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

                    subscribeTopic("ewpe-smart/#");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("AIRE", "connect failed");

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

                    case R.id.ivPower:

                        if (mqttAndroidClient.isConnected() && !aireHab1.equals("")) {

                            if (Integer.parseInt(aircoParams.getPow()) == 0) {

                                publishMessage("ewpe-smart/" + aireHab1 + "/set", "{\"Pow\": 1}");

                            } else {

                                publishMessage("ewpe-smart/" + aireHab1 + "/set", "{\"Pow\": 0}");

                            }
                        }
                        break;

                    case R.id.ivBajarTemp:

                        if (mqttAndroidClient.isConnected() && !aireHab1.equals("")) {

                            temp = Integer.parseInt(aircoParams.getSetTem());

                            if (temp > TEMP_MIN) {

                                publishMessage("ewpe-smart/" + aireHab1 + "/set", "{\"SetTem\": " + (temp - 1) + "}");

                            } else {

                                Toast.makeText(this, "Valor de temperatura no válido", Toast.LENGTH_SHORT).show();

                            }
                        }
                        break;

                    case R.id.ivSubirTemp:

                        if (mqttAndroidClient.isConnected() && !aireHab1.equals("")) {

                            temp = Integer.parseInt(aircoParams.getSetTem());

                            if (temp < TEMP_MAX) {

                                publishMessage("ewpe-smart/" + aireHab1 + "/set", "{\"SetTem\": " + (temp + 1) + "}");

                            } else {

                                Toast.makeText(this, "Valor de temperatura no válido", Toast.LENGTH_SHORT).show();

                            }
                        }
                        break;

                    case R.id.ivFanSpeed:

                        if (mqttAndroidClient.isConnected() && !aireHab1.equals("")) {

                            switch (aircoParams.getWdSpd()) {

                                case "0":

                                    publishMessage("ewpe-smart/" + aireHab1 + "/set", "{\"WdSpd\": " + ("1") + "}");
                                    break;

                                case "1":

                                    publishMessage("ewpe-smart/" + aireHab1 + "/set", "{\"WdSpd\": " + ("3") + "}");
                                    break;

                                case "3":

                                    publishMessage("ewpe-smart/" + aireHab1 + "/set", "{\"WdSpd\": " + ("5") + "}");
                                    break;

                                case "5":

                                    publishMessage("ewpe-smart/" + aireHab1 + "/set", "{\"WdSpd\": " + ("0") + "}");
                                    break;

                            }
                        }
                        break;

                case R.id.ivTurbo:

                    if(mqttAndroidClient.isConnected() && !aireHab1.equals("")) {

                        if(aircoParams.getTur().equals("0")) {
                            publishMessage("ewpe-smart/" + aireHab1 + "/set", "{\"Tur\": " + ("1") + "}");
                        }else {

                            publishMessage("ewpe-smart/" + aireHab1 + "/set", "{\"Tur\": " + ("0") + "}");

                        }

                    }

                    break;

                case R.id.ivAir:

                    if(mqttAndroidClient.isConnected() && !aireHab1.equals("")) {

                        if(aircoParams.getAir().equals("0")) {
                            publishMessage("ewpe-smart/" + aireHab1 + "/set", "{\"Air\": " + ("1") + "}");
                        }else {

                            publishMessage("ewpe-smart/" + aireHab1 + "/set", "{\"Air\": " + ("0") + "}");

                        }

                    }

                    break;

                case R.id.ivHealth:

                    if(mqttAndroidClient.isConnected() && !aireHab1.equals("")) {

                        if(aircoParams.getHealth().equals("0")) {
                            publishMessage("ewpe-smart/" + aireHab1 + "/set", "{\"Health\": " + ("1") + "}");
                        }else {

                            publishMessage("ewpe-smart/" + aireHab1 + "/set", "{\"Health\": " + ("0") + "}");

                        }

                    }

                    break;

                case R.id.ivLight:

                    if(mqttAndroidClient.isConnected() && !aireHab1.equals("")) {

                        if(aircoParams.getLig().equals("0")) {
                            publishMessage("ewpe-smart/" + aireHab1 + "/set", "{\"Lig\": " + ("1") + "}");
                        }else {

                            publishMessage("ewpe-smart/" + aireHab1 + "/set", "{\"Lig\": " + ("0") + "}");

                        }
                    }

                    break;

                case R.id.ibRemote:
                    showPopupRemote(v);
                    break;
                }

        }


/*
        switch (v.getId()) {

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

                break;
        }
*/


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

    public void showPopupRemote(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_remote_ctrl, popup.getMenu());
        popup.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menuBuscar:

                Intent intent = new Intent(RemoteCtrlActivity.this, BuscarActivity.class);
                startActivity(intent);
                break;

        }

        return true;
    }

    public static ArrayList getMacs() {

        return macs;

    }

    public static String getAireHab1() {

        return aireHab1;

    }

    public static void setAireHab1(String mac) {

        aireHab1 = mac;

    }

    public void refrescarParams(MqttMessage message) {

        gson = new Gson();

        aircoParams = gson.fromJson(new String(message.getPayload()), AireAcondicionado.class);

        if (aircoParams != null && !aireHab1.equals("")) {

            if (Integer.parseInt(aircoParams.getPow()) == 0) {

                ivPower.setImageResource(R.drawable.ic_poweroff);

            } else {

                ivPower.setImageResource(R.drawable.ic_poweron);

            }

            temperaturaActual = aircoParams.getSetTem() + "º";

            tvTemperatura.setText(temperaturaActual);

            switch (aircoParams.getWdSpd()) {

                case "0":

                    ivFanMinDisplay.setVisibility(View.GONE);
                    ivFanMedDisplay.setVisibility(View.GONE);
                    ivFanMaxDisplay.setVisibility(View.GONE);
                    ivFanAutoDisplay.setVisibility(View.VISIBLE);
                    break;

                case "1":

                    ivFanMedDisplay.setVisibility(View.GONE);
                    ivFanMaxDisplay.setVisibility(View.GONE);
                    ivFanAutoDisplay.setVisibility(View.GONE);
                    ivFanMinDisplay.setVisibility(View.VISIBLE);
                    break;

                case "3":

                    ivFanMinDisplay.setVisibility(View.GONE);
                    ivFanMaxDisplay.setVisibility(View.GONE);
                    ivFanAutoDisplay.setVisibility(View.GONE);
                    ivFanMedDisplay.setVisibility(View.VISIBLE);
                    break;

                case "5":

                    ivFanMinDisplay.setVisibility(View.GONE);
                    ivFanMedDisplay.setVisibility(View.GONE);
                    ivFanAutoDisplay.setVisibility(View.GONE);
                    ivFanMaxDisplay.setVisibility(View.VISIBLE);
                    break;

            }

            switch (aircoParams.getTur()) {

                case "0":

                    ivTurboDisplay.setVisibility(View.GONE);
                    break;

                case "1":

                    ivTurboDisplay.setVisibility(View.VISIBLE);

                    break;

            }

            switch (aircoParams.getAir()) {

                case "0":

                    ivAirDisplay.setVisibility(View.GONE);
                    break;

                case "1":

                    ivAirDisplay.setVisibility(View.VISIBLE);

                    break;

            }

            switch (aircoParams.getHealth()) {

                case "0":

                    ivHealthDisplay.setVisibility(View.GONE);
                    break;

                case "1":

                    ivHealthDisplay.setVisibility(View.VISIBLE);

                    break;

            }

            switch (aircoParams.getLig()) {

                case "0":

                    ivLightDisplay.setVisibility(View.GONE);
                    break;

                case "1":

                    ivLightDisplay.setVisibility(View.VISIBLE);

                    break;

            }

        }

    }

}

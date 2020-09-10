package com.example.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

public class RemoteCtrlActivity extends AppCompatActivity implements View.OnClickListener {

    private static  String mac;
    private static AireAcondicionado aircoParams = new AireAcondicionado();
    private final String TOPIC = "ewpe-smart/#";
    private final int TEMP_MIN = 17;
    private final int TEMP_MAX = 30;


    private static ImageView ivPower;
    private static TextView tvTemperatura;
    private static  ImageView ivSubirTemp;
    private static  ImageView ivBajarTemp;
    private static  ImageView ivFan;
    private static  ImageView ivFanAutoDisplay;
    private static  ImageView ivFanMinDisplay;
    private static  ImageView ivFanMedDisplay;
    private static  ImageView ivFanMaxDisplay;
    private static  ImageView ivTurbo;
    private static  ImageView ivAir;
    private static  ImageView ivHealth;
    private static  ImageView ivLight;
    private static  ImageView ivTurboDisplay;
    private static  ImageView ivAirDisplay;
    private static  ImageView ivHealthDisplay;
    private static  ImageView ivLightDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_remote_ctrl);

        mac = getIntent().getExtras().getString("mac");
        aircoParams.setMac(mac);

        ivPower = findViewById(R.id.ivPower);
        tvTemperatura = findViewById(R.id.tvTemperatura);
        ivBajarTemp = findViewById(R.id.ivBajarTemp);
        ivSubirTemp = findViewById(R.id.ivSubirTemp);
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

        ivPower.setOnClickListener(this);
        ivBajarTemp.setOnClickListener(this);
        ivSubirTemp.setOnClickListener(this);
        ivFan.setOnClickListener(this);
        ivTurbo.setOnClickListener(this);
        ivAir.setOnClickListener(this);
        ivHealth.setOnClickListener(this);
        ivLight.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    protected void onPause() {
        super.onPause();


    }

    @Override
    public void onClick(View v) {

        int temp;

        switch (v.getId()) {

            case R.id.ivPower:

                if (PlanoActivity.getMqttAndroidClient().getMqttAndroidClient().isConnected() && !mac.equals("")) {

                    if (Integer.parseInt(aircoParams.getPow()) == 0) {

                        PlanoActivity.getMqttAndroidClient().publishMessage("ewpe-smart/" + mac + "/set", "{\"Pow\": 1}");

                    } else {

                        PlanoActivity.getMqttAndroidClient().publishMessage("ewpe-smart/" + mac + "/set", "{\"Pow\": 0}");

                    }
                }
                break;

            case R.id.ivBajarTemp:

                if (PlanoActivity.getMqttAndroidClient().getMqttAndroidClient().isConnected() && !mac.equals("")) {

                    temp = Integer.parseInt(aircoParams.getSetTem());

                    if (temp > TEMP_MIN) {

                        PlanoActivity.getMqttAndroidClient().publishMessage("ewpe-smart/" + mac + "/set", "{\"SetTem\": " + (temp - 1) + "}");

                    } else {

                        Toast.makeText(this, "Valor de temperatura no válido", Toast.LENGTH_SHORT).show();

                    }
                }
                break;

            case R.id.ivSubirTemp:

                if (PlanoActivity.getMqttAndroidClient().getMqttAndroidClient().isConnected() && !mac.equals("")) {

                    temp = Integer.parseInt(aircoParams.getSetTem());

                    if (temp < TEMP_MAX) {

                        PlanoActivity.getMqttAndroidClient().publishMessage("ewpe-smart/" + mac + "/set", "{\"SetTem\": " + (temp + 1) + "}");

                    } else {

                        Toast.makeText(this, "Valor de temperatura no válido", Toast.LENGTH_SHORT).show();

                    }
                }
                break;

            case R.id.ivFanSpeed:

                if (PlanoActivity.getMqttAndroidClient().getMqttAndroidClient().isConnected() && !mac.equals("")) {

                    switch (aircoParams.getWdSpd()) {

                        case "0":

                            PlanoActivity.getMqttAndroidClient().publishMessage("ewpe-smart/" + mac + "/set", "{\"WdSpd\": " + ("1") + "}");
                            break;

                        case "1":

                            PlanoActivity.getMqttAndroidClient().publishMessage("ewpe-smart/" + mac + "/set", "{\"WdSpd\": " + ("3") + "}");
                            break;

                        case "3":

                            PlanoActivity.getMqttAndroidClient().publishMessage("ewpe-smart/" + mac + "/set", "{\"WdSpd\": " + ("5") + "}");
                            break;

                        case "5":

                            PlanoActivity.getMqttAndroidClient().publishMessage("ewpe-smart/" + mac + "/set", "{\"WdSpd\": " + ("0") + "}");
                            break;

                    }
                }
                break;

            case R.id.ivTurbo:

                if (PlanoActivity.getMqttAndroidClient().getMqttAndroidClient().isConnected() && !mac.equals("")) {

                    if (aircoParams.getTur().equals("0")) {
                        PlanoActivity.getMqttAndroidClient().publishMessage("ewpe-smart/" + mac + "/set", "{\"Tur\": " + ("1") + "}");
                    } else {

                        PlanoActivity.getMqttAndroidClient().publishMessage("ewpe-smart/" + mac + "/set", "{\"Tur\": " + ("0") + "}");

                    }

                }

                break;

            case R.id.ivAir:

                if (PlanoActivity.getMqttAndroidClient().getMqttAndroidClient().isConnected() && !mac.equals("")) {

                    if (aircoParams.getAir().equals("0")) {
                        PlanoActivity.getMqttAndroidClient().publishMessage("ewpe-smart/" + mac + "/set", "{\"Air\": " + ("1") + "}");
                    } else {

                        PlanoActivity.getMqttAndroidClient().publishMessage("ewpe-smart/" + mac + "/set", "{\"Air\": " + ("0") + "}");

                    }

                }

                break;

            case R.id.ivHealth:

                if (PlanoActivity.getMqttAndroidClient().getMqttAndroidClient().isConnected() && !mac.equals("")) {

                    if (aircoParams.getHealth().equals("0")) {
                        PlanoActivity.getMqttAndroidClient().publishMessage("ewpe-smart/" + mac + "/set", "{\"Health\": " + ("1") + "}");
                    } else {

                        PlanoActivity.getMqttAndroidClient().publishMessage("ewpe-smart/" + mac + "/set", "{\"Health\": " + ("0") + "}");

                    }

                }

                break;

            case R.id.ivLight:

                if (PlanoActivity.getMqttAndroidClient().getMqttAndroidClient().isConnected() && !mac.equals("")) {

                    if (aircoParams.getLig().equals("0")) {
                        PlanoActivity.getMqttAndroidClient().publishMessage("ewpe-smart/" + mac + "/set", "{\"Lig\": " + ("1") + "}");
                    } else {

                        PlanoActivity.getMqttAndroidClient().publishMessage("ewpe-smart/" + mac + "/set", "{\"Lig\": " + ("0") + "}");

                    }
                }

                break;


        }

    }


    public static AireAcondicionado getAircoParams() {
        return aircoParams;
    }

    public static void setAircoParams(AireAcondicionado aircoParams) {

        RemoteCtrlActivity.aircoParams = aircoParams;
    }


    public static void refrescarParams() {

        if (aircoParams != null && !mac.equals("")) {

            if (Integer.parseInt(aircoParams.getPow()) == 0) {

                ivPower.setImageResource(R.drawable.ic_poweroff);

            } else {

                ivPower.setImageResource(R.drawable.ic_poweron);

            }

            String temperaturaActual = aircoParams.getSetTem() + "º";

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


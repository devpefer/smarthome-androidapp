package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;


import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MQTTUtils {

    private MqttAndroidClient mqttAndroidClient;

    private ArrayList<AireAcondicionado> aircoParams;

    private static String aireHab1 = "";
    private ArrayList<String> macs = new ArrayList<>();

    private static String suscripcion;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList datos = new ArrayList();

    public MQTTUtils(final Context context, String serverURI, String clientId) {

        this.mqttAndroidClient = new MqttAndroidClient(context, serverURI, clientId);
        aircoParams = new ArrayList<>();

        this.mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.i("AIRE", "connection lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {

                Gson gson = new Gson();

                String mac = obtenerMAC(topic);
                String payLoad = new String(message.getPayload());

                if(!macs.contains(mac) && !mac.contains("devices/list")) {

                    macs.add(mac);

                }

                for(int i = 0 ; i < macs.size();i ++) {

                    AireAcondicionado aircoParamsTemp;
                    aircoParamsTemp = gson.fromJson(payLoad, AireAcondicionado.class);

                    aircoParamsTemp.setMac(mac);
                    BuscarActivity.refrescarLista(mac);

                    Log.i("ASDASDASDASDASDAsd", new String(message.getPayload()));
                    
                    if(new String(message.getPayload()).contains("Pow") &&
                            new String(message.getPayload()).contains("Mod") &&
                            new String(message.getPayload()).contains("TemUn") &&
                            new String(message.getPayload()).contains("SetTem") &&
                            new String(message.getPayload()).contains("TemRec") &&
                            new String(message.getPayload()).contains("WdSpd") &&
                            new String(message.getPayload()).contains("Air") &&
                            new String(message.getPayload()).contains("Blo") &&
                            new String(message.getPayload()).contains("Health") &&
                            new String(message.getPayload()).contains("SwhSlp") &&
                            new String(message.getPayload()).contains("Lig") &&
                            new String(message.getPayload()).contains("SwingLfRig") &&
                            new String(message.getPayload()).contains("SwUpDn") &&
                            new String(message.getPayload()).contains("Quiet") &&
                            new String(message.getPayload()).contains("Tur") &&
                            new String(message.getPayload()).contains("SvSt")) {

                        PlanoActivity.refrescarLista(aircoParamsTemp);

                    }

                }



            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.i("AIRE", "msg delivered");
            }
        });

    }

    public void conectar(final Context context, final String topic) {

        if (!mqttAndroidClient.isConnected()) {
            try {
                mqttAndroidClient.connect(null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {

                        subscribeTopic(context, topic);
                        mensajesMQTT(context, 2);

                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        mensajesMQTT(context, 3);

                    }

                });

            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void publishMessage(Context context, String topic, String payload) {
        try {

            if (!mqttAndroidClient.isConnected()) {

                conectar(context, topic);

            }


            MqttMessage message = new MqttMessage();
            message.setPayload(payload.getBytes());
            message.setQos(0);
            mqttAndroidClient.publish(topic, message, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {


                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                }
            });
        } catch (MqttException e) {
            Log.e("AIRE", e.toString());
            e.printStackTrace();
        }
    }

    public void subscribeTopic(final Context context, final String topic) {

        Log.i("TOPIC", topic);

        try {
            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    suscripcion = topic;
                    mensajesMQTT(context, 0);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    suscripcion = topic;
                    mensajesMQTT(context, 1);
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void mensajesMQTT(Context context,int codigo) {

        switch (codigo) {

            case 0:

                Toast.makeText(context, "Suscrito al topic " + suscripcion, Toast.LENGTH_SHORT).show();
                suscripcion = "";

                break;

            case 1:

                Toast.makeText(context, "No se ha podido suscribir al topic " + suscripcion, Toast.LENGTH_SHORT).show();
                suscripcion = "";

                break;

            case 2:

                Toast.makeText(context, "Se ha establecido la conexión con el broker", Toast.LENGTH_SHORT).show();
                suscripcion = "";

                break;

            case 3:

                Toast.makeText(context, "No se ha podido establecer la conexión con el broker ", Toast.LENGTH_SHORT).show();
                suscripcion = "";

                break;

        }

    }

    public String obtenerMAC(String topic) {

        String mac;
        mac = topic;
        mac = mac.replace("ewpe-smart/", "");
        mac = mac.replace("/status", "");

        return mac;


    }

    public void desconectar() {

        if(mqttAndroidClient.isConnected()) {

            try {
                mqttAndroidClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }

        }

    }

    public  ArrayList<String> getMacs() {
        return macs;
    }

    public void setMacs(ArrayList<String> macs) {
        this.macs = macs;
    }

    public ArrayList<AireAcondicionado> getAircoParams() {
        return aircoParams;
    }

    public void setAircoParams(ArrayList aircoParams) {
        this.aircoParams = aircoParams;
    }
}

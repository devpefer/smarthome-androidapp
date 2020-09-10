package com.example.smarthome;

import android.content.Context;
import android.util.Log;


import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

public class MQTTUtils {

    public Context context;

    private MqttAndroidClient mqttAndroidClient;

    private ArrayList<String> macs = new ArrayList<>();

    private String serverURI;
    private String clientId;

    public MQTTUtils(final Context context, String serverURI, String clientId) {

        this.context = context;
        this.serverURI = serverURI;
        this.clientId = clientId;

    }


        public void conectar (final String topic) {

            this.mqttAndroidClient = new MqttAndroidClient(context, serverURI, clientId);

            this.mqttAndroidClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.i("AIRE", "connection lost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {

                    procesarMensaje(topic, message);

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.i("AIRE", "msg delivered");
                }
            });


            if (!mqttAndroidClient.isConnected()) {
                try {
                    mqttAndroidClient.connect(null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {

                            subscribeTopic(topic);

                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {


                        }

                    });

                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }

        public void desconectar () {
            try {
                mqttAndroidClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }

        }

        public void publishMessage (String topic, String payload){
            try {

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
                e.printStackTrace();
            }
        }

        public void subscribeTopic (final String topic){

            try {
                this.mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {

                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    }
                });

            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        private void procesarMensaje (String topic, MqttMessage message){

            Gson gson = new Gson();

            String mac = procesarMAC(topic);

            String payLoad = new String(message.getPayload());


            if (topic.equals("ewpe-smart/" + mac + "/status")) {

                AireAcondicionado aircoParamsTemp;
                aircoParamsTemp = gson.fromJson(payLoad, AireAcondicionado.class);

                aircoParamsTemp.setMac(mac);

                for (int i = 0; i < PlanoActivity.getAircoParams().size(); i++) {

                    if (PlanoActivity.getAircoParams().get(i).getMac().equals(aircoParamsTemp.getMac())) {

                        PlanoActivity.getAircoParams().get(i).setAir(aircoParamsTemp.getAir());
                        PlanoActivity.getAircoParams().get(i).setBlo(aircoParamsTemp.getBlo());
                        PlanoActivity.getAircoParams().get(i).setHealth(aircoParamsTemp.getHealth());
                        PlanoActivity.getAircoParams().get(i).setLig(aircoParamsTemp.getLig());
                        PlanoActivity.getAircoParams().get(i).setMod(aircoParamsTemp.getMod());
                        PlanoActivity.getAircoParams().get(i).setPow(aircoParamsTemp.getPow());
                        PlanoActivity.getAircoParams().get(i).setQuiet(aircoParamsTemp.getQuiet());
                        PlanoActivity.getAircoParams().get(i).setSetTem(aircoParamsTemp.getSetTem());
                        PlanoActivity.getAircoParams().get(i).setSvSt(aircoParamsTemp.getSvSt());
                        PlanoActivity.getAircoParams().get(i).setSwhSlp(aircoParamsTemp.getSwhSlp());
                        PlanoActivity.getAircoParams().get(i).setSwingLfRig(aircoParamsTemp.getSwingLfRig());
                        PlanoActivity.getAircoParams().get(i).setSwUpDn(aircoParamsTemp.getSwUpDn());
                        PlanoActivity.getAircoParams().get(i).setTemRec(aircoParamsTemp.getTemRec());
                        PlanoActivity.getAircoParams().get(i).setTemUn(aircoParamsTemp.getTemUn());
                        PlanoActivity.getAircoParams().get(i).setTur(aircoParamsTemp.getTur());
                        PlanoActivity.getAircoParams().get(i).setWdSpd(aircoParamsTemp.getWdSpd());

                        PlanoActivity.getmAdapter().notifyDataSetChanged();
                    }

                }

            if (RemoteCtrlActivity.getAircoParams().getMac().equals(aircoParamsTemp.getMac())) {

                RemoteCtrlActivity.getAircoParams().setAir(aircoParamsTemp.getAir());
                RemoteCtrlActivity.getAircoParams().setBlo(aircoParamsTemp.getBlo());
                RemoteCtrlActivity.getAircoParams().setHealth(aircoParamsTemp.getHealth());
                RemoteCtrlActivity.getAircoParams().setLig(aircoParamsTemp.getLig());
                RemoteCtrlActivity.getAircoParams().setMod(aircoParamsTemp.getMod());
                RemoteCtrlActivity.getAircoParams().setPow(aircoParamsTemp.getPow());
                RemoteCtrlActivity.getAircoParams().setQuiet(aircoParamsTemp.getQuiet());
                RemoteCtrlActivity.getAircoParams().setSetTem(aircoParamsTemp.getSetTem());
                RemoteCtrlActivity.getAircoParams().setSvSt(aircoParamsTemp.getSvSt());
                RemoteCtrlActivity.getAircoParams().setSwhSlp(aircoParamsTemp.getSwhSlp());
                RemoteCtrlActivity.getAircoParams().setSwingLfRig(aircoParamsTemp.getSwingLfRig());
                RemoteCtrlActivity.getAircoParams().setSwUpDn(aircoParamsTemp.getSwUpDn());
                RemoteCtrlActivity.getAircoParams().setTemRec(aircoParamsTemp.getTemRec());
                RemoteCtrlActivity.getAircoParams().setTemUn(aircoParamsTemp.getTemUn());
                RemoteCtrlActivity.getAircoParams().setTur(aircoParamsTemp.getTur());
                RemoteCtrlActivity.getAircoParams().setWdSpd(aircoParamsTemp.getWdSpd());
                RemoteCtrlActivity.refrescarParams();

            }

            } else if (topic.equals("stat/" + mac + "/POWER")) {

                Sonoff sonoffTemp;
                sonoffTemp = gson.fromJson(payLoad, Sonoff.class);

                sonoffTemp.setMac(mac);

                for (int i = 0; i < PlanoActivity.getAircoParams().size(); i++) {

                    if (PlanoActivity.getAircoParams().get(i).getMac().equals(sonoffTemp.getMac())) {

                        PlanoActivity.getAircoParams().get(i).setPow(sonoffTemp.getPow());

                    }

                }

            }
        }

        private String procesarMAC (String topic){

            String mac;
            mac = topic;

            if (mac.contains("ewpe"));
            {
                mac = mac.replace("ewpe-smart/", "");
                mac = mac.replace("/status", "");

                if (!macs.contains(mac) && !mac.contains("devices/list")) {

                    macs.add(mac);

                }

                if (!BuscarActivity.getMacs().contains(mac) && !mac.contains("devices/list")) {

                    BuscarActivity.getMacs().add(mac);
                    BuscarActivity.getmAdapter().notifyDataSetChanged();

                }
            }

            return mac;

        }

    public MqttAndroidClient getMqttAndroidClient() {
        return mqttAndroidClient;
    }
}




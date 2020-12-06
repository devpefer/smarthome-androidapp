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


        public void conectar (final ArrayList<String> topics) {

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

                            for (int i = 0; i < topics.size(); i++) {
                                subscribeTopic(topics.get(i));

                            }

                            for (int i = 0; i < DeviceListActivity.getDevices().size(); i++) {

                                if (DeviceListActivity.getDevices().get(i) instanceof Sonoff) {

                                    DeviceListActivity.getMqttAndroidClient().publishMessage("cmnd/" + DeviceListActivity.getDevices().get(i).getMac() + "/POWER", "");

                                }

                            }

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

                for (int i = 0; i < DeviceListActivity.getDevices().size(); i++) {

                    if (DeviceListActivity.getDevices().get(i).getMac().equals(aircoParamsTemp.getMac())) {

                        DeviceListActivity.getDevices().get(i).setAir(aircoParamsTemp.getAir());
                        DeviceListActivity.getDevices().get(i).setBlo(aircoParamsTemp.getBlo());
                        DeviceListActivity.getDevices().get(i).setHealth(aircoParamsTemp.getHealth());
                        DeviceListActivity.getDevices().get(i).setLig(aircoParamsTemp.getLig());
                        DeviceListActivity.getDevices().get(i).setMod(aircoParamsTemp.getMod());
                        DeviceListActivity.getDevices().get(i).setPow(aircoParamsTemp.getPow());
                        DeviceListActivity.getDevices().get(i).setQuiet(aircoParamsTemp.getQuiet());
                        DeviceListActivity.getDevices().get(i).setSetTem(aircoParamsTemp.getSetTem());
                        DeviceListActivity.getDevices().get(i).setSvSt(aircoParamsTemp.getSvSt());
                        DeviceListActivity.getDevices().get(i).setSwhSlp(aircoParamsTemp.getSwhSlp());
                        DeviceListActivity.getDevices().get(i).setSwingLfRig(aircoParamsTemp.getSwingLfRig());
                        DeviceListActivity.getDevices().get(i).setSwUpDn(aircoParamsTemp.getSwUpDn());
                        DeviceListActivity.getDevices().get(i).setTemRec(aircoParamsTemp.getTemRec());
                        DeviceListActivity.getDevices().get(i).setTemUn(aircoParamsTemp.getTemUn());
                        DeviceListActivity.getDevices().get(i).setTur(aircoParamsTemp.getTur());
                        DeviceListActivity.getDevices().get(i).setWdSpd(aircoParamsTemp.getWdSpd());

                        DeviceListActivity.getmAdapter().notifyDataSetChanged();
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
                for (int i = 0; i < DeviceListActivity.getDevices().size(); i++) {

                    if (DeviceListActivity.getDevices().get(i).getMac().equals(mac)) {
                        Log.i("ESTA ES LA MAC",DeviceListActivity.getDevices().get(i).getMac());
                        DeviceListActivity.getDevices().get(i).setPow(payLoad);
                        DeviceListActivity.getmAdapter().notifyDataSetChanged();
                    }

                }

            } else if (topic.equals("pyrpi/youtube/listaFicheros")) {

                String[] listaFicheros;
                listaFicheros=payLoad.split(";");

                PlayerActivity.getArchivos().clear();

                for (int i = 0; i < listaFicheros.length; i++) {

                    PlayerActivity.getArchivos().add(listaFicheros[i]);

                }

                PlayerActivity.getmAdapter().notifyDataSetChanged();
            }
        }

        private String procesarMAC (String topic){

            String mac;
            mac = topic;

            if (mac.contains("ewpe")) {
                mac = mac.replace("ewpe-smart/", "");
                mac = mac.replace("/status", "");

                if (!macs.contains(mac) && !mac.contains("devices/list")) {

                    macs.add(mac);

                }

                if (!BuscarActivity.getMacs().contains(mac) && !mac.contains("devices/list")) {

                    BuscarActivity.getMacs().add(mac);
                    BuscarActivity.getmAdapter().notifyDataSetChanged();

                }
            } else if (mac.contains("stat/") && (mac.contains("/POWER"))) {

                mac = mac.replace("stat/","");
                mac = mac.replace("/POWER","");

                macs.add(mac);

            }


            return mac;

        }

    public MqttAndroidClient getMqttAndroidClient() {
        return mqttAndroidClient;
    }
}




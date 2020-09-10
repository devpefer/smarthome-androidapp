package com.example.smarthome;

public class Sonoff extends MQTTDevice {

    private String pow ="OFF";
    private String mac;
    private String name;

    public void setPow(String pow) {

        this.pow = pow;

    }

    public String getPow() {

        return this.pow;

    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.example.smarthome.tasmota;

public class Sonoff extends Tasmota {

    private String pow ="OFF";
    private String mac = "";
    private String name = "";

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

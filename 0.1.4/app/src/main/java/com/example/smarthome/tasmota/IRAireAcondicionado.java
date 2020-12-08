package com.example.smarthome.tasmota;

public class IRAireAcondicionado extends Tasmota {

    String name="";
    String mac="";

    public String getName() {
        return name;
    }

    public void setName(String nombre) {
        this.name = nombre;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}

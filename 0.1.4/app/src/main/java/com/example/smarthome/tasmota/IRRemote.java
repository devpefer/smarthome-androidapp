package com.example.smarthome.tasmota;

public class IRRemote extends Tasmota {

    String nombre;
    String mac;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

}

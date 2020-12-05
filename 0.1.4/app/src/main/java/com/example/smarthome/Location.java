package com.example.smarthome;

public class Location {

    private String nombre;
    private String serverURI;
    private String clientId;



    public Location(String nombre, String serverURI, String clientId) {

        this.nombre = nombre;
        this.serverURI = serverURI;
        this.clientId = clientId;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getServerURI() {
        return serverURI;
    }

    public void setServerURI(String serverURI) {
        this.serverURI = serverURI;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}

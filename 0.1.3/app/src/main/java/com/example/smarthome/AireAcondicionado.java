package com.example.smarthome;

public class AireAcondicionado extends MQTTDevice {

    private String Pow = "0";
    private String Mod = "0";
    private String TemUn = "0";
    private String SetTem = "0";
    private String TemRec = "0";
    private String WdSpd = "0";
    private String Air = "0";
    private String Blo = "0";
    private String Health = "0";
    private String SwhSlp = "0";
    private String Lig = "0";
    private String SwingLfRig = "0";
    private String SwUpDn = "0";
    private String Quiet = "0";
    private String Tur = "0";
    private String SvSt = "0";
    private String mac = "0";
    private String name = "0";

    public String getPow() {
        return Pow;
    }

    public void setPow(String pow) {
        Pow = pow;
    }

    public String getMod() {
        return Mod;
    }

    public void setMod(String mod) {
        Mod = mod;
    }

    public String getTemUn() {
        return TemUn;
    }

    public void setTemUn(String temUn) {
        TemUn = temUn;
    }

    public String getSetTem() {
        return SetTem;
    }

    public void setSetTem(String setTem) {
        SetTem = setTem;
    }

    public String getTemRec() {
        return TemRec;
    }

    public void setTemRec(String temRec) {
        TemRec = temRec;
    }

    public String getWdSpd() {
        return WdSpd;
    }

    public void setWdSpd(String wdSpd) {
        WdSpd = wdSpd;
    }

    public String getAir() {
        return Air;
    }

    public void setAir(String air) {
        Air = air;
    }

    public String getBlo() {
        return Blo;
    }

    public void setBlo(String blo) {
        Blo = blo;
    }

    public String getHealth() {
        return Health;
    }

    public void setHealth(String health) {
        Health = health;
    }

    public String getSwhSlp() {
        return SwhSlp;
    }

    public void setSwhSlp(String swhSlp) {
        SwhSlp = swhSlp;
    }

    public String getLig() {
        return Lig;
    }

    public void setLig(String lig) {
        Lig = lig;
    }

    public String getSwingLfRig() {
        return SwingLfRig;
    }

    public void setSwingLfRig(String swingLfRig) {
        SwingLfRig = swingLfRig;
    }

    public String getSwUpDn() {
        return SwUpDn;
    }

    public void setSwUpDn(String swUpDn) {
        SwUpDn = swUpDn;
    }

    public String getQuiet() {
        return Quiet;
    }

    public void setQuiet(String quiet) {
        Quiet = quiet;
    }

    public String getTur() {
        return Tur;
    }

    public void setTur(String tur) {
        Tur = tur;
    }

    public String getSvSt() {
        return SvSt;
    }

    public void setSvSt(String svSt) {
        SvSt = svSt;
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

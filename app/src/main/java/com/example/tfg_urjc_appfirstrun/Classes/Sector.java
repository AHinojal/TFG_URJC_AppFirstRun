package com.example.tfg_urjc_appfirstrun.Classes;

import java.util.UUID;

public class Sector {
    private String uuid;
    private int number;
    // Estos valores vienen en milliseconds -> En la vista hay que tranformarlos parseandolos "mm:ss"
    private float goalTime;
    private float registerTime;
    private float difference;

    public Sector () {
        this.uuid = UUID.randomUUID().toString();
        this.number = 0;
        this.goalTime = 0;
        this.registerTime = 0;
        this.difference = 0;
    }

    public Sector (int number, float goalTime){
        this.uuid = UUID.randomUUID().toString();
        this.number = number;
        this.goalTime = goalTime;
        this.registerTime = 0;
        this.difference = 0;
    }

    public String getUuid() {
        return uuid;
    }

    public float getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(float registerTime) {
        this.registerTime = registerTime;
        this.difference = this.goalTime - this.registerTime;
    }
}

package com.example.tfg_urjc_appfirstrun.Classes;

import java.util.UUID;

public class Sector {
    public String _id;
    public int number;
    // Estos valores vienen en milliseconds -> En la vista hay que tranformarlos parseandolos "mm:ss"
    public float goalTime;
    public float registerTime;
    public float difference;

    public Sector () {
        this._id = UUID.randomUUID().toString();
        this.number = 0;
        this.goalTime = 0;
        this.registerTime = 0;
        this.difference = 0;
    }

    public Sector (int number, float goalTime){
        this._id = UUID.randomUUID().toString();
        this.number = number;
        this.goalTime = goalTime;
        this.registerTime = 0;
        this.difference = 0;
    }

    public void updateRegisterTime(float registerTime) {
        this.registerTime = registerTime;
        this.difference = this.goalTime - this.registerTime;
    }

}

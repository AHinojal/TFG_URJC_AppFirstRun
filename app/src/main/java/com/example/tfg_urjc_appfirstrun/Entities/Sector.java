package com.example.tfg_urjc_appfirstrun.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "sectors")
public class Sector {
    @PrimaryKey
    @NonNull
    public String sectorId;
    public String sessionOwnerId;
    @ColumnInfo(name = "number_sector")
    public int numberSector;
    // Estos valores vienen en milliseconds -> En la vista hay que tranformarlos parseandolos "mm:ss"
    @ColumnInfo(name = "goal_time")
    public float goalTime;
    @ColumnInfo(name = "register_time")
    public float registerTime;
    @ColumnInfo(name = "difference")
    public float difference;

    public Sector (String sessionOwnerId, int numberSector, float goalTime){
        this.sectorId = UUID.randomUUID().toString();
        this.sessionOwnerId = sessionOwnerId;
        this.numberSector = numberSector;
        this.goalTime = goalTime;
        this.registerTime = 0;
        this.difference = 0;
    }

    public void updateRegisterTime(float registerTime) {
        this.registerTime = registerTime;
        this.difference = this.goalTime - this.registerTime;
    }

}

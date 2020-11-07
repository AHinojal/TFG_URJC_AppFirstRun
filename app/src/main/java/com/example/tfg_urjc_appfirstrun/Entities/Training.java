package com.example.tfg_urjc_appfirstrun.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Entity (tableName = "trainings")
public class Training {
    @PrimaryKey
    @NonNull
    public String trainingId;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "start_date")
    public Date startDate;
    @ColumnInfo(name = "is_Finished")
    public boolean isFinished;
    @ColumnInfo(name = "type_training")
    public String typeTraining;
    @ColumnInfo(name = "mark_5km")
    public String mark5Km;

    public Training () {
        this.trainingId = UUID.randomUUID().toString();
        this.name = null;
        this.startDate = null;
        this.isFinished = false;
        this.typeTraining = null;
        this.mark5Km = null;
    }

    public Training(String name, Date startDate, String typeTraining, String mark5Km){
        this.trainingId = UUID.randomUUID().toString();
        this.name = name;
        this.startDate = startDate;
        this.isFinished = false;
        this.typeTraining = typeTraining;
        this.mark5Km = mark5Km;
    }

    public String getTrainingId() {
        return trainingId;
    }
}

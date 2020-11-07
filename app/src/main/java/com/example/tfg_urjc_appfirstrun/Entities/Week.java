package com.example.tfg_urjc_appfirstrun.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "weeks")
public class Week {
    @PrimaryKey
    @NonNull
    public String weekId;
    @ColumnInfo(name = "training_owner_Id")
    public String trainingOwnerId;
    @ColumnInfo(name = "number_week")
    public int numberWeek;

    public Week (String trainingOwnerId, int numberWeek) {
        this.weekId = UUID.randomUUID().toString();
        this.trainingOwnerId = trainingOwnerId;
        this.numberWeek = numberWeek;
    }

    public String getWeekId() {
        return weekId;
    }

}

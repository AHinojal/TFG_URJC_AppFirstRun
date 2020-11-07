package com.example.tfg_urjc_appfirstrun.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity(tableName = "sessions")
public class Session {
    @PrimaryKey
    @NonNull
    public String sessionId;
    @ColumnInfo(name = "week_owner_id")
    public String weekOwnerId;
    @ColumnInfo(name = "number_session")
    public int numberSession;
    @ColumnInfo(name = "replays")
    public int replays;
    @ColumnInfo(name = "distance")
    public List<String> distance;
    @ColumnInfo(name = "session_day")
    public Date sessionDay;
    @ColumnInfo(name = "recovery_time")
    public String recoveryTime;

    public Session (String weekOwnerId, int numberSession, int replays, List<String> distance, Date sessionDay, String recoveryTime){
        this.sessionId = UUID.randomUUID().toString();
        this.weekOwnerId = weekOwnerId;
        this.numberSession = numberSession;
        this.replays = replays;
        this.distance = distance;
        this.sessionDay = sessionDay;
        this.recoveryTime = recoveryTime;
    }

    public String getSessionId() {
        return sessionId;
    }
}

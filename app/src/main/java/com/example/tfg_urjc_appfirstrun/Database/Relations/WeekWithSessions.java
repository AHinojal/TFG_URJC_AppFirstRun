package com.example.tfg_urjc_appfirstrun.Database.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.tfg_urjc_appfirstrun.Entities.Session;
import com.example.tfg_urjc_appfirstrun.Entities.Week;

import java.util.List;

public class WeekWithSessions {
    @Embedded
    public Week week;
    @Relation(
            parentColumn = "weekId",
            entityColumn = "weekOwnerId"
    )
    public List<Session> session;
}

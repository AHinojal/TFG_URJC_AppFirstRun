package com.example.tfg_urjc_appfirstrun.Database.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.tfg_urjc_appfirstrun.Entities.Sector;
import com.example.tfg_urjc_appfirstrun.Entities.Session;

import java.util.List;

public class SessionWithSectors {
    @Embedded
    public Session session;
    @Relation(
            parentColumn = "sessionId",
            entityColumn = "sessionOwnerId"
    )
    public List<Sector> sectors;
}


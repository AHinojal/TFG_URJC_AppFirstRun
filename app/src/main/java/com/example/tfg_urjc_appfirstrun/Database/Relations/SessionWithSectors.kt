package com.example.tfg_urjc_appfirstrun.Database.Relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.tfg_urjc_appfirstrun.Entities.Sector
import com.example.tfg_urjc_appfirstrun.Entities.Session

class SessionWithSectors {
    @Embedded
    var session: Session? = null

    @Relation(parentColumn = "sessionId", entityColumn = "sessionOwnerId")
    var sectors: MutableList<Sector?>? = null
}
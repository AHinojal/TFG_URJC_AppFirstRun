package com.example.tfg_urjc_appfirstrun.Database.Relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.tfg_urjc_appfirstrun.Entities.Session
import com.example.tfg_urjc_appfirstrun.Entities.Week

class WeekWithSessions {
    @Embedded
    var week: Week? = null

    @Relation(parentColumn = "weekId", entityColumn = "weekOwnerId")
    var session: MutableList<Session?>? = null
}
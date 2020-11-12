package com.example.tfg_urjc_appfirstrun.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity(tableName = "sessions")
class Session(weekOwnerId: String?, numberSession: Int, replays: Int, distance: String?, sessionDay: Date?, recoveryTime: String?) {
    @PrimaryKey
    @NotNull
    var sessionId: String

    @ColumnInfo(name = "week_owner_id")
    var weekOwnerId: String?

    @ColumnInfo(name = "number_session")
    var numberSession: Int

    @ColumnInfo(name = "replays")
    var replays: Int

    @ColumnInfo(name = "distance")
    var distance: String?

    @ColumnInfo(name = "session_day")
    var sessionDay: Date?

    @ColumnInfo(name = "recovery_time")
    var recoveryTime: String?


    init {
        sessionId = UUID.randomUUID().toString()
        this.weekOwnerId = weekOwnerId
        this.numberSession = numberSession
        this.replays = replays
        this.distance = distance
        this.sessionDay = sessionDay
        this.recoveryTime = recoveryTime
    }
}
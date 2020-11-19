package com.example.tfg_urjc_appfirstrun.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity(tableName = "sectors")
class Sector(sessionOwnerId: String?, numberSector: Int, distance: String, goalTime: Float) {
    @PrimaryKey
    @NotNull
    var sectorId: String

    @ColumnInfo(name = "session_owner_id")
    var sessionOwnerId: String?

    @ColumnInfo(name = "number_sector")
    var numberSector: Int

    @ColumnInfo(name = "distance")
    var distance: String?

    // Estos valores vienen en milliseconds -> En la vista hay que tranformarlos parseandolos "mm:ss"
    @ColumnInfo(name = "goal_time")
    var goalTime: Float

    // Estos valores vienen en milliseconds -> En la vista hay que tranformarlos parseandolos "mm:ss"
    @ColumnInfo(name = "register_time")
    var registerTime: Float

    @ColumnInfo(name = "difference")
    var difference: Float

    init {
        this.sectorId = UUID.randomUUID().toString()
        this.sessionOwnerId = sessionOwnerId
        this.numberSector = numberSector
        this.distance = distance
        this.goalTime = goalTime
        this.registerTime = 0f
        this.difference = 0f
    }

    override fun toString(): String {
        return "Sector(sectorId='$sectorId', sessionOwnerId=$sessionOwnerId, numberSector=$numberSector, goalTime=$goalTime, registerTime=$registerTime, difference=$difference)"
    }


}
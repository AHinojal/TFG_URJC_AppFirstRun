package com.example.tfg_urjc_appfirstrun.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity(tableName = "sectors")
class Sector(sessionOwnerId: String?, numberSector: Int, goalTime: Long) {
    @PrimaryKey
    @NotNull
    var sectorId: String

    @ColumnInfo(name = "session_owner_")
    var sessionOwnerId: String?

    @ColumnInfo(name = "number_sector")
    var numberSector: Int

    // Estos valores vienen en milliseconds -> En la vista hay que tranformarlos parseandolos "mm:ss"
    @ColumnInfo(name = "goal_time")
    var goalTime: Float

    @ColumnInfo(name = "register_time")
    var registerTime: Float

    @ColumnInfo(name = "difference")
    var difference: Float

    init {
        sectorId = UUID.randomUUID().toString()
        this.sessionOwnerId = sessionOwnerId
        this.numberSector = numberSector
        this.goalTime = goalTime.toFloat()
        registerTime = 0f
        difference = 0f
    }
}
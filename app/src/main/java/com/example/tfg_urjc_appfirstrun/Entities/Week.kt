package com.example.tfg_urjc_appfirstrun.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity(tableName = "weeks")
class Week(trainingOwnerId: String?, numberWeek: Int) {
    @PrimaryKey
    @NotNull
    var weekId: String

    @ColumnInfo(name = "training_owner_Id")
    var trainingOwnerId: String?

    @ColumnInfo(name = "number_week")
    var numberWeek: Int

    init {
        weekId = UUID.randomUUID().toString()
        this.trainingOwnerId = trainingOwnerId
        this.numberWeek = numberWeek
    }
}
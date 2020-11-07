package com.example.tfg_urjc_appfirstrun.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity(tableName = "trainings")
class Training(name: String?, startDate: Date?, typeTraining: String?, mark5Km: String?){
    @PrimaryKey
    @NotNull
    var trainingId: String

    @ColumnInfo(name = "name")
    var name: String?

    @ColumnInfo(name = "start_date")
    var startDate: Date?

    @ColumnInfo(name = "is_Finished")
    var isFinished: Boolean

    @ColumnInfo(name = "type_training")
    var typeTraining: String?

    @ColumnInfo(name = "mark_5km")
    var mark5Km: String?

    init {
        trainingId = UUID.randomUUID().toString()
        this.name = name
        this.startDate = startDate
        isFinished = false
        this.typeTraining = typeTraining
        this.mark5Km = mark5Km
    }
}
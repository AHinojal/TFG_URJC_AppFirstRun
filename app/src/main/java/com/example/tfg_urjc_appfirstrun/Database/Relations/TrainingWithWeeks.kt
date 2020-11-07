package com.example.tfg_urjc_appfirstrun.Database.Relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.tfg_urjc_appfirstrun.Entities.Training
import com.example.tfg_urjc_appfirstrun.Entities.Week

class TrainingWithWeeks {
    @Embedded
    var training: Training? = null

    @Relation(parentColumn = "trainingId", entityColumn = "trainingOwnerId")
    var weeks: MutableList<Week?>? = null
}
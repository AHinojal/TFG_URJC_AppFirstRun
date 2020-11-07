package com.example.tfg_urjc_appfirstrun.Database.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.example.tfg_urjc_appfirstrun.Entities.Training;
import com.example.tfg_urjc_appfirstrun.Entities.Week;

import java.util.List;

public class TrainingWithWeeks {
    @Embedded
    public Training training;
    @Relation(
            parentColumn = "trainingId",
            entityColumn = "trainingOwnerId"
    )
    public List<Week> weeks;
}

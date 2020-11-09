package com.example.tfg_urjc_appfirstrun.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tfg_urjc_appfirstrun.Entities.Training

@Dao
interface TrainingDao {
    @Query("SELECT * FROM trainings")
    fun getAll(): List<Training?>?

    @Query("SELECT * FROM trainings WHERE trainingId = :trainingId")
    suspend fun getById(trainingId: String?): Training?

    @Query("SELECT * FROM trainings WHERE is_Actual_Training = 1")
    fun getActualTraining(): Training?

    @Insert
    suspend fun insert(training: Training?)

    @Delete
    suspend fun delete(training: Training?)
}
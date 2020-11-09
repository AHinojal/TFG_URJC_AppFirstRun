package com.example.tfg_urjc_appfirstrun.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tfg_urjc_appfirstrun.Entities.Week

@Dao
interface WeekDao {
    @Query("SELECT * FROM weeks")
    suspend fun getAll(): MutableList<Week?>?

    @Query("SELECT * FROM weeks WHERE training_owner_Id = :trainingId ORDER BY number_week ASC")
    suspend fun getWeekByTrainingId(trainingId: String): List<Week?>?

    @Query("SELECT * FROM weeks WHERE weekId = :weekId")
    suspend fun getById(weekId: String?): Week?

    @Insert
    suspend fun insertAll(vararg week: Week?)

    @Delete
    suspend fun delete(week: Week?)
}
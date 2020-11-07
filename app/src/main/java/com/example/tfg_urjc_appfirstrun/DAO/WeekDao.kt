package com.example.tfg_urjc_appfirstrun.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tfg_urjc_appfirstrun.Entities.Week

@Dao
interface WeekDao {
    @Query("SELECT * FROM weeks")
    open fun getAll(): MutableList<Week?>?
    @Query("SELECT * FROM weeks WHERE weekId = :weekId")
    open fun getById(weekId: String?): Week?
    @Insert
    open fun insertAll(vararg week: Week?)
    @Delete
    open fun delete(week: Week?)
}
package com.example.tfg_urjc_appfirstrun.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tfg_urjc_appfirstrun.Entities.Session
import com.example.tfg_urjc_appfirstrun.Entities.Week

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions")
    suspend fun getAll(): MutableList<Session?>?
    @Query("SELECT * FROM sessions WHERE sessionId = :sessionId")
    suspend fun getById(sessionId: String?): Session?
    @Query("SELECT * FROM sessions WHERE week_owner_id = :weekId ORDER BY number_session ASC")
    suspend fun getSessionByWeekId(weekId: String): List<Session?>?
    @Insert
    suspend fun insertAll(vararg session: Session?)
    @Delete
    suspend fun delete(session: Session?)
}
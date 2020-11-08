package com.example.tfg_urjc_appfirstrun.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tfg_urjc_appfirstrun.Entities.Session

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions")
    suspend fun getAll(): MutableList<Session?>?
    @Query("SELECT * FROM sessions WHERE sessionId = :sessionId")
    suspend fun getById(sessionId: String?): Session?
    @Insert
    suspend fun insertAll(vararg session: Session?)
    @Delete
    suspend fun delete(session: Session?)
}
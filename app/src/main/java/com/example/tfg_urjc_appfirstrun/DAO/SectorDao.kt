package com.example.tfg_urjc_appfirstrun.DAO

import androidx.room.*
import com.example.tfg_urjc_appfirstrun.Entities.Sector
import com.example.tfg_urjc_appfirstrun.Entities.Session

@Dao
interface SectorDao {
    @Query("SELECT * FROM sectors")
    suspend fun getAll(): MutableList<Sector?>?
    @Query("SELECT * FROM sectors WHERE sectorId = :sectorId")
    suspend fun getById(sectorId: String?): Sector?
    @Query("SELECT * FROM sectors WHERE session_owner_id = :sessionId ORDER BY number_sector ASC")
    open fun getSectorBySessionId(sessionId: String): List<Sector?>?
    @Insert
    suspend fun insertAll(vararg sector: Sector?)

    @Update
    suspend fun update(sector: Sector)

    @Delete
    suspend fun delete(sector: Sector?)
}
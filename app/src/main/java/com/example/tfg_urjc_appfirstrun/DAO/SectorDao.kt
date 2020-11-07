package com.example.tfg_urjc_appfirstrun.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tfg_urjc_appfirstrun.Entities.Sector

@Dao
interface SectorDao {
    @Query("SELECT * FROM sectors")
    suspend fun getAll(): MutableList<Sector?>?
    @Query("SELECT * FROM sectors WHERE sectorId = :sectorId")
    suspend fun getById(sectorId: String?): Sector?
    @Insert
    suspend fun insertAll(vararg sector: Sector?)
    @Delete
    suspend fun delete(sector: Sector?)
}
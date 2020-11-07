package com.example.tfg_urjc_appfirstrun.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tfg_urjc_appfirstrun.Entities.Sector;
import com.example.tfg_urjc_appfirstrun.Entities.Training;

import java.util.List;

@Dao
public interface SectorDao {
    @Query("SELECT * FROM sectors")
    List<Sector> getAll();

    @Query("SELECT * FROM sectors WHERE sectorId = :sectorId")
    Sector getById(String sectorId);

    @Insert
    void insertAll(Sector... sector);

    @Delete
    void delete(Sector sector);

}

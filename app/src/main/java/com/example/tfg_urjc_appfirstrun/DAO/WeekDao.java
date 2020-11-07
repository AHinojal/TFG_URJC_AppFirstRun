package com.example.tfg_urjc_appfirstrun.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tfg_urjc_appfirstrun.Entities.Training;
import com.example.tfg_urjc_appfirstrun.Entities.Week;

import java.util.List;

@Dao
public interface WeekDao {

    @Query("SELECT * FROM weeks")
    List<Week> getAll();

    @Query("SELECT * FROM weeks WHERE weekId = :weekId")
    Week getById(String weekId);

    @Insert
    void insertAll(Week... week);

    @Delete
    void delete(Week week);
}

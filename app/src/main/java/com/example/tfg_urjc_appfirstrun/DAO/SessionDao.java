package com.example.tfg_urjc_appfirstrun.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tfg_urjc_appfirstrun.Entities.Session;
import com.example.tfg_urjc_appfirstrun.Entities.Training;

import java.util.List;

@Dao
public interface SessionDao {
    @Query("SELECT * FROM sessions")
    List<Session> getAll();

    @Query("SELECT * FROM sessions WHERE sessionId = :sessionId")
    Session getById(String sessionId);

    @Insert
    void insertAll(Session... session);

    @Delete
    void delete(Session session);

}

package com.example.tfg_urjc_appfirstrun.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tfg_urjc_appfirstrun.Entities.Training;

import java.util.List;

@Dao
public interface TrainingDao {

    @Query("SELECT * FROM trainings")
    List<Training> getAll();

    @Query("SELECT * FROM trainings WHERE trainingId = :trainingId")
    Training getById(String trainingId);

    @Insert
    void insert(Training training);

    @Delete
    void delete(Training training);


}

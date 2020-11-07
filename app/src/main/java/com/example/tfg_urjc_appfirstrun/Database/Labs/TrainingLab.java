package com.example.tfg_urjc_appfirstrun.Database.Labs;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import com.example.tfg_urjc_appfirstrun.DAO.TrainingDao;
import com.example.tfg_urjc_appfirstrun.Database.TrainingDatabase;
import com.example.tfg_urjc_appfirstrun.Entities.Training;

import java.util.List;

public class TrainingLab {
    @SuppressLint("StaticFieldLeak")
    private static TrainingLab sTrainingLab;

    private TrainingDao mTrainingDao;

    private TrainingLab(Context context) {
        Context appContext = context.getApplicationContext();
        TrainingDatabase db = Room.databaseBuilder(appContext,
                TrainingDatabase.class, "tfg_database").build();
        mTrainingDao = db.trainingDao();
    }

    public static TrainingLab get(Context context) {
        if (sTrainingLab == null) {
            sTrainingLab = new TrainingLab(context);
        }
        return sTrainingLab;
    }

    public List<Training> getTraining() {
        return mTrainingDao.getAll();
    }

    public Training getTrainingById(String id) {
        return mTrainingDao.getById(id);
    }

    public void addTraining(Training training) {
        mTrainingDao.insert(training);
    }

    /*public void updateTraining(Training training) {
        mNotaDao.updateNota(training);
    }*/

    public void deleteTraining(Training training) {
        mTrainingDao.delete(training);
    }
}

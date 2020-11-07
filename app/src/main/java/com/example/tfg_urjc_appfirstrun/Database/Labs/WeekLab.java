package com.example.tfg_urjc_appfirstrun.Database.Labs;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import com.example.tfg_urjc_appfirstrun.DAO.WeekDao;
import com.example.tfg_urjc_appfirstrun.Database.TrainingDatabase;
import com.example.tfg_urjc_appfirstrun.Entities.Training;
import com.example.tfg_urjc_appfirstrun.Entities.Week;

import java.util.List;

public class WeekLab {
    @SuppressLint("StaticFieldLeak")
    private static WeekLab sWeekLab;

    private WeekDao mWeekDao;

    private WeekLab(Context context) {
        Context appContext = context.getApplicationContext();
        TrainingDatabase db = Room.databaseBuilder(appContext,
                TrainingDatabase.class, "tfg_database").build();
        mWeekDao = db.weekDao();
    }

    public static WeekLab get(Context context) {
        if (sWeekLab == null) {
            sWeekLab = new WeekLab(context);
        }
        return sWeekLab;
    }

    public List<Week> getWeek() {
        return mWeekDao.getAll();
    }

    public Week getWeekById(String id) {
        return mWeekDao.getById(id);
    }

    public void addWeek(Week week) {
        mWeekDao.insertAll(week);
    }

    /*public void updateTraining(Training training) {
        mNotaDao.updateNota(training);
    }*/

    public void deleteWeek(Week week) {
        mWeekDao.delete(week);
    }
}

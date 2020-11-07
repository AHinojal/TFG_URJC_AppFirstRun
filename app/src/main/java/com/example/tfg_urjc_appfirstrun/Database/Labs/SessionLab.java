package com.example.tfg_urjc_appfirstrun.Database.Labs;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import com.example.tfg_urjc_appfirstrun.DAO.SessionDao;
import com.example.tfg_urjc_appfirstrun.Database.TrainingDatabase;
import com.example.tfg_urjc_appfirstrun.Entities.Session;
import com.example.tfg_urjc_appfirstrun.Entities.Week;

import java.util.List;

public class SessionLab {
    @SuppressLint("StaticFieldLeak")
    private static SessionLab sSessionLab;

    private SessionDao mSessionDao;

    private SessionLab(Context context) {
        Context appContext = context.getApplicationContext();
        TrainingDatabase db = Room.databaseBuilder(appContext,
                TrainingDatabase.class, "tfg_database").build();
        mSessionDao = db.sessionDao();
    }

    public static SessionLab get(Context context) {
        if (sSessionLab == null) {
            sSessionLab = new SessionLab(context);
        }
        return sSessionLab;
    }

    public List<Session> getSession() {
        return mSessionDao.getAll();
    }

    public Session getSessionById(String id) {
        return mSessionDao.getById(id);
    }

    public void addSession(Session session) {
        mSessionDao.insertAll(session);
    }

    /*public void updateTraining(Training training) {
        mNotaDao.updateNota(training);
    }*/

    public void deleteSession(Session session) {
        mSessionDao.delete(session);
    }
}

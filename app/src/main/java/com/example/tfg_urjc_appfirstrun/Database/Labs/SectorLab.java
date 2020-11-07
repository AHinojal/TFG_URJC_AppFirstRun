package com.example.tfg_urjc_appfirstrun.Database.Labs;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import com.example.tfg_urjc_appfirstrun.DAO.SectorDao;
import com.example.tfg_urjc_appfirstrun.Database.TrainingDatabase;
import com.example.tfg_urjc_appfirstrun.Entities.Sector;

import java.util.List;

public class SectorLab {
    @SuppressLint("StaticFieldLeak")
    private static SectorLab sSectorLab;

    private SectorDao mSectorDao;

    private SectorLab(Context context) {
        Context appContext = context.getApplicationContext();
        TrainingDatabase db = Room.databaseBuilder(appContext,
                TrainingDatabase.class, "tfg_database").build();
        mSectorDao = db.sectorDao();
    }

    public static SectorLab get(Context context) {
        if (sSectorLab == null) {
            sSectorLab = new SectorLab(context);
        }
        return sSectorLab;
    }

    public List<Sector> getSector() {
        return mSectorDao.getAll();
    }

    public Sector getSectorById(String id) {
        return mSectorDao.getById(id);
    }

    public void addSector(Sector sector) {
        mSectorDao.insertAll(sector);
    }

    /*public void updateTraining(Training training) {
        mNotaDao.updateNota(training);
    }*/

    public void deleteSector(Sector sector) {
        mSectorDao.delete(sector);
    }
}

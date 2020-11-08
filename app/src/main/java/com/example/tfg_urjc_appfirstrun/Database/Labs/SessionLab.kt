package com.example.tfg_urjc_appfirstrun.Database.Labs

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import com.example.tfg_urjc_appfirstrun.DAO.SessionDao
import com.example.tfg_urjc_appfirstrun.Database.TrainingDatabase
import com.example.tfg_urjc_appfirstrun.Entities.Session

class SessionLab private constructor(context: Context?) {
    private val mSessionDao: SessionDao?
    suspend fun getSession(): MutableList<Session?>? {
        return mSessionDao?.getAll()
    }

    suspend fun getSessionById(id: String?): Session? {
        return mSessionDao?.getById(id)
    }

    suspend fun addSession(session: Session?) {
        mSessionDao?.insertAll(session)
    }

    /*public void updateTraining(Training training) {
        mNotaDao.updateNota(training);
    }*/
    suspend fun deleteSession(session: Session?) {
        mSessionDao?.delete(session)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var sSessionLab: SessionLab? = null
        operator fun get(context: Context?): SessionLab? {
            if (sSessionLab == null) {
                sSessionLab = SessionLab(context)
            }
            return sSessionLab
        }
    }

    init {
        val appContext = context!!.applicationContext
        val database: TrainingDatabase = Room.databaseBuilder(
                appContext,
                TrainingDatabase::class.java, "tfg-urjc-strava"
        ).build()
        mSessionDao = database.sessionDao()
    }
}
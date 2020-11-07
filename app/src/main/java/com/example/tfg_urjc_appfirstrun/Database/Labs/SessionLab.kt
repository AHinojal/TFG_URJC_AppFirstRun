package com.example.tfg_urjc_appfirstrun.Database.Labs

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import com.example.tfg_urjc_appfirstrun.DAO.SessionDao
import com.example.tfg_urjc_appfirstrun.Database.TrainingDatabase
import com.example.tfg_urjc_appfirstrun.Entities.Session

class SessionLab private constructor(context: Context?) {
    private val mSessionDao: SessionDao?
    fun getSession(): MutableList<Session?>? {
        return mSessionDao?.getAll()
    }

    fun getSessionById(id: String?): Session? {
        return mSessionDao?.getById(id)
    }

    fun addSession(session: Session?) {
        mSessionDao?.insertAll(session)
    }

    /*public void updateTraining(Training training) {
        mNotaDao.updateNota(training);
    }*/
    fun deleteSession(session: Session?) {
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
        val appContext = context?.getApplicationContext()
        val db = appContext?.let {
            Room.databaseBuilder(it,
                TrainingDatabase::class.java, "tfg_database").build()
        }
        mSessionDao = db?.sessionDao()
    }
}
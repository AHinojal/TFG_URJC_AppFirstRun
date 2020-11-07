package com.example.tfg_urjc_appfirstrun.Database.Labs

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import com.example.tfg_urjc_appfirstrun.DAO.WeekDao
import com.example.tfg_urjc_appfirstrun.Database.TrainingDatabase
import com.example.tfg_urjc_appfirstrun.Entities.Week

class WeekLab private constructor(context: Context?) {
    private val mWeekDao: WeekDao?
    fun getWeek(): MutableList<Week?>? {
        return mWeekDao?.getAll()
    }

    fun getWeekById(id: String?): Week? {
        return mWeekDao?.getById(id)
    }

    fun addWeek(week: Week?) {
        mWeekDao?.insertAll(week)
    }

    /*public void updateTraining(Training training) {
        mNotaDao.updateNota(training);
    }*/
    fun deleteWeek(week: Week?) {
        mWeekDao?.delete(week)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var sWeekLab: WeekLab? = null
        operator fun get(context: Context?): WeekLab? {
            if (sWeekLab == null) {
                sWeekLab = WeekLab(context)
            }
            return sWeekLab
        }
    }

    init {
        val appContext = context?.getApplicationContext()
        val db = appContext?.let {
            Room.databaseBuilder(it,
                TrainingDatabase::class.java, "tfg_database").build()
        }
        mWeekDao = db?.weekDao()
    }
}
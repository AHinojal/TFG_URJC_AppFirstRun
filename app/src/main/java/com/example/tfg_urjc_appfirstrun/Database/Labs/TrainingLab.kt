package com.example.tfg_urjc_appfirstrun.Database.Labs

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import com.example.tfg_urjc_appfirstrun.DAO.TrainingDao
import com.example.tfg_urjc_appfirstrun.Database.TrainingDatabase
import com.example.tfg_urjc_appfirstrun.Entities.Training

class TrainingLab private constructor(context: Context?) {
    private val mTrainingDao: TrainingDao?
    suspend fun getTraining(): MutableList<Training?>? {
        return mTrainingDao?.getAll()
    }

    suspend fun getTrainingById(id: String?): Training? {
        return mTrainingDao?.getById(id)
    }

    suspend fun addTraining(training: Training?) {
        mTrainingDao?.insert(training)
    }

    /*public void updateTraining(Training training) {
        mNotaDao.updateNota(training);
    }*/
    suspend fun deleteTraining(training: Training?) {
        mTrainingDao?.delete(training)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var sTrainingLab: TrainingLab? = null
        operator fun get(context: Context?): TrainingLab? {
            if (sTrainingLab == null) {
                sTrainingLab = TrainingLab(context)
            }
            return sTrainingLab
        }
    }

    init {
        val appContext = context?.getApplicationContext()
        val db = appContext?.let {
            Room.databaseBuilder(it,
                TrainingDatabase::class.java, "tfg_database").build()
        }
        mTrainingDao = db?.trainingDao()
    }
}
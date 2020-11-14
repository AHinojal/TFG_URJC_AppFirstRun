package com.example.tfg_urjc_appfirstrun.Database.Labs

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import com.example.tfg_urjc_appfirstrun.DAO.SectorDao
import com.example.tfg_urjc_appfirstrun.Database.TrainingDatabase
import com.example.tfg_urjc_appfirstrun.Entities.Sector

class SectorLab private constructor(context: Context?) {
    private val mSectorDao: SectorDao?
    suspend fun getSector(): MutableList<Sector?>? {
        return mSectorDao?.getAll()
    }

    suspend fun getSectorById(id: String?): Sector? {
        return mSectorDao?.getById(id)
    }

    fun getSectorBySessionId(sessionId: String): List<Sector?>? {
        return mSectorDao?.getSectorBySessionId(sessionId)
    }

    suspend fun addSector(sector: Sector?) {
        mSectorDao?.insertAll(sector)
    }

    suspend fun updateSector(sector: Sector) {
        mSectorDao?.update(sector);
    }

    suspend fun deleteSector(sector: Sector?) {
        mSectorDao?.delete(sector)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var sSectorLab: SectorLab? = null
        operator fun get(context: Context?): SectorLab? {
            if (sSectorLab == null) {
                sSectorLab = SectorLab(context)
            }
            return sSectorLab
        }
    }

    init {
        val appContext = context!!.applicationContext
        val database: TrainingDatabase = Room.databaseBuilder(
                appContext,
                TrainingDatabase::class.java, "tfg-urjc-strava"
        )
                .allowMainThreadQueries()
                .build()
        mSectorDao = database.sectorDao()
    }
}
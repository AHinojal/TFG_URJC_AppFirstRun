package com.example.tfg_urjc_appfirstrun.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tfg_urjc_appfirstrun.DAO.SectorDao
import com.example.tfg_urjc_appfirstrun.DAO.SessionDao
import com.example.tfg_urjc_appfirstrun.DAO.TrainingDao
import com.example.tfg_urjc_appfirstrun.DAO.WeekDao
import com.example.tfg_urjc_appfirstrun.Database.Converters.DateConverter
import com.example.tfg_urjc_appfirstrun.Database.Converters.DistanceListConverter
import com.example.tfg_urjc_appfirstrun.Entities.Sector
import com.example.tfg_urjc_appfirstrun.Entities.Session
import com.example.tfg_urjc_appfirstrun.Entities.Training
import com.example.tfg_urjc_appfirstrun.Entities.Week

@Database(entities = [Training::class, Week::class, Session::class, Sector::class], version = 1)
@TypeConverters(DateConverter::class, DistanceListConverter::class)
abstract class TrainingDatabase : RoomDatabase() {
    abstract fun trainingDao(): TrainingDao?
    abstract fun weekDao(): WeekDao?
    abstract fun sessionDao(): SessionDao?
    abstract fun sectorDao(): SectorDao?
}
package com.example.tfg_urjc_appfirstrun.Entities

class Lap(id: String, lap_index: Int, elapsed_time: Int, moving_time: Int, distance: Double){

    var id: String

    var lap_index: Int

    var elapsed_time: Int //Viene en segundos

    var moving_time: Int //Viene en segundos

    var distance: Double

    init {
        this.id = id
        this.lap_index = lap_index
        this.elapsed_time = elapsed_time
        this.moving_time = moving_time
        this.distance = distance
    }

    override fun toString(): String {
        return "Lap(id='$id', lap_index=$lap_index, elapsed_time=$elapsed_time, moving_time=$moving_time, distance=$distance)"
    }


}
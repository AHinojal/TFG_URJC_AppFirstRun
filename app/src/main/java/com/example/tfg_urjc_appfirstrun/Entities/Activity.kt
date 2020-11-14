package com.example.tfg_urjc_appfirstrun.Entities

import java.text.SimpleDateFormat
import java.util.*

class Activity(id: String, name: String, startDate: String){
    var id: String

    var name: String

    var startDate: String

    init {
        this.id = id
        this.name = name
        this.startDate = startDate
    }

    override fun toString(): String {
        val formatDateCalender = SimpleDateFormat("dd/MM/yyyy")
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val convertedDate: Date = input.parse(this.startDate);
        return this.name + " - " + formatDateCalender.format(convertedDate)
    }


}
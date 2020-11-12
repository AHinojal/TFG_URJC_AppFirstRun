package com.example.tfg_urjc_appfirstrun.Adapters

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.tfg_urjc_appfirstrun.Entities.Sector
import com.example.tfg_urjc_appfirstrun.R
import java.text.SimpleDateFormat

/**
 * [RecyclerView.Adapter] that can display a [Sector].
 * TODO: Replace the implementation with code for your data type.
 */
class MySectorRecyclerViewAdapter(
        private val values: ArrayList<Sector?>)
    : RecyclerView.Adapter<MySectorRecyclerViewAdapter.ViewHolder>() {

    val formatDateTimer = SimpleDateFormat("mm:ss")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_data_session, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.sectionNumber.text = item?.numberSector.toString()
        holder.goalTime.text = formatDateTimer.format(item?.goalTime)
        holder.registerTime.text = formatDateTimer.format(item?.registerTime)
        holder.difference.text = formatDateTimer.format(item?.difference)
        if (item?.difference!! == 0f) {
            holder.difference.setBackgroundColor(Color.YELLOW)
        } else if (item?.difference!! < 0f){
            holder.difference.setBackgroundColor(Color.GREEN)
        } else {
            holder.difference.setBackgroundColor(Color.RED)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sectionNumber: TextView = view.findViewById(R.id.sector_number)
        val goalTime: TextView = view.findViewById(R.id.tv_goalTime)
        val registerTime: TextView = view.findViewById(R.id.tv_registerTime)
        val difference: TextView = view.findViewById(R.id.tv_difference)

        override fun toString(): String {
            return super.toString() + " '" + sectionNumber.text + "'"
        }
    }
}
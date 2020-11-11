package com.example.tfg_urjc_appfirstrun.Adapters

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.tfg_urjc_appfirstrun.Entities.Training
import com.example.tfg_urjc_appfirstrun.R

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyTrainingRecyclerViewAdapter(
        private val values: List<Training>)
    : RecyclerView.Adapter<MyTrainingRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_historical_training, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i("Data Recycler View", values.toString())
        val item = values[position]
        holder.idView.text = item.name
        holder.nameView.text = item.mark5Km
        holder.typeView.text = item.typeTraining
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val nameView: TextView = view.findViewById(R.id.nameTraining)
        val typeView: TextView = view.findViewById(R.id.typeTraining)

        override fun toString(): String {
            return super.toString() + " '" + nameView.text + "'" + typeView.text
        }
    }
}
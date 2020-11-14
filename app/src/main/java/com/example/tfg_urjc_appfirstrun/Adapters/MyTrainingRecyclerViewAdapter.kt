package com.example.tfg_urjc_appfirstrun.Adapters

import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_urjc_appfirstrun.Entities.Training
import com.example.tfg_urjc_appfirstrun.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

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

        val fab: FloatingActionButton = view.findViewById(R.id.deleteTraining)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "¡Entrenamiento creado!", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i("Data Recycler View", values.toString())
        val item = values[position]
        holder.idView.text = item.name
        holder.nameView.text = item.mark5Km
        holder.typeView.text = item.typeTraining
        if (item.isActualTraining){
            holder.idView.setTypeface(null, Typeface.BOLD)
            holder.nameView.setTypeface(null, Typeface.BOLD)
            holder.typeView.setTypeface(null, Typeface.BOLD)
            holder.setActualTraining.visibility = View.INVISIBLE
            //holder.deleteTraining.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val nameView: TextView = view.findViewById(R.id.nameTraining)
        val typeView: TextView = view.findViewById(R.id.typeTraining)
        var setActualTraining: ImageButton = view.findViewById(R.id.setActualTraining)
        val deleteTraining: ImageButton = view.findViewById(R.id.deleteTraining)

        override fun toString(): String {
            return super.toString() + " '" + nameView.text + "'" + typeView.text
        }
    }
}
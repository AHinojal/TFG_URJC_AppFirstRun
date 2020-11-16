package com.example.tfg_urjc_appfirstrun.Adapters

import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_urjc_appfirstrun.Database.Labs.TrainingLab
import com.example.tfg_urjc_appfirstrun.Entities.Training
import com.example.tfg_urjc_appfirstrun.R
import com.google.android.material.snackbar.Snackbar

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyTrainingRecyclerViewAdapter(
        private val values: ArrayList<Training>,
        val currentFragment: Fragment?,
        val fragmentTransaction: FragmentTransaction)
    : RecyclerView.Adapter<MyTrainingRecyclerViewAdapter.ViewHolder>() {

    var trainingDbInstance: TrainingLab? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_historical_training, parent, false)

        // Instancia de TrainingDB para inicialr la bd
        trainingDbInstance = TrainingLab.get(parent.context)

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
        }
    }

    fun deleteTraining(pos: Int, viewHolder: RecyclerView.ViewHolder){
        //var idTraining: String = values[pos].trainingId
        if (values[pos].isActualTraining){
            Snackbar.make(viewHolder.itemView, "¡El entrenamiento seleccionado no puede ser borrado al ser el actual!", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            notifyItemRemoved(pos)
        }else{
            values.removeAt(pos) // Lo borramos de la lista
            Snackbar.make(viewHolder.itemView, "¡Borrado el entrenamiento seleccionado!", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            notifyItemRemoved(pos)
        }
    }

    fun updateActualTraining(pos: Int, viewHolder: RecyclerView.ViewHolder) {
        if (values[pos].isActualTraining){ // Si es ya el actual, no actualizamos
            Snackbar.make(viewHolder.itemView, "¡Ya es el entrenamiento actual!", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            notifyDataSetChanged()
        } else { // Si no lo es, actualizamos y ponemos todos a 0
            var idTraining: String = values[pos].trainingId
            var trainings = trainingDbInstance?.getTraining()
            if (trainings != null) {
                for (t in trainings){
                    if (t!!.trainingId == idTraining) {
                        var auxTraining = t
                        auxTraining.isActualTraining = true
                        trainingDbInstance?.updateTraining(auxTraining)
                    } else {
                        var auxTraining = t
                        auxTraining.isActualTraining = false
                        trainingDbInstance?.updateTraining(auxTraining)
                    }
                }
            }
            Snackbar.make(viewHolder.itemView, "¡Actualizado el entrenamiento actual!", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            notifyDataSetChanged()
            reloadFragment()
        }
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

    private fun reloadFragment() {
        values.clear()
        fragmentTransaction.detach(currentFragment!!)
        fragmentTransaction.attach(currentFragment!!)
        fragmentTransaction.commit()
    }
}
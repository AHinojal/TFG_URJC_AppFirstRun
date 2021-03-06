package com.example.tfg_urjc_appfirstrun.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_urjc_appfirstrun.Adapters.MyTrainingRecyclerViewAdapter
import com.example.tfg_urjc_appfirstrun.Adapters.SwipeToDeleteCallback
import com.example.tfg_urjc_appfirstrun.Adapters.SwipeToUpdateActualTrainingCallback
import com.example.tfg_urjc_appfirstrun.Database.Labs.TrainingLab
import com.example.tfg_urjc_appfirstrun.Entities.Training
import com.example.tfg_urjc_appfirstrun.R


/**
 * A fragment representing a list of Trainings.
 */
class HistoricalTrainingFragment : BaseFragment(){

    private var columnCount = 1

    var trainingDbInstance: TrainingLab? = null
    var trainingList = ArrayList<Training>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instancia de TrainingDB para inicialr la bd
        trainingDbInstance = TrainingLab.get(context)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_historical_training_list, container, false)

        showBackButton(true)

        loadTrainingList()

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyTrainingRecyclerViewAdapter(trainingList, fragmentManager!!.findFragmentByTag("nextFragment"), fragmentManager!!.beginTransaction())
                var itemTouchHelperDelete = ItemTouchHelper(SwipeToDeleteCallback(adapter as MyTrainingRecyclerViewAdapter, context))
                itemTouchHelperDelete.attachToRecyclerView(view)
                var itemTouchHelperUpdateActualTraining = ItemTouchHelper(SwipeToUpdateActualTrainingCallback(adapter as MyTrainingRecyclerViewAdapter, context))
                itemTouchHelperUpdateActualTraining.attachToRecyclerView(view)
            }
        }
        return view
    }

    private fun loadTrainingList() {
        var trainings = trainingDbInstance?.getTraining()
        if (trainings != null) {
            for (t in trainings){
                trainingList.add(t!!)
                Log.i("Trainings", t.name)
            }
        }
    }

    override fun onClick(v: View?) {
        // TODO: Nothing
    }

    override fun onBackPressed() {
        getActivityContext().onBackPressed()
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                HistoricalTrainingFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}
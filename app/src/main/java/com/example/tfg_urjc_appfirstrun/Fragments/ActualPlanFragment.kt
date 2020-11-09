package com.example.tfg_urjc_appfirstrun.Fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tfg_urjc_appfirstrun.Adapters.ExpandableListAdapter
import com.example.tfg_urjc_appfirstrun.Database.Labs.SessionLab
import com.example.tfg_urjc_appfirstrun.Database.Labs.TrainingLab
import com.example.tfg_urjc_appfirstrun.Database.Labs.WeekLab
import com.example.tfg_urjc_appfirstrun.Entities.Session
import com.example.tfg_urjc_appfirstrun.Entities.Training
import com.example.tfg_urjc_appfirstrun.Entities.Week
import com.example.tfg_urjc_appfirstrun.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class ActualPlanFragment : Fragment() {

    // Extendable List View
    var listAdapter: ExpandableListAdapter? = null
    var expListView: ExpandableListView? = null
    var listDataWeeks = ArrayList<Week>()
    var listDataSessionByWeek = ArrayList<Session>()
    var listDataSession = HashMap<String, ArrayList<Session>>()
    // Database Instances
    var trainingDbInstance: TrainingLab? = null
    var weekDbInstance: WeekLab? = null
    var sessionDbInstance: SessionLab? = null
    // Variables
    var actualTraining: Training? = null
    var actualIdTraining: String? = null
    // Sincronizacion condicional
    var isFinishedCoroutine: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater?.inflate(R.layout.fragment_actual_plan, container, false)

        // Instancia de TrainingDB para inicialr la bd
        trainingDbInstance = TrainingLab.get(context)
        weekDbInstance = WeekLab.get(context)
        sessionDbInstance = SessionLab.get(context)

        // get the listview
        expListView = v.findViewById(R.id.lvExp)

        // preparing list data
        getActualTrainingFromDB(v)
        loadTrainingPlan()

        while (!isFinishedCoroutine){
            //Bloqueiiiito
        }
        listAdapter = ExpandableListAdapter(context!!, listDataWeeks!!, listDataSession!!)

        // setting list adapter
        expListView!!.setAdapter(listAdapter)

        expListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

            false
        }

        return v
    }

    private fun getActualTrainingFromDB(v: View) {
        actualTraining = trainingDbInstance?.getActualTraining()
        if (actualTraining != null){
            actualIdTraining = actualTraining?.trainingId
            Log.i("ActualTraining", actualIdTraining)
        } else {
            Snackbar.make(v, "¡No hay ningun entrenamiento actualmente!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    private fun loadTrainingPlan() {
        lifecycleScope.launch {
            var weeks = weekDbInstance?.getWeeksByTrainingId(actualIdTraining!!)
            if (weeks != null) {
                for (w in weeks){
                    listDataWeeks.add(w!!)
                    //Log.i("Week", w!!.numberWeek.toString())
                    var sessions = sessionDbInstance?.getSessionByWeekId(w.weekId!!)
                    if (sessions != null) {
                        for (s in sessions){
                            listDataSessionByWeek.add(s!!)
                            //Log.i("Week", w!!.weekId)
                        }
                    }
                    listDataSession.put(w!!.numberWeek.toString(),listDataSessionByWeek)
                    listDataSessionByWeek.clear()
                }
            }
            isFinishedCoroutine = true
        }
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        open fun onFragmentInteraction(uri: Uri?)
    }
}

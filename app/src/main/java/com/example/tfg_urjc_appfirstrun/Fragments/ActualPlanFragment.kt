package com.example.tfg_urjc_appfirstrun.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tfg_urjc_appfirstrun.Database.Labs.SectorLab
import com.example.tfg_urjc_appfirstrun.Database.Labs.SessionLab
import com.example.tfg_urjc_appfirstrun.Database.Labs.TrainingLab
import com.example.tfg_urjc_appfirstrun.Database.Labs.WeekLab
import com.example.tfg_urjc_appfirstrun.Entities.Training
import com.example.tfg_urjc_appfirstrun.R
import kotlinx.coroutines.launch
import java.util.*

class ActualPlanFragment : Fragment() {

    var trainingDbInstance: TrainingLab? = null
    var weekDbInstance: WeekLab? = null
    var sessionDbInstance: SessionLab? = null
    var sectorDbInstance: SectorLab? = null

    private val idActivity: String? = "2857978398"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instancia de TrainingDB para inicialr la bd
        trainingDbInstance = TrainingLab.get(context)
        weekDbInstance = WeekLab.get(context)
        sessionDbInstance = SessionLab.get(context)
        sectorDbInstance = SectorLab.get(context)

        //doPetition();
        loadTrainingPlan()
    }

    private fun loadTrainingPlan() {
        lifecycleScope.launch {
            var trainings = trainingDbInstance?.getTraining()
            if (trainings != null) {
                for (t in trainings){
                    Log.i("Trainings", t?.name)
                }
            }
        }
    }
    private fun doPetition() {
        // REQUEST TO OBTAIN ACCESS TOKEN
        val queue = Volley.newRequestQueue(this.context)
        // URL which return the access token
        val url = "https://www.strava.com/api/v3/activities/" + idActivity + "?include_all_efforts=true"
        Log.i("URL Activity Test", url)

        // Request a JSON response from the provided URL.
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null,
                Response.Listener { response -> Log.i("Request Auth", "Response is: $response") },
                Response.ErrorListener { error -> Log.i("Request Auth", "That didn't work!: $error") }
        ) {
            //This is for Headers If You Needed
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String?, String?>? {
                val headers: MutableMap<String?, String?> = HashMap()
                val preferences = activity?.getSharedPreferences("credentials", Context.MODE_PRIVATE)
                val access_token = preferences?.getString("access_token", null)
                headers["Content-Type"] = "application/json; charset=utf-8"
                headers["Authorization"] = "Bearer $access_token"
                Log.i("Header Request", headers.toString())
                return headers
            }
        }


        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_actual_plan, container, false)
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        open fun onFragmentInteraction(uri: Uri?)
    }
}
package com.example.tfg_urjc_appfirstrun.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tfg_urjc_appfirstrun.Adapters.MySectorRecyclerViewAdapter
import com.example.tfg_urjc_appfirstrun.Database.Labs.SectorLab
import com.example.tfg_urjc_appfirstrun.Entities.Activity
import com.example.tfg_urjc_appfirstrun.Entities.Sector
import com.example.tfg_urjc_appfirstrun.Entities.Session
import com.example.tfg_urjc_appfirstrun.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

/**
 * A fragment representing a list of Items.
 */
class DataSessionFragment(selectedSession: Session?, actualWeekNumber: Int?, actualSessionNumber: Int?): Fragment() {

    private var _session = selectedSession
    private var _actualWeekNumber = actualWeekNumber
    private var _actualSessionNumber = actualSessionNumber

    var sectorDbInstance: SectorLab? = null

    // Format Date (millis to Date)
    val formatDateCalender = SimpleDateFormat("dd/MM/yyyy")
    val formatDateTimer = SimpleDateFormat("mm:ss")

    // Data variables
    var listActivities = ArrayList<Activity>()
    var listDataSectors = ArrayList<Sector?>()

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(HistoricalTrainingFragment.ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_data_session_list, container, false)


        // Instancia de TrainingDB para inicialr la bd
        sectorDbInstance = SectorLab.get(context)

        // Get list of sectors
        fillSectorList()

        // All info session variables
        val sessionDay = view.findViewById(R.id.tv_sessionDay) as TextView
        sessionDay.text = formatDateCalender.format(_session!!.sessionDay)
        val actualWeekNumber = view.findViewById(R.id.tv_numberWeek) as TextView
        actualWeekNumber.text = _actualWeekNumber.toString()
        val actualSessionNumber = view.findViewById(R.id.tv_sessionNumber) as TextView
        actualSessionNumber.text = _actualSessionNumber.toString()
        // All training info variables
        val replays = view.findViewById(R.id.tv_replays) as TextView
        replays.text = _session!!.replays.toString()
        val recoveryTime = view.findViewById(R.id.tv_recoveryTime) as TextView
        recoveryTime.text = _session!!.recoveryTime
        val distance = view.findViewById(R.id.tv_distance) as TextView
        distance.text = _session!!.distance

        // Set the adapter list Sectors
        val recyclerView = view.findViewById(R.id.rv_resultsList) as RecyclerView
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = MySectorRecyclerViewAdapter(listDataSectors)
        }

        // Get activities from Strava
        //getActivitiesStrava();

        // Load spinner
        var spinner_activities = view.findViewById<View?>(R.id.spinner_activities) as Spinner
        val adapterSpinner = ArrayAdapter(activity, android.R.layout.simple_spinner_item, listActivities)
        spinner_activities?.setAdapter(adapterSpinner)

        val fab: FloatingActionButton = view.findViewById(R.id.floatingActionButton_addData)
        fab.setOnClickListener { view ->
            // addInfoToDatabase()
            val act: Activity = spinner_activities.getSelectedItem() as Activity
            Log.i("Selected Activity", act.toString())
        }

        return view
    }


    private fun fillSectorList() {
        var sectors = sectorDbInstance?.getSectorBySessionId(_session?.sessionId!!)
        if (sectors != null) {
            for (sect in sectors){
                listDataSectors.add(sect!!)
                Log.i("Sector", sect!!.numberSector.toString())
            }
        }
    }

    private fun getActivitiesStrava() {
        // REQUEST TO OBTAIN ACCESS TOKEN
        val queue = Volley.newRequestQueue(this.context)
        // URL which return the access token
        val url = "https://www.strava.com/api/v3/athlete/activities"
        Log.i("URL Activity Test", url)
        // Request a JSON response from the provided URL.
        val jsonArrayRequest: JsonArrayRequest = object : JsonArrayRequest(Method.GET, url, null,
                Response.Listener { response ->
                    //Log.i("Request Auth", "Response is: $response")
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        //Log.i("Activity", item.get("id").toString())
                        var activity = Activity(item.get("id").toString(), item.get("name").toString(), item.get("start_date").toString())
                        listActivities.add(activity)
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Request Error", "That didn't work!: $error")
                }
        ) {
            //This is for Headers If You Needed
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String?, String?>? {
                val headers: MutableMap<String?, String?> = HashMap()
                val preferences = activity?.getSharedPreferences("credentials", Context.MODE_PRIVATE)
                val access_token = preferences?.getString("access_token", null)
                headers["ContentType"] = "application/json; charset=utf8"
                headers["Authorization"] = "Bearer $access_token"
                Log.i("Header Request", headers.toString())
                return headers
            }
        }


        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest)
    }

    private fun addInfoToDatabase() {
        // REQUEST TO OBTAIN ACCESS TOKEN
        val queue = Volley.newRequestQueue(this.context)
        // URL which return the access token
        val url = "https://www.strava.com/api/v3/athlete/activities"
        Log.i("URL Activity Test", url)
        // Request a JSON response from the provided URL.
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null,
                Response.Listener { response ->
                    Log.i("Request Actitivites", "Response is: $response")
                },
                Response.ErrorListener { error ->
                    Log.e("Request Error", "That didn't work!: $error")
                }
        ) {
            //This is for Headers If You Needed
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String?, String?>? {
                val headers: MutableMap<String?, String?> = HashMap()
                val preferences = activity?.getSharedPreferences("credentials", Context.MODE_PRIVATE)
                val access_token = preferences?.getString("access_token", null)
                headers["ContentType"] = "application/json; charset=utf8"
                headers["Authorization"] = "Bearer $access_token"
                Log.i("Header Request", headers.toString())
                return headers
            }
        }

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    companion object {
        const val ARG_COLUMN_COUNT: String = "column-count"
    }
}
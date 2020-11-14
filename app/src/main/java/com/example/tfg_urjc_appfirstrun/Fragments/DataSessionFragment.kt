package com.example.tfg_urjc_appfirstrun.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.tfg_urjc_appfirstrun.Adapters.MySectorRecyclerViewAdapter
import com.example.tfg_urjc_appfirstrun.Database.Labs.SectorLab
import com.example.tfg_urjc_appfirstrun.Entities.Activity
import com.example.tfg_urjc_appfirstrun.Entities.Lap
import com.example.tfg_urjc_appfirstrun.Entities.Sector
import com.example.tfg_urjc_appfirstrun.Entities.Session
import com.example.tfg_urjc_appfirstrun.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

/**
 * A fragment representing a list of Items.
 */
class DataSessionFragment(selectedSession: Session?, actualWeekNumber: Int?, actualSessionNumber: Int?, listActivities: ArrayList<Activity>): Fragment() {

    private var _session = selectedSession
    private var _actualWeekNumber = actualWeekNumber
    private var _actualSessionNumber = actualSessionNumber
    private var _listActivities = listActivities

    var sectorDbInstance: SectorLab? = null

    // Format Date (millis to Date)
    val formatDateCalender = SimpleDateFormat("dd/MM/yyyy")
    val formatDateTimer = SimpleDateFormat("mm:ss")

    // Data variables
    var listDataSectors = ArrayList<Sector?>()
    var selectedActivityId: String = ""
    var listLaps: ArrayList<Lap> = arrayListOf()

    // For spinner
    private lateinit var mySpinner: Spinner
    private lateinit var adapter: ArrayAdapter<Activity>
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

        // Load spinner
        mySpinner = view.findViewById<View?>(R.id.spinner_activities) as Spinner
        adapter = ArrayAdapter<Activity>(context, android.R.layout.simple_spinner_item, _listActivities)
        mySpinner?.adapter = adapter
        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                // You can define you actions as you want
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                var activity = mySpinner.selectedItem as Activity
                selectedActivityId = activity.id
                Log.i("Selected Activity", selectedActivityId)
            }
        }
        val fab: FloatingActionButton = view.findViewById(R.id.floatingActionButton_addData)
        fab.setOnClickListener { view ->
            Log.i("Floating Button", selectedActivityId)
            infoLapsRequest(selectedActivityId)
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

    private fun infoLapsRequest(idActivity: String){
        // REQUEST TO OBTAIN ACCESS TOKEN
        val queue = Volley.newRequestQueue(this.context)
        // URL which return the access token
        val url = "https://www.strava.com/api/v3/activities/$idActivity/laps"
        Log.i("URL Activity Test", url)
        // Request a JSON response from the provided URL.
        val jsonArrayRequest: JsonArrayRequest = object : JsonArrayRequest(Method.GET, url, null,
                Response.Listener { response ->
                    //Log.i("Request Actitivites", "Response is: $response")
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        //Log.i("Activity", item.get("id").toString())
                        var lap = Lap(item?.get("id").toString(), item?.get("lap_index") as Int, item?.get("elapsed_time") as Int, item?.get("moving_time") as Int, item?.get("distance") as Double)
                        Log.i("Lap", lap.toString())
                        listLaps.add(lap)
                    }
                    Log.i("List Laps", listLaps.toString())
                    addDataLapsToDatabase(_session!!.numberSession)
                    //reloadFragment()
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

    private fun reloadFragment() {
        val currentFragment = activity!!.supportFragmentManager.findFragmentById(R.id.content_main)
        val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.detach(currentFragment!!)
        fragmentTransaction.attach(currentFragment!!)
        fragmentTransaction.commit()
    }

    private fun addDataLapsToDatabase(numberSession: Int) {
        when (numberSession) {
                1 -> updateDataSeries()
                2 -> updateDataShortRun()
                3 -> updateDataLongRun()
            else -> {
            }
        }
    }

    private fun updateDataSeries() {
        lifecycleScope.launch(){
            var posLap: Int = 1;
            for (sector: Sector? in listDataSectors) {
                var updateSector: Sector = sector!!
                var goalTime = updateSector!!.goalTime
                var registerTime = listLaps[posLap]!!.elapsed_time.toLong() // Lo transformamos a Long
                updateSector.registerTime = registerTime.toFloat() * 1000 // Hay que pasarlo a milliseconds
                updateSector.difference = (registerTime - goalTime).toFloat()
                Log.i("Update Sector", updateSector.toString())
                sectorDbInstance?.updateSector(updateSector)
                posLap += 2
            }
        }
    }

    private fun updateDataShortRun() {
        lifecycleScope.launch(){
            var posLap: Int = 1;
            for (sector: Sector? in listDataSectors) {
                var updateSector: Sector = sector!!
                var goalTime = updateSector!!.goalTime
                var registerTime = (listLaps[posLap]!!.moving_time / (listLaps[posLap]!!.distance / 1000)).toLong() // Lo transformamos a Long
                updateSector.registerTime = registerTime.toFloat() * 1000 // Hay que pasarlo a milliseconds
                updateSector.difference = (registerTime - goalTime).toFloat()
                Log.i("Update Sector", updateSector.toString())
                sectorDbInstance?.updateSector(updateSector)
                posLap++
            }
        }
    }

    private fun updateDataLongRun() {
        lifecycleScope.launch(){
            var acuRegisterTime: Float = 0f
            for (lap: Lap? in listLaps) {
                acuRegisterTime += (lap!!.moving_time / (lap!!.distance / 1000)).toFloat()
            }
            var updateSector: Sector = listDataSectors[0]!!
            var goalTime = updateSector!!.goalTime
            var registerTime = acuRegisterTime.toLong() // Lo transformamos a Long
            updateSector.registerTime = registerTime.toFloat() * 1000 // Hay que pasarlo a milliseconds
            updateSector.difference = (registerTime - goalTime).toFloat()
            Log.i("Update Sector", updateSector.toString())
            sectorDbInstance?.updateSector(updateSector)
        }
    }

    companion object {
        const val ARG_COLUMN_COUNT: String = "column-count"
    }
}

package com.example.tfg_urjc_appfirstrun.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tfg_urjc_appfirstrun.Entities.Activity
import com.example.tfg_urjc_appfirstrun.Entities.Session
import com.example.tfg_urjc_appfirstrun.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat

/**
 * A simple [Fragment] subclass.
 * Use the [InfoSessionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoSessionFragment(selectedSession: Session?, actualWeekNumber: Int?, actualSessionNumber: Int?) : Fragment() {

    private var _session = selectedSession
    private var _actualWeekNumber = actualWeekNumber
    private var _actualSessionNumber = actualSessionNumber

    // Format Date (millis to Date)
    val formatDateCalender = SimpleDateFormat("dd/MM/yyyy")
    val formatDateTimer = SimpleDateFormat("mm:ss")

    // Data variables
    var listActivities = ArrayList<Activity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var v: View = inflater?.inflate(R.layout.fragment_info_session, container, false)

        // All info session variables
        val sessionDay = v.findViewById(R.id.tv_sessionDay) as TextView
        sessionDay.text = formatDateCalender.format(_session!!.sessionDay)
        val actualWeekNumber = v.findViewById(R.id.tv_numberWeek) as TextView
        actualWeekNumber.text = _actualWeekNumber.toString()
        val actualSessionNumber = v.findViewById(R.id.tv_sessionNumber) as TextView
        actualSessionNumber.text = _actualSessionNumber.toString()
        // All training info variables
        val replays = v.findViewById(R.id.tv_replays) as TextView
        replays.text = _session!!.replays.toString()
        val recoveryTime = v.findViewById(R.id.tv_recoveryTime) as TextView
        recoveryTime.text = _session!!.recoveryTime
        val distance = v.findViewById(R.id.tv_distance) as TextView
        distance.text = _session!!.distance

        // Get activities from Strava
        getActivitiesStrava();
        // Load spinner
        var spinner_activities = v.findViewById<View?>(R.id.spinner_activities) as Spinner
        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, listActivities)
        spinner_activities?.setAdapter(adapter)

        val fab: FloatingActionButton = v.findViewById(R.id.floatingActionButton_addData)
        fab.setOnClickListener { view ->
            // addInfoToDatabase()
            val act: Activity = spinner_activities.getSelectedItem() as Activity
            Log.i("Selected Activity", act.toString())
        }
        return v
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


}
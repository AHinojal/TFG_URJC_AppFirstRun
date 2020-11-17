package com.example.tfg_urjc_appfirstrun.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.tfg_urjc_appfirstrun.Adapters.ExpandableListAdapter
import com.example.tfg_urjc_appfirstrun.Entities.Activity
import com.example.tfg_urjc_appfirstrun.Entities.Session
import com.example.tfg_urjc_appfirstrun.Entities.Training
import com.example.tfg_urjc_appfirstrun.Entities.Week
import com.example.tfg_urjc_appfirstrun.R
import java.util.*
import kotlin.collections.ArrayList


class ActualPlanFragment(training: Training, listDataWeeks: ArrayList<Week>, listDataSession: HashMap<String, ArrayList<Session>>) : BaseFragment() {

    var _training: Training = training
    // Load lists
    var listDataWeeks: ArrayList<Week> = listDataWeeks
    var listDataSession: HashMap<String, ArrayList<Session>> = listDataSession
    // Extendable List View
    var listAdapter: ExpandableListAdapter? = null
    var expListView: ExpandableListView? = null
    // List with Strava's activities
    var listActivities = ArrayList<Activity>()

    override fun onClick(v: View?) {
    }

    override fun onBackPressed() {
        getActivityContext().onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_actual_plan, container, false)

        showBackButton(true)

        // textView
        val nameView: TextView = v.findViewById(R.id.tv_nameTraining)
        nameView.text = _training.name
        val typeView: TextView = v.findViewById(R.id.tv_typeTraining)
        typeView.text = _training.typeTraining
        val personalMark: TextView = v.findViewById(R.id.tv_personalMark)
        personalMark.text = _training.mark5Km

        // get the listview
        expListView = v.findViewById(R.id.lvExp)

        listAdapter = ExpandableListAdapter(context!!, listDataWeeks, listDataSession)

        getActivitiesStrava()

        // setting list adapter
        expListView!!.setAdapter(listAdapter)

        expListView!!.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            // Introducir ir a seccion concreta
            var selectedSession : Session? = obtainSession(groupPosition+1,childPosition)
            Log.i("Session Seleccionada", selectedSession?.sessionId.toString())

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.content_main, DataSessionFragment(selectedSession, groupPosition+1, childPosition+1, listActivities), "infoDataFragment")
            transaction?.addToBackStack(null)
            transaction?.commit()
            false
        }

        return v
    }

    private fun obtainSession(numberWeek: Int, numberSessionPos: Int) : Session {
        val listSessions : ArrayList<Session>? = listDataSession.get(numberWeek.toString())
        return listSessions!![numberSessionPos]
    }
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri?)
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
                        Log.i("Activity", activity.toString())
                        listActivities?.add(activity)
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
}

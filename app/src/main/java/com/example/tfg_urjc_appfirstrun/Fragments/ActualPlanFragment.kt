package com.example.tfg_urjc_appfirstrun.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tfg_urjc_appfirstrun.Adapters.ExpandableListAdapter
import com.example.tfg_urjc_appfirstrun.Database.Labs.SessionLab
import com.example.tfg_urjc_appfirstrun.Database.Labs.TrainingLab
import com.example.tfg_urjc_appfirstrun.Database.Labs.WeekLab
import com.example.tfg_urjc_appfirstrun.Entities.Training
import com.example.tfg_urjc_appfirstrun.R
import kotlinx.coroutines.launch
import java.util.*


class ActualPlanFragment : Fragment() {

    // Extendable List View
    var listAdapter: ExpandableListAdapter? = null
    var expListView: ExpandableListView? = null
    var listDataHeader = ArrayList<String>()
    var listDataChild = HashMap<String, List<String>>()
    // Database Instances
    var trainingDbInstance: TrainingLab? = null
    var weekDbInstance: WeekLab? = null
    var sessionDbInstance: SessionLab? = null

    private val idActivity: String? = "2857978398"

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
        prepareListData()

        listAdapter = ExpandableListAdapter(context!!, listDataHeader!!, listDataChild!!)

        // setting list adapter
        expListView!!.setAdapter(listAdapter)

        expListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

            false
        }


        //doPetition();
        loadTrainingPlan()

        return v
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

    private fun prepareListData() {
        // Adding child data
        listDataHeader.add("Top 250")
        listDataHeader.add("Now Showing")
        listDataHeader.add("Coming Soon..")

        // Adding child data
        val top250: MutableList<String> = ArrayList()
        top250.add("The Shawshank Redemption")
        top250.add("The Godfather")
        top250.add("The Godfather: Part II")
        top250.add("Pulp Fiction")
        top250.add("The Good, the Bad and the Ugly")
        top250.add("The Dark Knight")
        top250.add("12 Angry Men")
        val nowShowing: MutableList<String> = ArrayList()
        nowShowing.add("The Conjuring")
        nowShowing.add("Despicable Me 2")
        nowShowing.add("Turbo")
        nowShowing.add("Grown Ups 2")
        nowShowing.add("Red 2")
        nowShowing.add("The Wolverine")
        val comingSoon: MutableList<String> = ArrayList()
        comingSoon.add("2 Guns")
        comingSoon.add("The Smurfs 2")
        comingSoon.add("The Spectacular Now")
        comingSoon.add("The Canyons")
        comingSoon.add("Europa Report")

        listDataChild!![listDataHeader!!.get(0)] = top250 // Header, Child data
        listDataChild!![listDataHeader!!.get(1)] = nowShowing
        listDataChild!![listDataHeader!!.get(2)] = comingSoon
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        open fun onFragmentInteraction(uri: Uri?)
    }
}

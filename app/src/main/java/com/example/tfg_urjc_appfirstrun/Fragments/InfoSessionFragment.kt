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
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tfg_urjc_appfirstrun.Adapters.MySectorRecyclerViewAdapter
import com.example.tfg_urjc_appfirstrun.Entities.Sector
import com.example.tfg_urjc_appfirstrun.R
import java.util.HashMap

/**
 * A fragment representing a list of Items.
 */
class InfoSessionFragment : Fragment() {

    private var columnCount = 1
    private val idActivity: String? = "2857978398"
    var sectorsList = ArrayList<Sector>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_info_session_list, container, false)

        doPetition()

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MySectorRecyclerViewAdapter(sectorsList)
            }
        }
        return view
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

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                InfoSessionFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}
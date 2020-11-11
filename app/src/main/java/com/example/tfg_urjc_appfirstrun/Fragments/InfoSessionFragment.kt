package com.example.tfg_urjc_appfirstrun.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.tfg_urjc_appfirstrun.Entities.Session
import com.example.tfg_urjc_appfirstrun.R
import java.text.SimpleDateFormat

/**
 * A simple [Fragment] subclass.
 * Use the [InfoSessionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoSessionFragment(selectedSession: Session?) : Fragment() {

    private var _session = selectedSession

    // Format Date (millis to Date)
    val formatDateCalender = SimpleDateFormat("dd/MM/yyyy")
    val formatDateTimer = SimpleDateFormat("mm:ss")

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

        return v
    }
}
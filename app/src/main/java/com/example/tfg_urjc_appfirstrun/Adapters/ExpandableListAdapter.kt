package com.example.tfg_urjc_appfirstrun.Adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.tfg_urjc_appfirstrun.Entities.Session
import com.example.tfg_urjc_appfirstrun.Entities.Week
import com.example.tfg_urjc_appfirstrun.R
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class ExpandableListAdapter(context: Context, listDataHeader: ArrayList<Week>, listChildData: HashMap<String, ArrayList<Session>>) : BaseExpandableListAdapter() {
    private var _context: Context? = context
    // header titles
    private var _listDataHeader : ArrayList<Week> = listDataHeader
    // child data in format of header title, child title
    private var _listDataChild: HashMap<String, ArrayList<Session>>? = listChildData



    override fun getChild(groupPosition: Int, childPosititon: Int): Any? {
        return _listDataChild!![_listDataHeader!![groupPosition].numberWeek.toString()]
                ?.get(childPosititon)
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int,
                              isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {
        var convertView: View? = convertView
        val childText = getChild(groupPosition, childPosition) as Session?
        if (convertView == null) {
            val infalInflater = _context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.session_list_cplan, null)
        }
        val txtListChild = convertView?.findViewById(R.id.lblListItem) as TextView
        val formatDate = SimpleDateFormat("dd/MM/yyyy")
        when (childText?.numberSession) {
            1 -> txtListChild.text = formatDate.format(childText?.sessionDay) + " - SERIES"
            2 -> txtListChild.text = formatDate.format(childText?.sessionDay) + " - CARRERA CORTA"
            3 -> txtListChild.text = formatDate.format(childText?.sessionDay) + " - CARRERA LARGA"
            else -> {
            }
        }
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return _listDataChild!![_listDataHeader!![groupPosition].numberWeek.toString()]?.size!!;
    }

    override fun getGroup(groupPosition: Int): Any? {
        return _listDataHeader!![groupPosition]
    }

    override fun getGroupCount(): Int {
        return _listDataHeader!!.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean,
                              convertView: View?, parent: ViewGroup?): View? {
        var convertView: View? = convertView
        val headerTitle = getGroup(groupPosition) as Week?
        if (convertView == null) {
            val infalInflater = _context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.week_list_cplan, null)
        }
        val lblListHeader = convertView?.findViewById(R.id.lblListHeader) as TextView
        lblListHeader.setTypeface(null, Typeface.BOLD)
        lblListHeader.text = "Semana "+ headerTitle?.numberWeek
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
package com.example.tfg_urjc_appfirstrun.Fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.tfg_urjc_appfirstrun.Activities.MainActivity
import com.example.tfg_urjc_appfirstrun.Interfaces.OnBackPressedListener


abstract class BaseFragment : Fragment(), View.OnClickListener, OnBackPressedListener {

    private var _toolbar: Toolbar? = null
    private var _actionBar: ActionBar? = null
    private var _toggle: ActionBarDrawerToggle? = null
    private var _drawer: DrawerLayout? = null
    private var mToolBarNavigationListenerIsRegistered = false
    private var _activity : MainActivity? = null

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            _activity = context
            _toggle = _activity!!._toggle
            _toolbar = this._activity!!._toolbar
            _actionBar = _activity!!.supportActionBar
            _drawer = _activity!!._drawer
        }
    }

    protected fun showBackButton(show: Boolean) {
        if (show) {
            // Remove hamburger
            _toggle!!.isDrawerIndicatorEnabled = false
            // Show back button
            _actionBar!!.setDisplayHomeAsUpEnabled(true)
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            if (!mToolBarNavigationListenerIsRegistered) {
                _toggle!!.setToolbarNavigationClickListener { _ -> onBackPressed() }
                mToolBarNavigationListenerIsRegistered = true
            }
        } else {
            // Remove back button
            _actionBar?.setDisplayHomeAsUpEnabled(false)
            // Show hamburger
            _toggle?.isDrawerIndicatorEnabled = true
            // Remove the/any drawer toggle listener
            _toggle?.toolbarNavigationClickListener = null
            mToolBarNavigationListenerIsRegistered = false
        }
    }

    /**
     * Simplify setTitle in child fragments
     */
    protected fun setTitle(resId: Int) {
        _activity?.setTitle(getResources().getString(resId))
    }

    fun getActivityContext() : MainActivity {
        return _activity!!
    }
    //
    abstract override fun onClick(v: View?)

    // Handles BackPress events from MainActivity
    abstract override fun onBackPressed()
}
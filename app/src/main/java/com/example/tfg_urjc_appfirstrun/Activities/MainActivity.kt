package com.example.tfg_urjc_appfirstrun.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tfg_urjc_appfirstrun.Database.Labs.TrainingLab
import com.example.tfg_urjc_appfirstrun.Entities.Training
import com.example.tfg_urjc_appfirstrun.Fragments.*
import com.example.tfg_urjc_appfirstrun.R
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.json.JSONException

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, CreatePlanFragment.OnFragmentInteractionListener {

    private val clientId: String? = "40301"
    private val clientSecret: String? = "cf7feabaae97e78edbd6b35e2e3a3280dc7c7fbb"
    private var code: String? = ""
    private val grantType: String? = "authorization_code"
    private var navigationView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View?>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        preloadSharedPreferences()

        val drawer = findViewById<View?>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView = findViewById<View?>(R.id.nav_view) as NavigationView
        val menuNavigation = navigationView?.getMenu()
        // Para conseguir el valor de si estamos logueados en Strava ya
        checkIfIsStravaIsLogin(menuNavigation)
        navigationView?.setNavigationItemSelectedListener(this)
        if (intent != null && intent.data != null) {
            //it's the deeplink you want "http://localhost/exchane_token?......"
            val data = intent.data
            // to obtain the code for the auth
            code = data.getQueryParameter("code")
            Log.i("Code for Auth", code)
            requestAccessToken()
        }
    }

    private fun preloadSharedPreferences() {
        val preferences = getSharedPreferences("credentials", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("isStravaLogin", true) // Es FALSE. TRUE es para test
        editor.putString("access_token", null)
        editor.commit()
    }

    private fun checkIfIsStravaIsLogin(menuNavigation: Menu?) {
        val preferences = getSharedPreferences("credentials", MODE_PRIVATE)
        val isStravaLogin = preferences.getBoolean("isStravaLogin", false)
        menuNavigation?.findItem(R.id.redirect_strava)?.isVisible = !isStravaLogin
        menuNavigation?.findItem(R.id.create_plan)?.isVisible = isStravaLogin
        menuNavigation?.findItem(R.id.actual_plan)?.isVisible = isStravaLogin
        menuNavigation?.findItem(R.id.historical_plan)?.isVisible = isStravaLogin
    }

    private fun requestAccessToken() {
        // REQUEST TO OBTAIN ACCESS TOKEN
        val queue = Volley.newRequestQueue(this)
        // URL which return the access token
        val url = "https://www.strava.com/oauth/token?client_id=" + clientId + "&client_secret=" + clientSecret + "&code=" + code + "&grant_type=" + grantType
        Log.i("URL Auth: ", url)

        // Request a JSON response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, null,
                { response ->
                    Log.i("Request Auth", "Response is: $response")
                    try {
                        // PARA GUARDAR LOS DATOS ALMACENADOS
                        //Log.i("Access Token", response.getString("access_token"));
                        val preferences = getSharedPreferences("credentials", MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.putBoolean("isStravaLogin", true)
                        editor.putString("access_token", response.getString("access_token"))
                        editor.commit()
                        Log.i("Access Token in BDInt", preferences.getString("access_token", "No access token"))
                        val menuNavigation = navigationView?.getMenu()
                        checkIfIsStravaIsLogin(menuNavigation)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
        ) { error -> Log.i("Request Auth", "That didn't work!: $error") }

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    override fun onBackPressed() {
        val drawer = findViewById<View?>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item?.getItemId()
        if (id == R.id.action_settings) {
            Snackbar.make(findViewById(android.R.id.content), "App diseñada y creada por Álvaro Hinojal Blanco como TFG para la URJC", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = p0?.getItemId()
        var fragmentSelected = false
        var fragment: Fragment? = null
        if (id == R.id.create_plan) {
            fragment = CreatePlanFragment()
            fragmentSelected = true
        } else if (id == R.id.redirect_strava) {
            /*fragment = new InfoApiStravaFragment();
            fragmentSelected = true;*/
            // Nos redirecciona a Strava
            val intentUri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
                    .buildUpon()
                    .appendQueryParameter("client_id", clientId)
                    .appendQueryParameter("redirect_uri", "http://localhost/exchange_token")
                    .appendQueryParameter("response_type", "code")
                    .appendQueryParameter("approval_prompt", "auto")
                    .appendQueryParameter("scope", "activity:read_all,profile:read_all,read_all")
                    .build()
            val intent = Intent(Intent.ACTION_VIEW, intentUri)
            startActivity(intent)
        } else if (id == R.id.actual_plan) {
            fragment = ActualPlanFragment()
            fragmentSelected = true
        } else if (id == R.id.historical_plan) {
            fragment = HistoricalTrainingFragment();
            fragmentSelected = true
        } else if (id == R.id.info_first) {
            fragment = InfoFirstTrainingFragment()
            fragmentSelected = true
        }
        if (fragmentSelected) {
            fragment?.let { supportFragmentManager.beginTransaction().replace(R.id.content_main, it).commit() }
            p0.setChecked(true)
            supportActionBar?.setTitle(p0.getTitle())
        }
        val drawer = findViewById<View?>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onFragmentInteraction(uri: Uri?) {}

}
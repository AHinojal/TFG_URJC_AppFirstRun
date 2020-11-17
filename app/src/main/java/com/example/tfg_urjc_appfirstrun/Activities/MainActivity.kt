package com.example.tfg_urjc_appfirstrun.Activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
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
import com.example.tfg_urjc_appfirstrun.Database.Labs.SessionLab
import com.example.tfg_urjc_appfirstrun.Database.Labs.TrainingLab
import com.example.tfg_urjc_appfirstrun.Database.Labs.WeekLab
import com.example.tfg_urjc_appfirstrun.Entities.Session
import com.example.tfg_urjc_appfirstrun.Entities.Training
import com.example.tfg_urjc_appfirstrun.Entities.Week
import com.example.tfg_urjc_appfirstrun.Fragments.ActualPlanFragment
import com.example.tfg_urjc_appfirstrun.Fragments.CreatePlanFragment
import com.example.tfg_urjc_appfirstrun.Fragments.HistoricalTrainingFragment
import com.example.tfg_urjc_appfirstrun.R
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.json.JSONException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, CreatePlanFragment.OnFragmentInteractionListener {

    private val clientId: String? = "40301"
    private val clientSecret: String? = "cf7feabaae97e78edbd6b35e2e3a3280dc7c7fbb"
    private var code: String? = ""
    private val grantType: String? = "authorization_code"
    private var navigationView: NavigationView? = null

    var _drawer: DrawerLayout? = null
    var _toolbar: Toolbar? = null
    var _toggle : ActionBarDrawerToggle? = null

    // Database Instances
    var trainingDbInstance: TrainingLab? = null
    var weekDbInstance: WeekLab? = null
    var sessionDbInstance: SessionLab? = null
    // Variables
    var actualTraining: Training? = null
    var actualIdTraining: String? = null
    var listDataWeeks = ArrayList<Week>()
    val listDataSession: HashMap<String, ArrayList<Session>> = HashMap()
    var haveActualTraining: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        _toolbar = findViewById<View?>(R.id.toolbar) as Toolbar
        setSupportActionBar(_toolbar)

        /*val formatDateTimer = SimpleDateFormat("mm:ss")
        val seconds: Int = 109 * 1000
        var test = findViewById<View?>(R.id.test) as TextView
        test.text = formatDateTimer.format(seconds)*/

        if (intent.extras != null) {
            if (!getIntent().getExtras().getBoolean("isReload")){
                preloadSharedPreferences()
            } else {
                val preferences = getSharedPreferences("credentials", MODE_PRIVATE)
                val expiredAt_token = preferences.getInt("expires_at", 0)
                var dateExpired: Calendar? =  Calendar.getInstance()
                dateExpired?.timeInMillis = expiredAt_token * 1000.toLong()
                var dateActual: LocalDateTime = LocalDateTime.now()
                Log.i("Fecha actual", dateActual.toString())
                Log.i("Fecha Expiracion Token", convertToLocalDateTime(dateExpired!!).toString())
                if(dateActual.isAfter(convertToLocalDateTime(dateExpired!!))){
                    preloadSharedPreferences()
                }
            }
        }

        // Instancia de TrainingDB para iniciar la bd
        trainingDbInstance = TrainingLab.get(this)
        weekDbInstance = WeekLab.get(this)
        sessionDbInstance = SessionLab.get(this)

        // preparing list data
        getActualTrainingFromDB()
        if (actualIdTraining != null) {
            loadTrainingPlan()
        }

        _drawer = findViewById<View?>(R.id.drawer_layout) as DrawerLayout
        _toggle = ActionBarDrawerToggle(
                this, _drawer, _toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        _drawer?.addDrawerListener(_toggle!!)
        _toggle?.syncState()

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

    override fun onBackPressed() {
        var data = supportFragmentManager.backStackEntryCount
        if (data > 0) {
            supportFragmentManager.popBackStackImmediate()
            if (data < 2) { // Si el paso atras viene desde un fragment, reiniciamos activity
                val intent = Intent(this, MainActivity::class.java)
                val bundle = Bundle()
                bundle.putBoolean("isReload", true)
                intent.putExtras(bundle)
                startActivity(intent)
                this.finish()
            }
        } else {
            super.onBackPressed()
        }

    }

    private fun preloadSharedPreferences() {
        val preferences = getSharedPreferences("credentials", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("isStravaLogin", false) // Es FALSE. TRUE es para test en creacion pantalla tras login
        editor.putString("access_token", null)
        editor.putInt("expires_at", 0)
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
                        editor.putInt("expires_at", response.getInt("expires_at"))
                        editor.commit()
                        Log.i("Access Token in BDInt", preferences.getString("access_token", "No access token"))
                        val menuNavigation = navigationView?.getMenu()
                        checkIfIsStravaIsLogin(menuNavigation)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
        ) { error -> Log.e("Request Auth", "That didn't work!: $error") }

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    private fun getActualTrainingFromDB() {
        actualTraining = trainingDbInstance?.getActualTraining()
        if (actualTraining != null){
            actualIdTraining = actualTraining?.trainingId
            haveActualTraining = true
            Log.i("ActualTraining", actualIdTraining)
        }
    }

    private fun loadTrainingPlan() {
        lifecycleScope.launch {
            var weeks = weekDbInstance?.getWeeksByTrainingId(actualIdTraining!!)
            if (weeks != null) {
                for (w in weeks){
                    listDataWeeks.add(w!!)
                    Log.i("Week", w.numberWeek.toString())
                    var sessions = sessionDbInstance?.getSessionByWeekId(w.weekId)
                    if (sessions != null) {
                        var listDataSessionByWeek = ArrayList<Session>()
                        for (s in sessions){
                            listDataSessionByWeek.add(s!!)
                            Log.i("Session", s.sessionId)
                        }
                        listDataSession.set(w.numberWeek.toString(), listDataSessionByWeek)
                    }
                }
            }
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
        val id = p0.getItemId()
        var fragmentSelected = false
        var fragment: Fragment? = null
        if (id == R.id.create_plan) {
            fragment = CreatePlanFragment()
            fragmentSelected = true
        } else if (id == R.id.redirect_strava) {
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
            if (haveActualTraining){
                fragment = ActualPlanFragment(actualTraining!!, listDataWeeks, listDataSession)
                fragmentSelected = true
            }else{
                Snackbar.make(findViewById(android.R.id.content), "¡No hay ningun entrenamiento actualmente!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()

            }
        } else if (id == R.id.historical_plan) {
            fragment = HistoricalTrainingFragment();
            fragmentSelected = true
        }
        if (fragmentSelected) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content_main, fragment!!, "nextFragment")
                    .addToBackStack("")
                    .commit()
            p0.setChecked(true)
            supportActionBar?.setTitle(p0.getTitle())
        }
        val drawer = findViewById<View?>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onFragmentInteraction(uri: Uri?) {}

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertToLocalDateTime(calendar: Calendar): LocalDateTime{
        return LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId())
    }
}
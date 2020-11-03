package com.example.tfg_urjc_appfirstrun.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tfg_urjc_appfirstrun.Activities.LoginActivity.ui.login.LoginActivity;
import com.example.tfg_urjc_appfirstrun.Fragments.ActualPlanFragment;
import com.example.tfg_urjc_appfirstrun.Fragments.CreatePlanFragment;
import com.example.tfg_urjc_appfirstrun.Fragments.CreatePlanFragment.OnFragmentInteractionListener;
import com.example.tfg_urjc_appfirstrun.Fragments.InfoApiStravaFragment;
import com.example.tfg_urjc_appfirstrun.Fragments.InfoFirstFragment;
import com.example.tfg_urjc_appfirstrun.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener{

    private View view;
    private String clientId = "40301";
    private String clientSecret = "cf7feabaae97e78edbd6b35e2e3a3280dc7c7fbb";
    private String code = "";
    private String grantType = "authorization_code";
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menuNavigation = navigationView.getMenu();
        // Para conseguir el valor de si estamos logueados en Strava ya
        checkIfIsStravaIsLogin(menuNavigation);
        navigationView.setNavigationItemSelectedListener(this);

        if (getIntent() != null && getIntent().getData() != null)
        {
            //it's the deeplink you want "http://localhost/exchane_token?......"
            Uri data = getIntent().getData();
            // to obtain the code for the auth
            this.code = data.getQueryParameter("code");
            Log.i("Code for Auth", this.code);

            this.requestAccessToken();
        }
    }

    private void checkIfIsStravaIsLogin(Menu menuNavigation) {
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        boolean isStravaLogin = preferences.getBoolean("isStravaLogin", false);
        menuNavigation.findItem(R.id.redirect_strava).setVisible(!isStravaLogin);
        menuNavigation.findItem(R.id.create_plan).setVisible(isStravaLogin);
        menuNavigation.findItem(R.id.actual_plan).setVisible(isStravaLogin);
        menuNavigation.findItem(R.id.historical_plan).setVisible(isStravaLogin);
    }

    private void requestAccessToken() {
        // REQUEST TO OBTAIN ACCESS TOKEN
        RequestQueue queue = Volley.newRequestQueue(this);
        // URL which return the access token
        String url = "https://www.strava.com/oauth/token?client_id=" + this.clientId + "&client_secret=" + this.clientSecret + "&code=" + this.code + "&grant_type=" + this.grantType;
        Log.i("URL Auth: ", url);

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Request Auth", "Response is: " + response);
                        try {
                            // PARA GUARDAR LOS DATOS ALMACENADOS
                            //Log.i("Access Token", response.getString("access_token"));
                            SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = preferences.edit();

                            editor.putBoolean("isStravaLogin", true);
                            editor.putString("access_token", response.getString("access_token"));

                            editor.commit();

                            Log.i("Access Token in BDInt", preferences.getString("access_token", "No access token"));

                            Menu menuNavigation = navigationView.getMenu();
                            checkIfIsStravaIsLogin(menuNavigation);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Request Auth", "That didn't work!: " + error);
                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Snackbar.make(view,"App diseñada y creada por Álvaro Hinojal Blanco como TFG para la URJC",Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean fragmentSelected = false;
        Fragment fragment = null;

        if (id == R.id.create_plan) {
            fragment = new CreatePlanFragment();
            fragmentSelected = true;
        } else if (id == R.id.redirect_strava) {
            /*fragment = new InfoApiStravaFragment();
            fragmentSelected = true;*/
            // Nos redirecciona a Strava
            Uri intentUri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
                    .buildUpon()
                    .appendQueryParameter("client_id", this.clientId)
                    .appendQueryParameter("redirect_uri", "http://localhost/exchange_token")
                    .appendQueryParameter("response_type", "code")
                    .appendQueryParameter("approval_prompt", "auto")
                    .appendQueryParameter("scope", "activity:read_all,profile:read_all,read_all")
                    .build();
            Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
            startActivity(intent);
        } else if (id == R.id.actual_plan) {
            fragment = new ActualPlanFragment();
            fragmentSelected = true;
        } else if (id == R.id.historical_plan) {
            // fragment = new HistoricalPlanFragment();
            // TODO: Crear RecyclerView para mostrar la lista de entrenamientos realizados
            fragmentSelected = false;
        } else if (id == R.id.info_first){
            fragment = new InfoFirstFragment();
            fragmentSelected = true;
        } else if (id == R.id.log_out) {
            logout();
        }

        if(fragmentSelected){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Log.d("Logout: ", "User logout correctly");
        Toast.makeText(getApplicationContext(), getString(R.string.session_closed), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}

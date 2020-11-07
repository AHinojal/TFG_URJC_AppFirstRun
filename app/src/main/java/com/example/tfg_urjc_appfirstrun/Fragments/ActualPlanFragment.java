package com.example.tfg_urjc_appfirstrun.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tfg_urjc_appfirstrun.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActualPlanFragment extends Fragment {

    // To recovery info from Firebase
    // private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    // DatabaseReference ref = database.getReference();

    private OnFragmentInteractionListener mListener;
    private String idActivity = "2857978398";

    public ActualPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //doPetition();
        loadTrainingPlan();
    }

    private void loadTrainingPlan() {

    }

    private void doPetition() {
        // REQUEST TO OBTAIN ACCESS TOKEN
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        // URL which return the access token
        String url = "https://www.strava.com/api/v3/activities/" + this.idActivity + "?include_all_efforts=true";
        Log.i("URL Activity Test", url);

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Request Auth", "Response is: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Request Auth", "That didn't work!: " + error);
                    }
                }
        ){
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences preferences = getActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE);
                String access_token = preferences.getString("access_token", null);
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Bearer " + access_token);
                Log.i("Header Request", headers.toString());
                return headers;
            }
        };


        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_actual_plan, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

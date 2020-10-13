package com.example.tfg_urjc_appfirstrun.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.tfg_urjc_appfirstrun.R;

public class InfoApiStravaFragment extends Fragment {
    /* Para pruebas:
    private String clientId = "40301";
    private String clientSecret = "cf7feabaae97e78edbd6b35e2e3a3280dc7c7fbb";
    */
    private Button buttonGoToStrava;
    private EditText clientId;
    private EditText secretClient;

    public InfoApiStravaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_api_strava, container, false);
        clientId = (EditText) v.findViewById(R.id.editText_clientId);
        secretClient = (EditText) v.findViewById(R.id.editText_secretClient);
        buttonGoToStrava = (Button) v.findViewById (R.id.button_go_to_strava);
        buttonGoToStrava.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Guardamos los valores en SharedPreferances
                SharedPreferences preferences = getActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("clientId", clientId.getText().toString());
                editor.putString("secretClient", secretClient.getText().toString());
                editor.commit();
                String client_id = preferences.getString("clientId", null);
                Log.i("ClientId", client_id);
                // Nos redirecciona a Strava
                Uri intentUri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
                        .buildUpon()
                        .appendQueryParameter("client_id", preferences.getString("clientId", null))
                        .appendQueryParameter("redirect_uri", "http://localhost/exchange_token")
                        .appendQueryParameter("response_type", "code")
                        .appendQueryParameter("approval_prompt", "auto")
                        .appendQueryParameter("scope", "activity:read_all,profile:read_all,read_all")
                        .build();
                Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}
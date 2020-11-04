package com.example.tfg_urjc_appfirstrun.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.tfg_urjc_appfirstrun.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CreatePlanFragment extends Fragment {

    private Spinner spinner_durationPlan;
    private Spinner spinner_distancePlan;
    private EditText actualTime;
    private HashMap<String, String> dataPlan = new HashMap<String, String>();

    private OnFragmentInteractionListener mListener;

    public CreatePlanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_plan, container, false);

        actualTime = (EditText) v.findViewById(R.id.editText_actualtime5km);

        // Inicializacion de los atributos
        spinner_durationPlan = (Spinner) v.findViewById (R.id.spinner_durationPlan);
        spinner_distancePlan = (Spinner) v.findViewById (R.id.spinner_distancePlan);

        // Adaptadores para los spinner que indica la duracion del plan de entrenamiento
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.duration_plan, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_durationPlan.setAdapter(adapter);

        spinner_durationPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Si selecciona 12 semanas
                if (spinner_durationPlan.getSelectedItem().equals("12 semanas")) {
                    // Adaptador para el spinner que indica las distancias para planes de 12 semanas
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.distance_12w_plan, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_distancePlan.setAdapter(adapter);
                } else if (spinner_durationPlan.getSelectedItem().equals("16 semanas")){ // Si selecciona 16 semanas
                    // Adaptador para el spinner que indica las distancias para planes de 12 semanas
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.distance_16w_plan, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_distancePlan.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FloatingActionButton fab = v.findViewById(R.id.floatingActionButton_savePlan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // CALCULO DEL FACTOR EN FUNCION DEL TIEMPO
                int factor = calculateFactor(actualTime.getText().toString());
                // tiempo 16:00 (00:01:07) + factor * tiempo 0:10 (00:00:00.800)
                Date initialTime = new Date(800);
                Date baseTime = new Date(67000);
                float time = baseTime.getTime() + (factor * initialTime.getTime());
                Date markSectorTime = new Date(Math.round(time));
                SimpleDateFormat formatDate = new SimpleDateFormat("mm:ss");
                String infoData = formatDate.format(markSectorTime);
                Snackbar.make(view, infoData, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return v;
    }

    // To get the time diference between
    private int calculateFactor(String actualTime) {
        int factor = 0;
        try {
            SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm:ss");
            Date dateStart = formatDate.parse("00:16:00");
            Date dateEnd = formatDate.parse(actualTime);
            float milliseconds = dateEnd.getTime() - dateStart.getTime();
            factor = (int) (milliseconds / 10000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Log.i("Factor", Integer.toString(factor));
        return factor;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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

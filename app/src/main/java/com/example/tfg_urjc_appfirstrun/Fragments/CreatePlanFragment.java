package com.example.tfg_urjc_appfirstrun.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tfg_urjc_appfirstrun.R;

public class CreatePlanFragment extends Fragment {

    private Spinner spinner_durationPlan;
    private Spinner spinner_distancePlan;

    private OnFragmentInteractionListener mListener;

    public CreatePlanFragment() {
        // Required empty public constructor
    }

    public static CreatePlanFragment newInstance() {
        CreatePlanFragment fragment = new CreatePlanFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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

        return v;
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

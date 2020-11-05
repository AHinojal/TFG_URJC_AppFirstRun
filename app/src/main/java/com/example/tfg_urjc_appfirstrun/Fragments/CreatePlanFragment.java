package com.example.tfg_urjc_appfirstrun.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.tfg_urjc_appfirstrun.Classes.Sector;
import com.example.tfg_urjc_appfirstrun.Classes.Session;
import com.example.tfg_urjc_appfirstrun.Classes.Training;
import com.example.tfg_urjc_appfirstrun.Classes.Week;
import com.example.tfg_urjc_appfirstrun.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class CreatePlanFragment extends Fragment {

    private Spinner spinner_durationPlan;
    private Spinner spinner_distancePlan;
    private EditText actualTime;
    private HashMap<String, Date> hashMapPlanning = new HashMap<String, Date>();
    private Training training;

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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                // Creamos la lista de tiempos a hacer segun la marca que nos da el usuario
                fillHashMapWithTrainingTimes();

                /* To show in log all the training times
                for (Map.Entry times : dataPlan.entrySet()) {
                    SimpleDateFormat formatDate = new SimpleDateFormat("mm:ss");
                    Log.i("Dato " + times.getKey(), formatDate.format(times.getValue()));
                }*/

                // Creamos toda la estructura de base de datos con el entrenamiento
                //createTraining(0);

                Snackbar.make(view, "Creado", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    // METHODS CREATION PLANNING TRAINING
    private void createTraining(int typeTraining){
        switch(typeTraining){
            case 0: // 5 km - 12 semanas
                this.creation5km();
                break;
            case 1: // 10 km - 12 semanas
                this.creation10km();
                break;
            case 2: // Marathon - 16 semanas
                this.creationMarathon();
                break;
            default:
                break;
        }
    }

    // METHODS with each one CREATION TRAINING
    private void creation5km (){
        // Creamos los sectores y lo guardamos en un array para pasarselo a las sesiones
        // SERIES (Xw_1s)
        ArrayList<Sector> sectors1w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 8; i++){
            int number = i + 1;
            sectors1w_1s.add(new Sector(number, hashMapPlanning.get("400").getTime()));
        }
        ArrayList<Sector> sectors2w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 8; i++){
            int number = i + 1;
            sectors2w_1s.add(new Sector(number, hashMapPlanning.get("800").getTime()));
        }
        ArrayList<Sector> sectors3w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 3; i++){
            int number = i + 1;
            if (i < 2){
                sectors3w_1s.add(new Sector(number, hashMapPlanning.get("1600").getTime()));
            } else {
                sectors3w_1s.add(new Sector(number, hashMapPlanning.get("800").getTime()));
            }
        }
        ArrayList<Sector> sectors4w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 6; i++){
            int number = i + 1;
            if (i == 0 || i == 5){
                sectors4w_1s.add(new Sector(number, hashMapPlanning.get("400").getTime()));
            } else if (i == 1 || i == 4){
                sectors4w_1s.add(new Sector(number, hashMapPlanning.get("600").getTime()));
            } else {
                sectors4w_1s.add(new Sector(number, hashMapPlanning.get("800").getTime()));
            }
        }
        ArrayList<Sector> sectors5w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 4; i++){
            int number = i + 1;
            sectors5w_1s.add(new Sector(number, hashMapPlanning.get("1000").getTime()));
        }
        ArrayList<Sector> sectors6w_1s = new ArrayList<Sector>();
        sectors6w_1s.add(new Sector(1, hashMapPlanning.get("1600").getTime()));
        sectors6w_1s.add(new Sector(2, hashMapPlanning.get("1200").getTime()));
        sectors6w_1s.add(new Sector(3, hashMapPlanning.get("800").getTime()));
        sectors6w_1s.add(new Sector(4, hashMapPlanning.get("400").getTime()));
        ArrayList<Sector> sectors7w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 10; i++){
            int number = i + 1;
            sectors7w_1s.add(new Sector(number, hashMapPlanning.get("400").getTime()));
        }
        ArrayList<Sector> sectors8w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 6; i++){
            int number = i + 1;
            sectors8w_1s.add(new Sector(number, hashMapPlanning.get("800").getTime()));
        }
        ArrayList<Sector> sectors9w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 4; i++){
            int number = i + 1;
            sectors9w_1s.add(new Sector(number, hashMapPlanning.get("1200").getTime()));
        }
        ArrayList<Sector> sectors10w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 5; i++){
            int number = i + 1;
            sectors10w_1s.add(new Sector(number, hashMapPlanning.get("1000").getTime()));
        }
        ArrayList<Sector> sectors11w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 3; i++){
            int number = i + 1;
            sectors11w_1s.add(new Sector(number, hashMapPlanning.get("1600").getTime()));
        }
        ArrayList<Sector> sectors12w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 6; i++){
            int number = i + 1;
            sectors12w_1s.add(new Sector(number, hashMapPlanning.get("400").getTime()));
        }
        // CARRERA CORTA (Xw_2s)
        ArrayList<Sector> sectors1w_2s = new ArrayList<Sector>();
        sectors1w_2s.add(new Sector(1, hashMapPlanning.get("corto").getTime()));
        ArrayList<Sector> sectors2w_2s = new ArrayList<Sector>();
        sectors2w_2s.add(new Sector(1, hashMapPlanning.get("corto").getTime()));
        ArrayList<Sector> sectors3w_2s = new ArrayList<Sector>();
        sectors3w_2s.add(new Sector(1, hashMapPlanning.get("corto").getTime()));
        sectors3w_2s.add(new Sector(2, hashMapPlanning.get("facil").getTime()));
        sectors3w_2s.add(new Sector(3, hashMapPlanning.get("corto").getTime()));
        ArrayList<Sector> sectors4w_2s = new ArrayList<Sector>();
        sectors4w_2s.add(new Sector(1, hashMapPlanning.get("medio").getTime()));
        ArrayList<Sector> sectors5w_2s = new ArrayList<Sector>();
        sectors5w_2s.add(new Sector(1, hashMapPlanning.get("corto").getTime()));
        ArrayList<Sector> sectors6w_2s = new ArrayList<Sector>();
        sectors6w_2s.add(new Sector(1, hashMapPlanning.get("corto").getTime()));
        sectors6w_2s.add(new Sector(2, hashMapPlanning.get("facil").getTime()));
        sectors6w_2s.add(new Sector(3, hashMapPlanning.get("corto").getTime()));
        sectors6w_2s.add(new Sector(4, hashMapPlanning.get("facil").getTime()));
        sectors6w_2s.add(new Sector(5, hashMapPlanning.get("corto").getTime()));
        ArrayList<Sector> sectors7w_2s = new ArrayList<Sector>();
        sectors7w_2s.add(new Sector(1, hashMapPlanning.get("medio").getTime()));
        ArrayList<Sector> sectors8w_2s = new ArrayList<Sector>();
        sectors8w_2s.add(new Sector(1, hashMapPlanning.get("corto").getTime()));
        sectors8w_2s.add(new Sector(2, hashMapPlanning.get("facil").getTime()));
        sectors8w_2s.add(new Sector(3, hashMapPlanning.get("corto").getTime()));
        ArrayList<Sector> sectors9w_2s = new ArrayList<Sector>();
        sectors9w_2s.add(new Sector(1, hashMapPlanning.get("corto").getTime()));
        ArrayList<Sector> sectors10w_2s = new ArrayList<Sector>();
        sectors10w_2s.add(new Sector(1, hashMapPlanning.get("corto").getTime()));
        sectors10w_2s.add(new Sector(2, hashMapPlanning.get("facil").getTime()));
        sectors10w_2s.add(new Sector(3, hashMapPlanning.get("corto").getTime()));
        sectors10w_2s.add(new Sector(4, hashMapPlanning.get("facil").getTime()));
        sectors10w_2s.add(new Sector(5, hashMapPlanning.get("corto").getTime()));
        ArrayList<Sector> sectors11w_2s = new ArrayList<Sector>();
        sectors11w_2s.add(new Sector(1, hashMapPlanning.get("corto").getTime()));
        ArrayList<Sector> sectors12w_2s = new ArrayList<Sector>();
        sectors12w_2s.add(new Sector(1, hashMapPlanning.get("facil").getTime()));
        // CARRERA LARGA (Xw_3s)
        ArrayList<Sector> sectors1w_3s = new ArrayList<Sector>();
        sectors1w_3s.add(new Sector(1, hashMapPlanning.get("largo").getTime()));
        ArrayList<Sector> sectors2w_3s = new ArrayList<Sector>();
        sectors2w_3s.add(new Sector(1, hashMapPlanning.get("largo").getTime()));
        ArrayList<Sector> sectors3w_3s = new ArrayList<Sector>();
        sectors3w_3s.add(new Sector(1, hashMapPlanning.get("largo").getTime()));
        ArrayList<Sector> sectors4w_3s = new ArrayList<Sector>();
        sectors4w_3s.add(new Sector(1, hashMapPlanning.get("largo").getTime()));
        ArrayList<Sector> sectors5w_3s = new ArrayList<Sector>();
        sectors5w_3s.add(new Sector(1, hashMapPlanning.get("largo").getTime()));
        ArrayList<Sector> sectors6w_3s = new ArrayList<Sector>();
        sectors6w_3s.add(new Sector(1, hashMapPlanning.get("largo").getTime()));
        ArrayList<Sector> sectors7w_3s = new ArrayList<Sector>();
        sectors7w_3s.add(new Sector(1, hashMapPlanning.get("largo").getTime()));
        ArrayList<Sector> sectors8w_3s = new ArrayList<Sector>();
        sectors8w_3s.add(new Sector(1, hashMapPlanning.get("largo").getTime()));
        ArrayList<Sector> sectors9w_3s = new ArrayList<Sector>();
        sectors9w_3s.add(new Sector(1, hashMapPlanning.get("largo").getTime()));
        ArrayList<Sector> sectors10w_3s = new ArrayList<Sector>();
        sectors10w_3s.add(new Sector(1, hashMapPlanning.get("largo").getTime()));
        ArrayList<Sector> sectors11w_3s = new ArrayList<Sector>();
        sectors11w_3s.add(new Sector(1, hashMapPlanning.get("largo").getTime()));
        ArrayList<Sector> sectors12w_3s = new ArrayList<Sector>();
        sectors12w_3s.add(new Sector(1, hashMapPlanning.get("largo").getTime()));
        // Creamos las sesiones (pasandoles sus sectores)
        // week1
        Session session1w_1s = new Session (sectors1w_1s.size(),new ArrayList<String>(Arrays.asList("400")),new Date(),"400m",sectors1w_1s);
        Session session2w_1s = new Session (sectors2w_1s.size(),new ArrayList<String>(Arrays.asList("800")),new Date(),"400m",sectors2w_1s);
        Session session3w_1s = new Session (sectors3w_1s.size(),new ArrayList<String>(Arrays.asList("1600","1600","800")),new Date(),"400m",sectors3w_1s);
        Session session4w_1s = new Session (sectors4w_1s.size(),new ArrayList<String>(Arrays.asList("400","600","800","800","600","400")),new Date(),"400m",sectors4w_1s);
        Session session5w_1s = new Session (sectors5w_1s.size(),new ArrayList<String>(Arrays.asList("1000")),new Date(),"400m",sectors5w_1s);
        Session session6w_1s = new Session (sectors6w_1s.size(),new ArrayList<String>(Arrays.asList("1600","1200","800","400")),new Date(),"400m",sectors6w_1s);
        Session session7w_1s = new Session (sectors7w_1s.size(),new ArrayList<String>(Arrays.asList("400")),new Date(),"90seg",sectors7w_1s);
        Session session8w_1s = new Session (sectors8w_1s.size(),new ArrayList<String>(Arrays.asList("800")),new Date(),"400m",sectors8w_1s);
        Session session9w_1s = new Session (sectors9w_1s.size(),new ArrayList<String>(Arrays.asList("1200")),new Date(),"400m",sectors9w_1s);
        Session session10w_1s = new Session (sectors10w_1s.size(),new ArrayList<String>(Arrays.asList("1000")),new Date(),"400m",sectors10w_1s);
        Session session11w_1s = new Session (sectors11w_1s.size(),new ArrayList<String>(Arrays.asList("1600")),new Date(),"400m",sectors11w_1s);
        Session session12w_1s = new Session (sectors12w_1s.size(),new ArrayList<String>(Arrays.asList("400")),new Date(),"400m",sectors12w_1s);
        // week2
        Session session1w_2s = new Session (sectors1w_2s.size(),new ArrayList<String>(Arrays.asList("3K")),new Date(),null,sectors1w_2s);
        Session session2w_2s = new Session (sectors2w_2s.size(),new ArrayList<String>(Arrays.asList("5K")),new Date(),null,sectors2w_2s);
        Session session3w_2s = new Session (sectors3w_2s.size(),new ArrayList<String>(Arrays.asList("3K","1.5K","3K")),new Date(),null,sectors3w_2s);
        Session session4w_2s = new Session (sectors4w_2s.size(),new ArrayList<String>(Arrays.asList("6.5K")),new Date(),null,sectors4w_2s);
        Session session5w_2s = new Session (sectors5w_2s.size(),new ArrayList<String>(Arrays.asList("5K")),new Date(),null,sectors5w_2s);
        Session session6w_2s = new Session (sectors6w_2s.size(),new ArrayList<String>(Arrays.asList("1.5K","1.5K","1.5K","1.5K","1.5K")),new Date(),null,sectors6w_2s);
        Session session7w_2s = new Session (sectors7w_2s.size(),new ArrayList<String>(Arrays.asList("6.5K")),new Date(),null,sectors7w_2s);
        Session session8w_2s = new Session (sectors8w_2s.size(),new ArrayList<String>(Arrays.asList("3K","1.5K","3K")),new Date(),null,sectors8w_2s);
        Session session9w_2s = new Session (sectors9w_2s.size(),new ArrayList<String>(Arrays.asList("5K")),new Date(),null,sectors9w_2s);
        Session session10w_2s = new Session (sectors10w_2s.size(),new ArrayList<String>(Arrays.asList("3K","1.5K","1.5K","1.5K","3K")),new Date(),null,sectors10w_2s);
        Session session11w_2s = new Session (sectors11w_2s.size(),new ArrayList<String>(Arrays.asList("5K")),new Date(),null,sectors11w_2s);
        Session session12w_2s = new Session (sectors12w_2s.size(),new ArrayList<String>(Arrays.asList("5K")),new Date(),null,sectors12w_2s);
        //week3
        Session session1w_3s = new Session (sectors1w_3s.size(),new ArrayList<String>(Arrays.asList("8K")),new Date(),null,sectors1w_3s);
        Session session2w_3s = new Session (sectors2w_3s.size(),new ArrayList<String>(Arrays.asList("10K")),new Date(),null,sectors2w_3s);
        Session session3w_3s = new Session (sectors3w_3s.size(),new ArrayList<String>(Arrays.asList("8K")),new Date(),null,sectors3w_3s);
        Session session4w_3s = new Session (sectors4w_3s.size(),new ArrayList<String>(Arrays.asList("10K")),new Date(),null,sectors4w_3s);
        Session session5w_3s = new Session (sectors5w_3s.size(),new ArrayList<String>(Arrays.asList("11K")),new Date(),null,sectors5w_3s);
        Session session6w_3s = new Session (sectors6w_3s.size(),new ArrayList<String>(Arrays.asList("10K")),new Date(),null,sectors6w_3s);
        Session session7w_3s = new Session (sectors7w_3s.size(),new ArrayList<String>(Arrays.asList("13K")),new Date(),null,sectors7w_3s);
        Session session8w_3s = new Session (sectors8w_3s.size(),new ArrayList<String>(Arrays.asList("11K")),new Date(),null,sectors8w_3s);
        Session session9w_3s = new Session (sectors9w_3s.size(),new ArrayList<String>(Arrays.asList("10K")),new Date(),null,sectors9w_3s);
        Session session10w_3s = new Session (sectors10w_3s.size(),new ArrayList<String>(Arrays.asList("10K")),new Date(),null,sectors10w_3s);
        Session session11w_3s = new Session (sectors11w_3s.size(),new ArrayList<String>(Arrays.asList("10K")),new Date(),null,sectors11w_3s);
        Session session12w_3s = new Session (sectors12w_3s.size(),new ArrayList<String>(Arrays.asList("5K")),new Date(),null,sectors12w_3s);
        // Creamos las semanas
        ArrayList<Week> weeks = new ArrayList<Week>(12);
        Week week1 = new Week(session1w_1s, session1w_2s, session1w_3s);
        Week week2 = new Week(session2w_1s, session2w_2s, session2w_3s);
        Week week3 = new Week(session3w_1s, session3w_2s, session3w_3s);
        Week week4 = new Week(session4w_1s, session4w_2s, session4w_3s);
        Week week5 = new Week(session5w_1s, session5w_2s, session5w_3s);
        Week week6 = new Week(session6w_1s, session6w_2s, session6w_3s);
        Week week7 = new Week(session7w_1s, session7w_2s, session7w_3s);
        Week week8 = new Week(session8w_1s, session8w_2s, session8w_3s);
        Week week9 = new Week(session9w_1s, session9w_2s, session9w_3s);
        Week week10 = new Week(session10w_1s, session10w_2s, session10w_3s);
        Week week11 = new Week(session11w_1s, session11w_2s, session11w_3s);
        Week week12 = new Week(session12w_1s, session12w_2s, session12w_3s);
        weeks.add(week1);
        weeks.add(week2);
        weeks.add(week3);
        weeks.add(week4);
        weeks.add(week5);
        weeks.add(week6);
        weeks.add(week7);
        weeks.add(week8);
        weeks.add(week9);
        weeks.add(week10);
        weeks.add(week11);
        weeks.add(week12);
        // Creamos el entrenamiento
        SharedPreferences preferences = getContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
        training = new Training(preferences.getString("uuidUserLogged", null),"tipoEntrenamiento",actualTime.getText().toString(),weeks);
        Log.i("CREATION TRAINING", "Done...");
    }

    private void creation10km() {
        // Creamos los sectores


        // Creamos las sesiones (pasandoles sus sectores)
        // Creamos las semanas
        // Creamos el entrenamiento
    }

    private void creationMarathon() {
        // Creamos los sectores


        // Creamos las sesiones (pasandoles sus sectores)
        // Creamos las semanas
        // Creamos el entrenamiento
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void fillHashMapWithTrainingTimes() {
        // CALCULO DEL FACTOR EN FUNCION DEL TIEMPO
        int factor = calculateFactor(actualTime.getText().toString());
        hashMapPlanning.put("400", calculateTimeSector(new Date(67000),new Date(800),factor));
        hashMapPlanning.put("600", calculateTimeSector(new Date(103000),new Date(1200),factor));
        hashMapPlanning.put("800", calculateTimeSector(new Date(138000),new Date(1600),factor));
        hashMapPlanning.put("1000", calculateTimeSector(new Date(175000),new Date(2000),factor));
        hashMapPlanning.put("1200", calculateTimeSector(new Date(214000),new Date(2400),factor));
        hashMapPlanning.put("1600", calculateTimeSector(new Date(293000),new Date(3200),factor));
        hashMapPlanning.put("2000", calculateTimeSector(new Date(371000),new Date(4000),factor));
        hashMapPlanning.put("corto", calculateTimeSector(new Date(202000),new Date(2000),factor));
        hashMapPlanning.put("medio", calculateTimeSector(new Date(212000),new Date(2000),factor));
        hashMapPlanning.put("largo", calculateTimeSector(new Date(221000),new Date(2000),factor));
        hashMapPlanning.put("facil", plusTime(calculateTimeSector(new Date(221000),new Date(2000),factor), new Date (41000)));
        hashMapPlanning.put("mar", calculateTimeSector(new Date(221000),new Date(2300),factor));
        hashMapPlanning.put("mediamar", calculateTimeSector(new Date(211000),new Date(2200),factor));
    }

    private Date calculateTimeSector(Date initialTime, Date baseTime, int factor){
        // tiempo inicial 16:00 segun distancia + factor * tiempo base 0:10 segun distancia
        float time = initialTime.getTime() + (factor * baseTime.getTime());
        return new Date(Math.round(time));
    }

    private Date plusTime(Date initialTime, Date extraTime) {
        float milliseconds = initialTime.getTime() + extraTime.getTime();
        return new Date(Math.round(milliseconds));
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
        Log.i("Factor", Integer.toString(factor));
        return factor;
    }
}

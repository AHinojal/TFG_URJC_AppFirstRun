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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.tfg_urjc_appfirstrun.Entities.Sector;
import com.example.tfg_urjc_appfirstrun.Entities.Session;
import com.example.tfg_urjc_appfirstrun.Entities.Training;
import com.example.tfg_urjc_appfirstrun.Entities.Week;
import com.example.tfg_urjc_appfirstrun.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CreatePlanFragment extends Fragment {
    private static final int DAY_IN_MILLISECONDS = 86400000;

    private Spinner spinner_durationPlan;
    private Spinner spinner_distancePlan;
    private EditText trainingName;
    private EditText actualTime;
    private DatePicker picker_startingDate;

    private HashMap<String, Date> hashMapPlanning = new HashMap<String, Date>();
    private Training training;
    private Date startingDate;

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

        // Inicializacion de los atributos basicos
        trainingName = (EditText) v.findViewById(R.id.editText_namePlan);
        picker_startingDate = (DatePicker) v.findViewById(R.id.datePicker_startingDateTraining);
        actualTime = (EditText) v.findViewById(R.id.editText_actualtime5km);

        // Inicializacion de los atributos que van en el spinner
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
                // Creamos variable para el dia de inicio del programa
                startingDate = new Date(picker_startingDate.getYear()-1900,picker_startingDate.getMonth(),picker_startingDate.getDayOfMonth());
                // Creamos la lista de tiempos a hacer segun la marca que nos da el usuario
                fillHashMapWithTrainingTimes();

                /* To show in log all the training times
                for (Map.Entry times : dataPlan.entrySet()) {
                    SimpleDateFormat formatDate = new SimpleDateFormat("mm:ss");
                    Log.i("Dato " + times.getKey(), formatDate.format(times.getValue()));
                }*/

                // Creamos toda la estructura de base de datos con el entrenamiento
                createTraining(0);

                Snackbar.make(view, "Creado", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // Volver a la anterior vista
                // getActivity().onBackPressed();
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
        // Creamos el entrenamiento
        training = new Training(trainingName.getText().toString(), startingDate, "tipoEntrenamiento",actualTime.getText().toString());
        // Creamos las semanas, agregandoles el id del entrenamiento y el numero de semana que es
        Week week1 = new Week(training.getTrainingId(),1);
        Week week2 = new Week(training.getTrainingId(),2);
        Week week3 = new Week(training.getTrainingId(),3);
        Week week4 = new Week(training.getTrainingId(),4);
        Week week5 = new Week(training.getTrainingId(),5);
        Week week6 = new Week(training.getTrainingId(),6);
        Week week7 = new Week(training.getTrainingId(),7);
        Week week8 = new Week(training.getTrainingId(),8);
        Week week9 = new Week(training.getTrainingId(),9);
        Week week10 = new Week(training.getTrainingId(),10);
        Week week11 = new Week(training.getTrainingId(),11);
        Week week12 = new Week(training.getTrainingId(),12);
        // Creamos las sesiones
        // week1
        Session session1w_1s = new Session (week1.getWeekId(),1,8,new ArrayList<String>(Arrays.asList("400")),new Date (startingDate.getTime() + (DAY_IN_MILLISECONDS * 1)),"400m");
        Session session2w_1s = new Session (week2.getWeekId(),1,5,new ArrayList<String>(Arrays.asList("800")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 8)),"400m");
        Session session3w_1s = new Session (week3.getWeekId(),1,3,new ArrayList<String>(Arrays.asList("1600","1600","800")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 15)),"400m");
        Session session4w_1s = new Session (week4.getWeekId(),1,6,new ArrayList<String>(Arrays.asList("400","600","800","800","600","400")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 22)),"400m");
        Session session5w_1s = new Session (week5.getWeekId(),1, 4,new ArrayList<String>(Arrays.asList("1000")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 29)),"400m");
        Session session6w_1s = new Session (week6.getWeekId(),1,4,new ArrayList<String>(Arrays.asList("1600","1200","800","400")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 36)),"400m");
        Session session7w_1s = new Session (week7.getWeekId(),1,10,new ArrayList<String>(Arrays.asList("400")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 43)),"90seg");
        Session session8w_1s = new Session (week8.getWeekId(),1,6,new ArrayList<String>(Arrays.asList("800")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 50)),"400m");
        Session session9w_1s = new Session (week9.getWeekId(),1,4,new ArrayList<String>(Arrays.asList("1200")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 57)),"400m");
        Session session10w_1s = new Session (week10.getWeekId(),1,5,new ArrayList<String>(Arrays.asList("1000")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 64)),"400m");
        Session session11w_1s = new Session (week11.getWeekId(),1,3,new ArrayList<String>(Arrays.asList("1600")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 71)),"400m");
        Session session12w_1s = new Session (week12.getWeekId(),1,6,new ArrayList<String>(Arrays.asList("400")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 78)),"400m");
        // week2
        Session session1w_2s = new Session (week1.getWeekId(),2,1,new ArrayList<String>(Arrays.asList("3K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 4)),null);
        Session session2w_2s = new Session (week2.getWeekId(),2,1,new ArrayList<String>(Arrays.asList("5K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 11)),null);
        Session session3w_2s = new Session (week3.getWeekId(),2,3,new ArrayList<String>(Arrays.asList("3K","1.5K","3K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 18)),null);
        Session session4w_2s = new Session (week4.getWeekId(),2,1,new ArrayList<String>(Arrays.asList("6.5K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 25)),null);
        Session session5w_2s = new Session (week5.getWeekId(),2,1,new ArrayList<String>(Arrays.asList("5K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 1)),null);
        Session session6w_2s = new Session (week6.getWeekId(),2,5,new ArrayList<String>(Arrays.asList("1.5K","1.5K","1.5K","1.5K","1.5K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 32)),null);
        Session session7w_2s = new Session (week7.getWeekId(),2,1,new ArrayList<String>(Arrays.asList("6.5K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 39)),null);
        Session session8w_2s = new Session (week8.getWeekId(),2,8,new ArrayList<String>(Arrays.asList("3K","1.5K","3K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 46)),null);
        Session session9w_2s = new Session (week9.getWeekId(),2,1,new ArrayList<String>(Arrays.asList("5K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 53)),null);
        Session session10w_2s = new Session (week10.getWeekId(),2,5,new ArrayList<String>(Arrays.asList("3K","1.5K","1.5K","1.5K","3K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 60)),null);
        Session session11w_2s = new Session (week11.getWeekId(),2,1,new ArrayList<String>(Arrays.asList("5K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 67)),null);
        Session session12w_2s = new Session (week12.getWeekId(),2,1,new ArrayList<String>(Arrays.asList("5K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 74)),null);
        //week3
        Session session1w_3s = new Session (week1.getWeekId(),3, 1,new ArrayList<String>(Arrays.asList("8K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 6)),null);
        Session session2w_3s = new Session (week2.getWeekId(),3,1,new ArrayList<String>(Arrays.asList("10K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 13)),null);
        Session session3w_3s = new Session (week3.getWeekId(),3,1,new ArrayList<String>(Arrays.asList("8K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 20)),null);
        Session session4w_3s = new Session (week4.getWeekId(),3,1,new ArrayList<String>(Arrays.asList("10K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 27)),null);
        Session session5w_3s = new Session (week5.getWeekId(),3,1,new ArrayList<String>(Arrays.asList("11K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 34)),null);
        Session session6w_3s = new Session (week6.getWeekId(),3,1,new ArrayList<String>(Arrays.asList("10K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 41)),null);
        Session session7w_3s = new Session (week7.getWeekId(),3,1,new ArrayList<String>(Arrays.asList("13K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 48)),null);
        Session session8w_3s = new Session (week8.getWeekId(),3,1,new ArrayList<String>(Arrays.asList("11K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 55)),null);
        Session session9w_3s = new Session (week9.getWeekId(),3,1,new ArrayList<String>(Arrays.asList("10K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 62)),null);
        Session session10w_3s = new Session (week10.getWeekId(),3,1,new ArrayList<String>(Arrays.asList("10K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 69)),null);
        Session session11w_3s = new Session (week11.getWeekId(),3,1,new ArrayList<String>(Arrays.asList("10K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 76)),null);
        Session session12w_3s = new Session (week12.getWeekId(),3,1,new ArrayList<String>(Arrays.asList("5K")),new Date(startingDate.getTime() + (DAY_IN_MILLISECONDS * 83)),null);
        // Creamos los sectores y lo guardamos en un array para pasarselo a las sesiones
        // SERIES (Xw_1s)
        ArrayList<Sector> sectors1w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 8; i++){
            int number = i + 1;
            sectors1w_1s.add(new Sector(session1w_1s.getSessionId(),number, hashMapPlanning.get("400").getTime()));
        }
        ArrayList<Sector> sectors2w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 8; i++){
            int number = i + 1;
            sectors2w_1s.add(new Sector(session2w_1s.getSessionId(),number, hashMapPlanning.get("800").getTime()));
        }
        ArrayList<Sector> sectors3w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 3; i++){
            int number = i + 1;
            if (i < 2){
                sectors3w_1s.add(new Sector(session3w_1s.getSessionId(), number, hashMapPlanning.get("1600").getTime()));
            } else {
                sectors3w_1s.add(new Sector(session3w_1s.getSessionId(),number, hashMapPlanning.get("800").getTime()));
            }
        }
        ArrayList<Sector> sectors4w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 6; i++){
            int number = i + 1;
            if (i == 0 || i == 5){
                sectors4w_1s.add(new Sector(session4w_1s.getSessionId(),number, hashMapPlanning.get("400").getTime()));
            } else if (i == 1 || i == 4){
                sectors4w_1s.add(new Sector(session4w_1s.getSessionId(),number, hashMapPlanning.get("600").getTime()));
            } else {
                sectors4w_1s.add(new Sector(session4w_1s.getSessionId(),number, hashMapPlanning.get("800").getTime()));
            }
        }
        ArrayList<Sector> sectors5w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 4; i++){
            int number = i + 1;
            sectors5w_1s.add(new Sector(session5w_1s.getSessionId(),number, hashMapPlanning.get("1000").getTime()));
        }
        ArrayList<Sector> sectors6w_1s = new ArrayList<Sector>();
        sectors6w_1s.add(new Sector(session6w_1s.getSessionId(),1, hashMapPlanning.get("1600").getTime()));
        sectors6w_1s.add(new Sector(session6w_1s.getSessionId(),2, hashMapPlanning.get("1200").getTime()));
        sectors6w_1s.add(new Sector(session6w_1s.getSessionId(),3, hashMapPlanning.get("800").getTime()));
        sectors6w_1s.add(new Sector(session6w_1s.getSessionId(),4, hashMapPlanning.get("400").getTime()));
        ArrayList<Sector> sectors7w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 10; i++){
            int number = i + 1;
            sectors7w_1s.add(new Sector(session7w_1s.getSessionId(),number, hashMapPlanning.get("400").getTime()));
        }
        ArrayList<Sector> sectors8w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 6; i++){
            int number = i + 1;
            sectors8w_1s.add(new Sector(session8w_1s.getSessionId(),number, hashMapPlanning.get("800").getTime()));
        }
        ArrayList<Sector> sectors9w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 4; i++){
            int number = i + 1;
            sectors9w_1s.add(new Sector(session9w_1s.getSessionId(),number, hashMapPlanning.get("1200").getTime()));
        }
        ArrayList<Sector> sectors10w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 5; i++){
            int number = i + 1;
            sectors10w_1s.add(new Sector(session10w_1s.getSessionId(),number, hashMapPlanning.get("1000").getTime()));
        }
        ArrayList<Sector> sectors11w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 3; i++){
            int number = i + 1;
            sectors11w_1s.add(new Sector(session11w_1s.getSessionId(),number, hashMapPlanning.get("1600").getTime()));
        }
        ArrayList<Sector> sectors12w_1s = new ArrayList<Sector>();
        for (int i = 0; i < 6; i++){
            int number = i + 1;
            sectors12w_1s.add(new Sector(session12w_1s.getSessionId(),number, hashMapPlanning.get("400").getTime()));
        }
        // CARRERA CORTA (Xw_2s)
        Sector sectors1w_2s = new Sector(session1w_2s.getSessionId(),1, hashMapPlanning.get("corto").getTime());
        Sector sectors2w_2s = new Sector(session12w_2s.getSessionId(),1, hashMapPlanning.get("corto").getTime());
        ArrayList<Sector> sectors3w_2s = new ArrayList<Sector>();
        sectors3w_2s.add(new Sector(session3w_2s.getSessionId(),1, hashMapPlanning.get("corto").getTime()));
        sectors3w_2s.add(new Sector(session3w_2s.getSessionId(),2, hashMapPlanning.get("facil").getTime()));
        sectors3w_2s.add(new Sector(session3w_2s.getSessionId(),3, hashMapPlanning.get("corto").getTime()));
        Sector sectors4w_2s = new Sector(session4w_2s.getSessionId(),1, hashMapPlanning.get("medio").getTime());
        Sector sectors5w_2s = new Sector(session5w_2s.getSessionId(),1, hashMapPlanning.get("corto").getTime());
        ArrayList<Sector> sectors6w_2s = new ArrayList<Sector>();
        sectors6w_2s.add(new Sector(session6w_2s.getSessionId(),1, hashMapPlanning.get("corto").getTime()));
        sectors6w_2s.add(new Sector(session6w_2s.getSessionId(),2, hashMapPlanning.get("facil").getTime()));
        sectors6w_2s.add(new Sector(session6w_2s.getSessionId(),3, hashMapPlanning.get("corto").getTime()));
        sectors6w_2s.add(new Sector(session6w_2s.getSessionId(),4, hashMapPlanning.get("facil").getTime()));
        sectors6w_2s.add(new Sector(session6w_2s.getSessionId(),5, hashMapPlanning.get("corto").getTime()));
        Sector sectors7w_2s = new Sector(session7w_2s.getSessionId(),1, hashMapPlanning.get("medio").getTime());
        ArrayList<Sector> sectors8w_2s = new ArrayList<Sector>();
        sectors8w_2s.add(new Sector(session8w_2s.getSessionId(),1, hashMapPlanning.get("corto").getTime()));
        sectors8w_2s.add(new Sector(session8w_2s.getSessionId(),2, hashMapPlanning.get("facil").getTime()));
        sectors8w_2s.add(new Sector(session8w_2s.getSessionId(),3, hashMapPlanning.get("corto").getTime()));
        Sector sectors9w_2s = new Sector(session9w_2s.getSessionId(),1, hashMapPlanning.get("corto").getTime());
        ArrayList<Sector> sectors10w_2s = new ArrayList<Sector>();
        sectors10w_2s.add(new Sector(session10w_2s.getSessionId(),1, hashMapPlanning.get("corto").getTime()));
        sectors10w_2s.add(new Sector(session10w_2s.getSessionId(),2, hashMapPlanning.get("facil").getTime()));
        sectors10w_2s.add(new Sector(session10w_2s.getSessionId(),3, hashMapPlanning.get("corto").getTime()));
        sectors10w_2s.add(new Sector(session10w_2s.getSessionId(),4, hashMapPlanning.get("facil").getTime()));
        sectors10w_2s.add(new Sector(session10w_2s.getSessionId(),5, hashMapPlanning.get("corto").getTime()));
        Sector sectors11w_2s = new Sector(session11w_2s.getSessionId(),1, hashMapPlanning.get("corto").getTime());
        Sector sectors12w_2s = new Sector(session12w_2s.getSessionId(),1, hashMapPlanning.get("facil").getTime());
        // CARRERA LARGA (Xw_3s)
        Sector sectors1w_3s = new Sector(session1w_3s.getSessionId(),1, hashMapPlanning.get("largo").getTime());
        Sector sectors2w_3s = new Sector(session2w_3s.getSessionId(),1, hashMapPlanning.get("largo").getTime());
        Sector sectors3w_3s = new Sector(session3w_3s.getSessionId(),1, hashMapPlanning.get("largo").getTime());
        Sector sectors4w_3s = new Sector(session4w_3s.getSessionId(),1, hashMapPlanning.get("largo").getTime());
        Sector sectors5w_3s = new Sector(session5w_3s.getSessionId(),1, hashMapPlanning.get("largo").getTime());
        Sector sectors6w_3s = new Sector(session6w_3s.getSessionId(),1, hashMapPlanning.get("largo").getTime());
        Sector sectors7w_3s = new Sector(session7w_3s.getSessionId(),1, hashMapPlanning.get("largo").getTime());
        Sector sectors8w_3s = new Sector(session8w_3s.getSessionId(),1, hashMapPlanning.get("largo").getTime());
        Sector sectors9w_3s = new Sector(session9w_3s.getSessionId(),1, hashMapPlanning.get("largo").getTime());
        Sector sectors10w_3s = new Sector(session10w_3s.getSessionId(),1, hashMapPlanning.get("largo").getTime());
        Sector sectors11w_3s = new Sector(session11w_3s.getSessionId(),1, hashMapPlanning.get("largo").getTime());
        Sector sectors12w_3s = new Sector(session12w_3s.getSessionId(),1, hashMapPlanning.get("largo").getTime());
        Log.i("CREATION TRAINING", "Done...");
        // Guardar en Firebase

    }

    private void creation10km() {

    }

    private void creationMarathon() {

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

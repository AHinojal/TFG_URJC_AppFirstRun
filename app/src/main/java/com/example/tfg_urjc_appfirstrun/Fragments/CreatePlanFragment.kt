package com.example.tfg_urjc_appfirstrun.Fragments

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tfg_urjc_appfirstrun.Database.Labs.SectorLab
import com.example.tfg_urjc_appfirstrun.Database.Labs.SessionLab
import com.example.tfg_urjc_appfirstrun.Database.Labs.TrainingLab
import com.example.tfg_urjc_appfirstrun.Database.Labs.WeekLab
import com.example.tfg_urjc_appfirstrun.Entities.Sector
import com.example.tfg_urjc_appfirstrun.Entities.Session
import com.example.tfg_urjc_appfirstrun.Entities.Training
import com.example.tfg_urjc_appfirstrun.Entities.Week
import com.example.tfg_urjc_appfirstrun.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CreatePlanFragment : Fragment() {

    private var spinner_durationPlan: Spinner? = null
    private var spinner_distancePlan: Spinner? = null
    private var trainingName: EditText? = null
    private var actualTime: EditText? = null

    private var picker_startingDate: DatePicker? = null
    private val hashMapPlanning: HashMap<String?, Float?>? = HashMap()
    private val hashMapTraining: HashMap<String, Int?>? = HashMap()
    private var training: Training? = null

    var trainingDbInstance: TrainingLab? = null
    var weekDbInstance: WeekLab? = null
    var sessionDbInstance: SessionLab? = null
    var sectorDbInstance: SectorLab? = null

    private var startingDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater?.inflate(R.layout.fragment_create_plan, container, false)

        // Instancia de TrainingDB para inicialr la bd
        trainingDbInstance = TrainingLab.get(context)
        weekDbInstance = WeekLab.get(context)
        sessionDbInstance = SessionLab.get(context)
        sectorDbInstance = SectorLab.get(context)

        // Inicializacion de los atributos basicos
        trainingName = v.findViewById<View?>(R.id.editText_namePlan) as EditText
        picker_startingDate = v.findViewById<View?>(R.id.datePicker_startingDateTraining) as DatePicker
        actualTime = v.findViewById<View?>(R.id.editText_actualtime5km) as EditText

        // Inicializacion de los atributos que van en el spinner
        spinner_durationPlan = v.findViewById<View?>(R.id.spinner_durationPlan) as Spinner
        spinner_distancePlan = v.findViewById<View?>(R.id.spinner_distancePlan) as Spinner

        // Adaptadores para los spinner que indica la duracion del plan de entrenamiento
        val adapter = ArrayAdapter.createFromResource(this.context, R.array.duration_plan, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_durationPlan?.adapter = adapter
        spinner_durationPlan?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Si selecciona 12 semanas
                if (spinner_durationPlan?.getSelectedItem() == "12 semanas") {
                    // Adaptador para el spinner que indica las distancias para planes de 12 semanas
                    val adapter = ArrayAdapter.createFromResource(context, R.array.distance_12w_plan, android.R.layout.simple_spinner_item)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner_distancePlan?.setAdapter(adapter)
                } else if (spinner_durationPlan?.getSelectedItem() == "16 semanas") { // Si selecciona 16 semanas
                    // Adaptador para el spinner que indica las distancias para planes de 12 semanas
                    val adapter = ArrayAdapter.createFromResource(context, R.array.distance_16w_plan, android.R.layout.simple_spinner_item)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner_distancePlan?.setAdapter(adapter)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        val fab: FloatingActionButton = v.findViewById(R.id.floatingActionButton_savePlan)
        fab.setOnClickListener { view ->
            // Creamos variable para el dia de inicio del programa
            startingDate = Date(picker_startingDate!!.getYear() - 1900, picker_startingDate!!.getMonth(), picker_startingDate!!.getDayOfMonth())
            // Creamos la lista de tiempos a hacer segun la marca que nos da el usuario

            this.fillHashMapWithTrainingTimes()

            /* To show in log all the training times
                for (Map.Entry times : dataPlan.entrySet()) {
                    SimpleDateFormat formatDate = new SimpleDateFormat("mm:ss");
                    Log.i("Dato " + times.getKey(), formatDate.format(times.getValue()));
                }*/

            // Creamos toda la estructura de base de datos con el entrenamiento
            hashMapTrainings()
            createTraining(hashMapTraining!!.get(spinner_distancePlan?.getSelectedItem().toString())!!, v)

            // Indicar ID de entrenamiento actual
            val preferences = context?.getSharedPreferences("credentials", AppCompatActivity.MODE_PRIVATE)
            val editor = preferences?.edit()
            editor!!.putString("actualIdTraining", training?.trainingId)
            editor!!.commit()
            // Volver a la anterior vista
            // getActivity().onBackPressed();
        }
        return v
    }

    // METHODS CREATION PLANNING TRAINING
    private fun createTraining(typeTraining: Int, view: View) {
        when (typeTraining) {
                0 -> creation5km(view)
                1 -> creation10km(view)
                2 -> creationMarathon(view)
            else -> {
            }
        }
    }

    // METHODS with each one CREATION TRAINING
    private fun creation5km(view: View) {
        // Creamos el entrenamiento
        training = Training(trainingName!!.getText().toString(), startingDate, spinner_distancePlan?.getSelectedItem().toString(), actualTime!!.getText().toString())
        // Creamos las semanas, agregandoles el id del entrenamiento y el numero de semana que es
        val week1 = Week(training!!.trainingId, 1)
        val week2 = Week(training!!.trainingId, 2)
        val week3 = Week(training!!.trainingId, 3)
        val week4 = Week(training!!.trainingId, 4)
        val week5 = Week(training!!.trainingId, 5)
        val week6 = Week(training!!.trainingId, 6)
        val week7 = Week(training!!.trainingId, 7)
        val week8 = Week(training!!.trainingId, 8)
        val week9 = Week(training!!.trainingId, 9)
        val week10 = Week(training!!.trainingId, 10)
        val week11 = Week(training!!.trainingId, 11)
        val week12 = Week(training!!.trainingId, 12)
        val weekList = arrayListOf(week1, week2, week3, week4, week5, week6, week7, week8, week9, week10, week11, week12)

        // Creamos las sesiones
        // week1
        val session1w_1s = Session(week1.weekId, 1, 8, "400", addDaystoDate(startingDate!!,1), "400m")
        val session2w_1s = Session(week2.weekId, 1, 5, "800", addDaystoDate(startingDate!!,8), "400m")
        val session3w_1s = Session(week3.weekId, 1, 3, "1600, 1600, 800", addDaystoDate(startingDate!!,15), "400m")
        val session4w_1s = Session(week4.weekId, 1, 6, "400, 600, 800, 800, 600, 400", addDaystoDate(startingDate!!,22), "400m")
        val session5w_1s = Session(week5.weekId, 1, 4, "1000", addDaystoDate(startingDate!!,29), "400m")
        val session6w_1s = Session(week6.weekId, 1, 4, "1600, 1200, 800, 400", addDaystoDate(startingDate!!,36), "400m")
        val session7w_1s = Session(week7.weekId, 1, 10, "400", addDaystoDate(startingDate!!,43), "90seg")
        val session8w_1s = Session(week8.weekId, 1, 6, "800", addDaystoDate(startingDate!!,50), "400m")
        val session9w_1s = Session(week9.weekId, 1, 4, "1200", addDaystoDate(startingDate!!,57), "400m")
        val session10w_1s = Session(week10.weekId, 1, 5, "1000", addDaystoDate(startingDate!!,64), "400m")
        val session11w_1s = Session(week11.weekId, 1, 3, "1600", addDaystoDate(startingDate!!,71), "400m")
        val session12w_1s = Session(week12.weekId, 1, 6, "400", addDaystoDate(startingDate!!,78), "400m")
        val session1s_List = arrayListOf(session1w_1s, session2w_1s, session3w_1s, session4w_1s, session5w_1s, session6w_1s, session7w_1s, session8w_1s, session9w_1s, session10w_1s, session11w_1s, session12w_1s)
        // week2
        val session1w_2s = Session(week1.weekId, 2, 1, "3K", addDaystoDate(startingDate!!,4), null)
        val session2w_2s = Session(week2.weekId, 2, 1, "5K", addDaystoDate(startingDate!!,11), null)
        val session3w_2s = Session(week3.weekId, 2, 3, "3K, 1.5K, 3K", addDaystoDate(startingDate!!,18), null)
        val session4w_2s = Session(week4.weekId, 2, 1, "6.5K", addDaystoDate(startingDate!!,25), null)
        val session5w_2s = Session(week5.weekId, 2, 1, "5K", addDaystoDate(startingDate!!,32), null)
        val session6w_2s = Session(week6.weekId, 2, 5, "1.5K, 1.5k, 1.5K, 1.5K, 1.5K", addDaystoDate(startingDate!!,39), null)
        val session7w_2s = Session(week7.weekId, 2, 1, "6.5K", addDaystoDate(startingDate!!,46), null)
        val session8w_2s = Session(week8.weekId, 2, 3, "3K, 1.5K, 3K", addDaystoDate(startingDate!!,53), null)
        val session9w_2s = Session(week9.weekId, 2, 1, "5K", addDaystoDate(startingDate!!,60), null)
        val session10w_2s = Session(week10.weekId, 2, 5, "3K, 1.5K, 1.5K, 1.5K, 3K", addDaystoDate(startingDate!!,67), null)
        val session11w_2s = Session(week11.weekId, 2, 1, "5K", addDaystoDate(startingDate!!,74), null)
        val session12w_2s = Session(week12.weekId, 2, 1, "5K", addDaystoDate(startingDate!!,81), null)
        val session2s_List = arrayListOf(session1w_2s, session2w_2s, session3w_2s, session4w_2s, session5w_2s, session6w_2s, session7w_2s, session8w_2s, session9w_2s, session10w_2s, session11w_2s, session12w_2s)
        //week3
        val session1w_3s = Session(week1.weekId, 3, 1, "8K", addDaystoDate(startingDate!!,6), null)
        val session2w_3s = Session(week2.weekId, 3, 1, "10K", addDaystoDate(startingDate!!,13), null)
        val session3w_3s = Session(week3.weekId, 3, 1, "8K", addDaystoDate(startingDate!!,20), null)
        val session4w_3s = Session(week4.weekId, 3, 1, "10K", addDaystoDate(startingDate!!,27), null)
        val session5w_3s = Session(week5.weekId, 3, 1, "11K", addDaystoDate(startingDate!!,34), null)
        val session6w_3s = Session(week6.weekId, 3, 1, "10K", addDaystoDate(startingDate!!,41), null)
        val session7w_3s = Session(week7.weekId, 3, 1, "13K", addDaystoDate(startingDate!!,48), null)
        val session8w_3s = Session(week8.weekId, 3, 1, "11K", addDaystoDate(startingDate!!,55), null)
        val session9w_3s = Session(week9.weekId, 3, 1, "10K", addDaystoDate(startingDate!!,62), null)
        val session10w_3s = Session(week10.weekId, 3, 1, "10K", addDaystoDate(startingDate!!,69), null)
        val session11w_3s = Session(week11.weekId, 3, 1, "10K", addDaystoDate(startingDate!!,76), null)
        val session12w_3s = Session(week12.weekId, 3, 1, "5K", addDaystoDate(startingDate!!,83), null)
        val session3s_List = arrayListOf(session1w_3s, session2w_3s, session3w_3s, session4w_3s, session5w_3s, session6w_3s, session7w_3s,
                session8w_3s, session9w_3s, session10w_3s, session11w_3s, session12w_3s)
        // Creamos los sectores y lo guardamos en un array para pasarselo a las sesiones
        // SERIES (Xw_1s)
        val sectors1w_1s = arrayListOf<Sector>()
        for (i in 0 until session1w_1s.replays) {
            val number = i + 1
            sectors1w_1s.add(Sector(session1w_1s.sessionId, number, hashMapPlanning!!.get("400")!!))
        }
        val sectors2w_1s = arrayListOf<Sector>()
        for (i in 0 until session2w_1s.replays) {
            val number = i + 1
            sectors2w_1s.add(Sector(session2w_1s.sessionId, number, hashMapPlanning!!.get("800")!!))
        }
        val sectors3w_1s = arrayListOf<Sector>()
        for (i in 0 until session3w_1s.replays) {
            val number = i + 1
            if (i < 2) {
                sectors3w_1s.add(Sector(session3w_1s.sessionId, number, hashMapPlanning!!.get("1600")!!))
            } else {
                sectors3w_1s.add(Sector(session3w_1s.sessionId, number, hashMapPlanning!!.get("800")!!))
            }
        }
        val sectors4w_1s = arrayListOf<Sector>()
        for (i in 0 until session4w_1s.replays) {
            val number = i + 1
            if (i == 0 || i == 5) {
                sectors4w_1s.add(Sector(session4w_1s.sessionId, number, hashMapPlanning!!.get("400")!!))
            } else if (i == 1 || i == 4) {
                sectors4w_1s.add(Sector(session4w_1s.sessionId, number, hashMapPlanning!!.get("600")!!))
            } else {
                sectors4w_1s.add(Sector(session4w_1s.sessionId, number, hashMapPlanning!!.get("800")!!))
            }
        }
        val sectors5w_1s = arrayListOf<Sector>()
        for (i in 0 until session5w_1s.replays) {
            val number = i + 1
            sectors5w_1s.add(Sector(session5w_1s.sessionId, number, hashMapPlanning!!.get("1000")!!))
        }
        val sectors6w_1s = arrayListOf<Sector>()
        sectors6w_1s.add(Sector(session6w_1s.sessionId, 1, hashMapPlanning!!.get("1600")!!))
        sectors6w_1s.add(Sector(session6w_1s.sessionId, 2, hashMapPlanning!!.get("1200")!!))
        sectors6w_1s.add(Sector(session6w_1s.sessionId, 3, hashMapPlanning!!.get("800")!!))
        sectors6w_1s.add(Sector(session6w_1s.sessionId, 4, hashMapPlanning!!.get("400")!!))
        val sectors7w_1s = arrayListOf<Sector>()
        for (i in 0 until session7w_1s.replays) {
            val number = i + 1
            sectors7w_1s.add(Sector(session7w_1s.sessionId, number, hashMapPlanning.get("400")!!))
        }
        val sectors8w_1s = arrayListOf<Sector>()
        for (i in 0 until session8w_1s.replays) {
            val number = i + 1
            sectors8w_1s.add(Sector(session8w_1s.sessionId, number, hashMapPlanning.get("800")!!))
        }
        val sectors9w_1s = arrayListOf<Sector>()
        for (i in 0 until session9w_1s.replays) {
            val number = i + 1
            sectors9w_1s.add(Sector(session9w_1s.sessionId, number, hashMapPlanning.get("1200")!!))
        }
        val sectors10w_1s = arrayListOf<Sector>()
        for (i in 0 until session10w_1s.replays) {
            val number = i + 1
            sectors10w_1s.add(Sector(session10w_1s.sessionId, number, hashMapPlanning.get("1000")!!))
        }
        val sectors11w_1s = arrayListOf<Sector>()
        for (i in 0 until session11w_1s.replays) {
            val number = i + 1
            sectors11w_1s.add(Sector(session11w_1s.sessionId, number, hashMapPlanning.get("1600")!!))
        }
        val sectors12w_1s = arrayListOf<Sector>()
        for (i in 0 until session12w_1s.replays) {
            val number = i + 1
            sectors12w_1s.add(Sector(session12w_1s.sessionId, number, hashMapPlanning.get("400")!!))
        }
        // CARRERA CORTA (Xw_2s)
        val sectors1w_2s = Sector(session1w_2s.sessionId, 1, hashMapPlanning.get("corto")!!)
        val sectors2w_2s = Sector(session2w_2s.sessionId, 1, hashMapPlanning.get("corto")!!)
        val sectors3w_2s = arrayListOf<Sector>()
        sectors3w_2s.add(Sector(session3w_2s.sessionId, 1, hashMapPlanning.get("corto")!!))
        sectors3w_2s.add(Sector(session3w_2s.sessionId, 2, hashMapPlanning.get("facil")!!))
        sectors3w_2s.add(Sector(session3w_2s.sessionId, 3, hashMapPlanning.get("corto")!!))
        val sectors4w_2s = Sector(session4w_2s.sessionId, 1, hashMapPlanning.get("medio")!!)
        val sectors5w_2s = Sector(session5w_2s.sessionId, 1, hashMapPlanning.get("corto")!!)
        val sectors6w_2s = arrayListOf<Sector>()
        sectors6w_2s.add(Sector(session6w_2s.sessionId, 1, hashMapPlanning.get("corto")!!))
        sectors6w_2s.add(Sector(session6w_2s.sessionId, 2, hashMapPlanning.get("facil")!!))
        sectors6w_2s.add(Sector(session6w_2s.sessionId, 3, hashMapPlanning.get("corto")!!))
        sectors6w_2s.add(Sector(session6w_2s.sessionId, 4, hashMapPlanning.get("facil")!!))
        sectors6w_2s.add(Sector(session6w_2s.sessionId, 5, hashMapPlanning.get("corto")!!))
        val sectors7w_2s = Sector(session7w_2s.sessionId, 1, hashMapPlanning.get("medio")!!)
        val sectors8w_2s = arrayListOf<Sector>()
        sectors8w_2s.add(Sector(session8w_2s.sessionId, 1, hashMapPlanning.get("corto")!!))
        sectors8w_2s.add(Sector(session8w_2s.sessionId, 2, hashMapPlanning.get("facil")!!))
        sectors8w_2s.add(Sector(session8w_2s.sessionId, 3, hashMapPlanning.get("corto")!!))
        val sectors9w_2s = Sector(session9w_2s.sessionId, 1, hashMapPlanning.get("corto")!!)
        val sectors10w_2s = arrayListOf<Sector>()
        sectors10w_2s.add(Sector(session10w_2s.sessionId, 1, hashMapPlanning.get("corto")!!))
        sectors10w_2s.add(Sector(session10w_2s.sessionId, 2, hashMapPlanning.get("facil")!!))
        sectors10w_2s.add(Sector(session10w_2s.sessionId, 3, hashMapPlanning.get("corto")!!))
        sectors10w_2s.add(Sector(session10w_2s.sessionId, 4, hashMapPlanning.get("facil")!!))
        sectors10w_2s.add(Sector(session10w_2s.sessionId, 5, hashMapPlanning.get("corto")!!))
        val sectors11w_2s = Sector(session11w_2s.sessionId, 1, hashMapPlanning.get("corto")!!)
        val sectors12w_2s = Sector(session12w_2s.sessionId, 1, hashMapPlanning.get("facil")!!)
        // CARRERA LARGA (Xw_3s)
        val sectors1w_3s = Sector(session1w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors2w_3s = Sector(session2w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors3w_3s = Sector(session3w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors4w_3s = Sector(session4w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors5w_3s = Sector(session5w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors6w_3s = Sector(session6w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors7w_3s = Sector(session7w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors8w_3s = Sector(session8w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors9w_3s = Sector(session9w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors10w_3s = Sector(session10w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors11w_3s = Sector(session11w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors12w_3s = Sector(session12w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors3s_List = arrayListOf(sectors1w_3s, sectors2w_3s, sectors3w_3s, sectors4w_3s, sectors5w_3s, sectors6w_3s, sectors7w_3s,
                sectors8w_3s, sectors9w_3s, sectors10w_3s, sectors11w_3s, sectors12w_3s)
        // Guardar en Room
        lifecycleScope.launch {
            trainingDbInstance?.addTraining(training)
            for (week: Week in weekList) {
                weekDbInstance?.addWeek(week)
            }
            for (session: Session in session1s_List) {
                sessionDbInstance?.addSession(session)
            }
            for (session: Session in session2s_List) {
                sessionDbInstance?.addSession(session)
            }
            for (session: Session in session3s_List) {
                sessionDbInstance?.addSession(session)
            }
            for (sector: Sector in sectors1w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors2w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors3w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors4w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors5w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors6w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors7w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors8w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors9w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors10w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors11w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors12w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            sectorDbInstance?.addSector(sectors1w_2s)
            sectorDbInstance?.addSector(sectors2w_2s)
            for (sector: Sector in sectors3w_2s) {
                sectorDbInstance?.addSector(sector)
            }
            sectorDbInstance?.addSector(sectors4w_2s)
            sectorDbInstance?.addSector(sectors5w_2s)
            for (sector: Sector in sectors6w_2s) {
                sectorDbInstance?.addSector(sector)
            }
            sectorDbInstance?.addSector(sectors7w_2s)
            for (sector: Sector in sectors8w_2s) {
                sectorDbInstance?.addSector(sector)
            }
            sectorDbInstance?.addSector(sectors9w_2s)
            for (sector: Sector in sectors10w_2s) {
                sectorDbInstance?.addSector(sector)
            }
            sectorDbInstance?.addSector(sectors11w_2s)
            sectorDbInstance?.addSector(sectors12w_2s)
            for (sector: Sector in sectors3s_List) {
                sectorDbInstance?.addSector(sector)
            }
        }
        Log.i("CREATION TRAINING", "Done...")
        Snackbar.make(view, "¡Entrenamiento de 5 Kilómetros creado!", Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

    private fun creation10km(view: View) {
        // Creamos el entrenamiento
        training = Training(trainingName!!.getText().toString(), startingDate, spinner_distancePlan?.getSelectedItem().toString(), actualTime!!.getText().toString())
        // Creamos las semanas, agregandoles el id del entrenamiento y el numero de semana que es
        val week1 = Week(training!!.trainingId, 1)
        val week2 = Week(training!!.trainingId, 2)
        val week3 = Week(training!!.trainingId, 3)
        val week4 = Week(training!!.trainingId, 4)
        val week5 = Week(training!!.trainingId, 5)
        val week6 = Week(training!!.trainingId, 6)
        val week7 = Week(training!!.trainingId, 7)
        val week8 = Week(training!!.trainingId, 8)
        val week9 = Week(training!!.trainingId, 9)
        val week10 = Week(training!!.trainingId, 10)
        val week11 = Week(training!!.trainingId, 11)
        val week12 = Week(training!!.trainingId, 12)
        val weekList = arrayListOf(week1, week2, week3, week4, week5, week6, week7, week8, week9, week10, week11, week12)

        // Creamos las sesiones
        // week1
        val session1w_1s = Session(week1.weekId, 1, 8, "400", addDaystoDate(startingDate!!,1), "400m")
        val session2w_1s = Session(week2.weekId, 1, 5, "800", addDaystoDate(startingDate!!,8), "400m")
        val session3w_1s = Session(week3.weekId, 1, 3, "1600, 1600, 800", addDaystoDate(startingDate!!,15), "400m")
        val session4w_1s = Session(week4.weekId, 1, 6, "400, 600, 800, 800, 600, 400", addDaystoDate(startingDate!!,22), "400m")
        val session5w_1s = Session(week5.weekId, 1, 4, "1000", addDaystoDate(startingDate!!,29), "400m")
        val session6w_1s = Session(week6.weekId, 1, 4, "1600, 1200, 800, 400", addDaystoDate(startingDate!!,36), "400m")
        val session7w_1s = Session(week7.weekId, 1, 10, "400", addDaystoDate(startingDate!!,43), "90seg")
        val session8w_1s = Session(week8.weekId, 1, 6, "800", addDaystoDate(startingDate!!,50), "400m")
        val session9w_1s = Session(week9.weekId, 1, 4, "1200", addDaystoDate(startingDate!!,57), "400m")
        val session10w_1s = Session(week10.weekId, 1, 5, "1000", addDaystoDate(startingDate!!,64), "400m")
        val session11w_1s = Session(week11.weekId, 1, 3, "1600", addDaystoDate(startingDate!!,71), "400m")
        val session12w_1s = Session(week12.weekId, 1, 6, "400", addDaystoDate(startingDate!!,78), "400m")
        val session1s_List = arrayListOf(session1w_1s, session2w_1s, session3w_1s, session4w_1s, session5w_1s, session6w_1s, session7w_1s, session8w_1s, session9w_1s, session10w_1s, session11w_1s, session12w_1s)
        // week2
        val session1w_2s = Session(week1.weekId, 2, 1, "3K", addDaystoDate(startingDate!!,4), null)
        val session2w_2s = Session(week2.weekId, 2, 3, "3K, 1.5K, 3K", addDaystoDate(startingDate!!,11), null)
        val session3w_2s = Session(week3.weekId, 2, 1, "6.5K", addDaystoDate(startingDate!!,18), null)
        val session4w_2s = Session(week4.weekId, 2, 5, "3K, 1.5k, 1.5K, 1.5K, 3K", addDaystoDate(startingDate!!,25), null)
        val session5w_2s = Session(week5.weekId, 2, 1, "6.5K", addDaystoDate(startingDate!!,32), null)
        val session6w_2s = Session(week6.weekId, 2, 1, "8K", addDaystoDate(startingDate!!,39), null)
        val session7w_2s = Session(week7.weekId, 2, 1, "6.5K", addDaystoDate(startingDate!!,46), null)
        val session8w_2s = Session(week8.weekId, 2, 5, "1.5K, 1.5K, 3K, 1.5K, 1.5K",addDaystoDate(startingDate!!,53), null)
        val session9w_2s = Session(week9.weekId, 2, 1, "5K", addDaystoDate(startingDate!!,60), null)
        val session10w_2s = Session(week10.weekId, 2, 1, "10K", addDaystoDate(startingDate!!,67), null)
        val session11w_2s = Session(week11.weekId, 2, 1, "5K", addDaystoDate(startingDate!!,74), null)
        val session12w_2s = Session(week12.weekId, 2, 1, "5K", addDaystoDate(startingDate!!,81), null)
        val session2s_List = arrayListOf(session1w_2s, session2w_2s, session3w_2s, session4w_2s, session5w_2s, session6w_2s, session7w_2s, session8w_2s, session9w_2s, session10w_2s, session11w_2s, session12w_2s)
        //week3
        val session1w_3s = Session(week1.weekId, 3, 1, "10K", addDaystoDate(startingDate!!,6), null)
        val session2w_3s = Session(week2.weekId, 3, 1, "11K", addDaystoDate(startingDate!!,13), null)
        val session3w_3s = Session(week3.weekId, 3, 1, "13K", addDaystoDate(startingDate!!,20), null)
        val session4w_3s = Session(week4.weekId, 3, 1, "14K", addDaystoDate(startingDate!!,27), null)
        val session5w_3s = Session(week5.weekId, 3, 1, "16K", addDaystoDate(startingDate!!,34), null)
        val session6w_3s = Session(week6.weekId, 3, 1, "13K", addDaystoDate(startingDate!!,41), null)
        val session7w_3s = Session(week7.weekId, 3, 1, "16K", addDaystoDate(startingDate!!,48), null)
        val session8w_3s = Session(week8.weekId, 3, 1, "13K", addDaystoDate(startingDate!!,55), null)
        val session9w_3s = Session(week9.weekId, 3, 1, "16K", addDaystoDate(startingDate!!,62), null)
        val session10w_3s = Session(week10.weekId, 3, 1, "13K", addDaystoDate(startingDate!!,69), null)
        val session11w_3s = Session(week11.weekId, 3, 1, "11K", addDaystoDate(startingDate!!,76), null)
        val session12w_3s = Session(week12.weekId, 3, 1, "10K", addDaystoDate(startingDate!!,83), null)
        val session3s_List = arrayListOf(session1w_3s, session2w_3s, session3w_3s, session4w_3s, session5w_3s, session6w_3s, session7w_3s,
                session8w_3s, session9w_3s, session10w_3s, session11w_3s, session12w_3s)
        // Creamos los sectores y lo guardamos en un array para pasarselo a las sesiones
        // SERIES (Xw_1s)
        val sectors1w_1s = arrayListOf<Sector>()
        for (i in 0 until session1w_1s.replays) {
            val number = i + 1
            sectors1w_1s.add(Sector(session1w_1s.sessionId, number, hashMapPlanning!!.get("400")!!))
        }
        val sectors2w_1s = arrayListOf<Sector>()
        for (i in 0 until session2w_1s.replays) {
            val number = i + 1
            sectors2w_1s.add(Sector(session2w_1s.sessionId, number, hashMapPlanning!!.get("800")!!))
        }
        val sectors3w_1s = arrayListOf<Sector>()
        for (i in 0 until session3w_1s.replays) {
            val number = i + 1
            if (i < 2) {
                sectors3w_1s.add(Sector(session3w_1s.sessionId, number, hashMapPlanning!!.get("1600")!!))
            } else {
                sectors3w_1s.add(Sector(session3w_1s.sessionId, number, hashMapPlanning!!.get("800")!!))
            }
        }
        val sectors4w_1s = arrayListOf<Sector>()
        for (i in 0 until session4w_1s.replays) {
            val number = i + 1
            if (i == 0 || i == 5) {
                sectors4w_1s.add(Sector(session4w_1s.sessionId, number, hashMapPlanning!!.get("400")!!))
            } else if (i == 1 || i == 4) {
                sectors4w_1s.add(Sector(session4w_1s.sessionId, number, hashMapPlanning!!.get("600")!!))
            } else {
                sectors4w_1s.add(Sector(session4w_1s.sessionId, number, hashMapPlanning!!.get("800")!!))
            }
        }
        val sectors5w_1s = arrayListOf<Sector>()
        for (i in 0 until session5w_1s.replays) {
            val number = i + 1
            sectors5w_1s.add(Sector(session5w_1s.sessionId, number, hashMapPlanning!!.get("1000")!!))
        }
        val sectors6w_1s = arrayListOf<Sector>()
        sectors6w_1s.add(Sector(session6w_1s.sessionId, 1, hashMapPlanning!!.get("1600")!!))
        sectors6w_1s.add(Sector(session6w_1s.sessionId, 2, hashMapPlanning!!.get("1200")!!))
        sectors6w_1s.add(Sector(session6w_1s.sessionId, 3, hashMapPlanning!!.get("800")!!))
        sectors6w_1s.add(Sector(session6w_1s.sessionId, 4, hashMapPlanning!!.get("400")!!))
        val sectors7w_1s = arrayListOf<Sector>()
        for (i in 0 until session7w_1s.replays) {
            val number = i + 1
            sectors7w_1s.add(Sector(session7w_1s.sessionId, number, hashMapPlanning.get("400")!!))
        }
        val sectors8w_1s = arrayListOf<Sector>()
        for (i in 0 until session8w_1s.replays) {
            val number = i + 1
            sectors8w_1s.add(Sector(session8w_1s.sessionId, number, hashMapPlanning.get("800")!!))
        }
        val sectors9w_1s = arrayListOf<Sector>()
        for (i in 0 until session9w_1s.replays) {
            val number = i + 1
            sectors9w_1s.add(Sector(session9w_1s.sessionId, number, hashMapPlanning.get("1200")!!))
        }
        val sectors10w_1s = arrayListOf<Sector>()
        for (i in 0 until session10w_1s.replays) {
            val number = i + 1
            sectors10w_1s.add(Sector(session10w_1s.sessionId, number, hashMapPlanning.get("1000")!!))
        }
        val sectors11w_1s = arrayListOf<Sector>()
        for (i in 0 until session11w_1s.replays) {
            val number = i + 1
            sectors11w_1s.add(Sector(session11w_1s.sessionId, number, hashMapPlanning.get("1600")!!))
        }
        val sectors12w_1s = arrayListOf<Sector>()
        for (i in 0 until session12w_1s.replays) {
            val number = i + 1
            sectors12w_1s.add(Sector(session12w_1s.sessionId, number, hashMapPlanning.get("400")!!))
        }
        // CARRERA CORTA (Xw_2s)
        val sectors1w_2s = Sector(session1w_2s.sessionId, 1, hashMapPlanning.get("corto")!!)
        val sectors2w_2s = arrayListOf<Sector>()
        sectors2w_2s.add(Sector(session2w_2s.sessionId, 1, hashMapPlanning.get("corto")!!))
        sectors2w_2s.add(Sector(session2w_2s.sessionId, 2, hashMapPlanning.get("facil")!!))
        sectors2w_2s.add(Sector(session2w_2s.sessionId, 3, hashMapPlanning.get("corto")!!))
        val sectors3w_2s = Sector(session3w_2s.sessionId, 1, hashMapPlanning.get("medio")!!)
        val sectors4w_2s = arrayListOf<Sector>()
        sectors4w_2s.add(Sector(session4w_2s.sessionId, 1, hashMapPlanning.get("corto")!!))
        sectors4w_2s.add(Sector(session4w_2s.sessionId, 2, hashMapPlanning.get("facil")!!))
        sectors4w_2s.add(Sector(session4w_2s.sessionId, 3, hashMapPlanning.get("corto")!!))
        sectors4w_2s.add(Sector(session4w_2s.sessionId, 4, hashMapPlanning.get("facil")!!))
        sectors4w_2s.add(Sector(session4w_2s.sessionId, 5, hashMapPlanning.get("corto")!!))
        val sectors5w_2s = Sector(session5w_2s.sessionId, 1, hashMapPlanning.get("corto")!!)
        val sectors6w_2s = Sector(session6w_2s.sessionId, 1, hashMapPlanning.get("medio")!!)
        val sectors7w_2s = Sector(session7w_2s.sessionId, 1, hashMapPlanning.get("corto")!!)
        val sectors8w_2s = arrayListOf<Sector>()
        sectors8w_2s.add(Sector(session8w_2s.sessionId, 1, hashMapPlanning.get("corto")!!))
        sectors8w_2s.add(Sector(session8w_2s.sessionId, 2, hashMapPlanning.get("facil")!!))
        sectors8w_2s.add(Sector(session8w_2s.sessionId, 3, hashMapPlanning.get("corto")!!))
        sectors8w_2s.add(Sector(session8w_2s.sessionId, 4, hashMapPlanning.get("facil")!!))
        sectors8w_2s.add(Sector(session8w_2s.sessionId, 5, hashMapPlanning.get("corto")!!))
        val sectors9w_2s = Sector(session9w_2s.sessionId, 1, hashMapPlanning.get("corto")!!)
        val sectors10w_2s = Sector(session10w_2s.sessionId, 1, hashMapPlanning.get("medio")!!)
        val sectors11w_2s = Sector(session11w_2s.sessionId, 1, hashMapPlanning.get("corto")!!)
        val sectors12w_2s = Sector(session12w_2s.sessionId, 1, hashMapPlanning.get("facil")!!)
        // CARRERA LARGA (Xw_3s)
        val sectors1w_3s = Sector(session1w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors2w_3s = Sector(session2w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors3w_3s = Sector(session3w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors4w_3s = Sector(session4w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors5w_3s = Sector(session5w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors6w_3s = Sector(session6w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors7w_3s = Sector(session7w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors8w_3s = Sector(session8w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors9w_3s = Sector(session9w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors10w_3s = Sector(session10w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors11w_3s = Sector(session11w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors12w_3s = Sector(session12w_3s.sessionId, 1, hashMapPlanning.get("largo")!!)
        val sectors3s_List = arrayListOf(sectors1w_3s, sectors2w_3s, sectors3w_3s, sectors4w_3s, sectors5w_3s, sectors6w_3s, sectors7w_3s,
                sectors8w_3s, sectors9w_3s, sectors10w_3s, sectors11w_3s, sectors12w_3s)
        // Guardar en Room
        lifecycleScope.launch {
            trainingDbInstance?.addTraining(training)
            for (week: Week in weekList) {
                weekDbInstance?.addWeek(week)
            }
            for (session: Session in session1s_List) {
                sessionDbInstance?.addSession(session)
            }
            for (session: Session in session2s_List) {
                sessionDbInstance?.addSession(session)
            }
            for (session: Session in session3s_List) {
                sessionDbInstance?.addSession(session)
            }
            for (sector: Sector in sectors1w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors2w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors3w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors4w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors5w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors6w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors7w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors8w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors9w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors10w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors11w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            for (sector: Sector in sectors12w_1s) {
                sectorDbInstance?.addSector(sector)
            }
            sectorDbInstance?.addSector(sectors1w_2s)
            for (sector: Sector in sectors2w_2s) {
                sectorDbInstance?.addSector(sector)
            }
            sectorDbInstance?.addSector(sectors3w_2s)
            for (sector: Sector in sectors4w_2s) {
                sectorDbInstance?.addSector(sector)
            }
            sectorDbInstance?.addSector(sectors5w_2s)
            sectorDbInstance?.addSector(sectors6w_2s)
            sectorDbInstance?.addSector(sectors7w_2s)
            for (sector: Sector in sectors8w_2s) {
                sectorDbInstance?.addSector(sector)
            }
            sectorDbInstance?.addSector(sectors9w_2s)
            sectorDbInstance?.addSector(sectors10w_2s)
            sectorDbInstance?.addSector(sectors11w_2s)
            sectorDbInstance?.addSector(sectors12w_2s)
            for (sector: Sector in sectors3s_List) {
                sectorDbInstance?.addSector(sector)
            }
        }
        Log.i("CREATION TRAINING", "Done...")
        Snackbar.make(view, "¡Entrenamiento de 10 Kilómetros creado!", Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

    private fun creationMarathon(view: View) {
        Snackbar.make(view, "No ha sido disponible crear este tipo de entrenamiento. Lo sentimos", Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun fillHashMapWithTrainingTimes() {
        // CALCULO DEL FACTOR EN FUNCION DEL TIEMPO
        val factor = calculateFactor(actualTime!!.getText().toString())
        hashMapPlanning?.set("400", calculateTimeSector(67000f, 800f, factor))
        hashMapPlanning?.set("600", calculateTimeSector(103000f,1200f, factor))
        hashMapPlanning?.set("800", calculateTimeSector(138000f, 1600f, factor))
        hashMapPlanning?.set("1000", calculateTimeSector(175000f, 2000f, factor))
        hashMapPlanning?.set("1200", calculateTimeSector(214000f, 2400f, factor))
        hashMapPlanning?.set("1600", calculateTimeSector(293000f, 3200f, factor))
        hashMapPlanning?.set("2000", calculateTimeSector(371000f, 4000f, factor))
        hashMapPlanning?.set("corto", calculateTimeSector(202000f, 2000f, factor))
        hashMapPlanning?.set("medio", calculateTimeSector(212000f, 2000f, factor))
        hashMapPlanning?.set("largo", calculateTimeSector(221000f, 2000f, factor))
        hashMapPlanning?.set("facil", plusTime(calculateTimeSector(221000f, 2000f, factor), 41000f))
        hashMapPlanning?.set("mar", calculateTimeSector(221000f, 2300f, factor))
        hashMapPlanning?.set("mediamar", calculateTimeSector(211000f, 2200f, factor))
    }

    private fun calculateTimeSector(initialTime: Float?, baseTime: Float?, factor: Int): Float? {
        // tiempo inicial 16:00 segun distancia + factor * tiempo base 0:10 segun distancia
        val time: Float = initialTime!! + (factor * baseTime!!).toFloat()
        return time
    }

    private fun plusTime(initialTime: Float?, extraTime: Float?): Float? {
        val milliseconds = initialTime!! + extraTime!!
        return milliseconds
    }

    // To get the time diference between
    private fun calculateFactor(actualTime: String?): Int {
        var factor = 0
        try {
            val formatDate = SimpleDateFormat("HH:mm:ss")
            val dateStart = formatDate.parse("00:16:00")
            val dateEnd = formatDate.parse(actualTime)
            val milliseconds = dateEnd.time - dateStart.time.toFloat()
            factor = ((milliseconds / 10000).toInt())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        Log.i("Factor", Integer.toString(factor))
        return factor
    }

    private fun hashMapTrainings() {
        hashMapTraining?.set("5 Kilómetros", 0)
        hashMapTraining?.set("10 Kilómetros", 1)
        hashMapTraining?.set("Marathon", 2)
        hashMapTraining?.set("Media Marathon", 3)
        hashMapTraining?.set("Marathon para principiantes", 4)
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        open fun onFragmentInteraction(uri: Uri?)
    }


    // Suma los días recibidos a la fecha
    private fun addDaystoDate(fecha: Date, dias: Int): Date{
        var calendar : Calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }
}
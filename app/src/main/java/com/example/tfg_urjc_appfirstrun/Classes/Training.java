package com.example.tfg_urjc_appfirstrun.Classes;

import java.util.ArrayList;
import java.util.UUID;

public class Training {
    private String uuid;
    private String idRunner;
    private boolean isFinished;
    private String typeTraining;
    private String mark5Km;
    private Week week1;
    private Week week2;
    private Week week3;
    private Week week4;
    private Week week5;
    private Week week6;
    private Week week7;
    private Week week8;
    private Week week9;
    private Week week10;
    private Week week11;
    private Week week12;
    private Week week13;
    private Week week14;
    private Week week15;
    private Week week16;

    public Training () {
        this.uuid = UUID.randomUUID().toString();
        this.idRunner = null;
        this.isFinished = false;
        this.typeTraining = null;
        this.mark5Km = null;
        this.week1 = null;
        this.week2 = null;
        this.week3 = null;
        this.week4 = null;
        this.week5 = null;
        this.week6 = null;
        this.week7 = null;
        this.week8 = null;
        this.week9 = null;
        this.week10 = null;
        this.week11 = null;
        this.week12 = null;
        this.week13 = null;
        this.week14 = null;
        this.week15 = null;
        this.week16 = null;
    }

    public Training(String idRunner, String typeTraining, String mark5Km, ArrayList<Week> weeks){
        this.uuid = UUID.randomUUID().toString();
        this.idRunner = idRunner;
        this.isFinished = false;
        this.typeTraining = typeTraining;
        this.mark5Km = mark5Km;
        this.week1 = weeks.get(0);
        this.week2 = weeks.get(1);
        this.week3 = weeks.get(2);
        this.week4 = weeks.get(3);
        this.week5 = weeks.get(4);
        this.week6 = weeks.get(5);
        this.week7 = weeks.get(6);
        this.week8 = weeks.get(7);
        this.week9 = weeks.get(8);
        this.week10 = weeks.get(9);
        this.week11 = weeks.get(10);
        this.week12 = weeks.get(11);
        this.week13 = weeks.get(12);
        this.week14 = weeks.get(13);
        this.week15 = weeks.get(14);
        this.week16 = weeks.get(15);
    }
}

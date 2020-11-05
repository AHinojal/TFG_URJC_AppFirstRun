package com.example.tfg_urjc_appfirstrun.Classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Training {
    public String uuid;
    public String idRunner;
    public String name;
    public Date startDate;
    public boolean isFinished;
    public String typeTraining;
    public String mark5Km;
    public Week week1;
    public Week week2;
    public Week week3;
    public Week week4;
    public Week week5;
    public Week week6;
    public Week week7;
    public Week week8;
    public Week week9;
    public Week week10;
    public Week week11;
    public Week week12;
    public Week week13;
    public Week week14;
    public Week week15;
    public Week week16;

    public Training () {
        this.uuid = UUID.randomUUID().toString();
        this.name = null;
        this.idRunner = null;
        this.startDate = null;
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

    public Training(String idRunner, String name, Date startDate, String typeTraining, String mark5Km, ArrayList<Week> weeks){
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.idRunner = idRunner;
        this.startDate = startDate;
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

package com.example.tfg_urjc_appfirstrun.Classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Session {
    private String uuid;
    private int replays;
    private String distance;
    private Date sessionDay;
    private Sector sector1;
    private Sector sector2;
    private Sector sector3;
    private Sector sector4;
    private Sector sector5;
    private Sector sector6;
    private Sector sector7;
    private Sector sector8;
    private Sector sector9;
    private Sector sector10;
    private Sector sector11;
    private Sector sector12;

    public Session () {
        this.uuid = UUID.randomUUID().toString();
        this.replays = 0;
        this.distance = null;
        this.sessionDay = new Date();
        this.sector1 = null;
        this.sector2 = null;
        this.sector3 = null;
        this.sector4 = null;
        this.sector5 = null;
        this.sector6 = null;
        this.sector7 = null;
        this.sector8 = null;
        this.sector9 = null;
        this.sector10 = null;
        this.sector11 = null;
        this.sector12 = null;
    }

    public Session (int replays, String distance, Date sessionDay, ArrayList<Sector> sectors){
        this.uuid = UUID.randomUUID().toString();
        this.replays = replays;
        this.distance = distance;
        this.sessionDay = sessionDay;
        this.sector1 = sectors.get(0);
        this.sector2 = sectors.get(1);
        this.sector3 = sectors.get(2);
        this.sector4 = sectors.get(3);
        this.sector5 = sectors.get(4);
        this.sector6 = sectors.get(5);
        this.sector7 = sectors.get(6);
        this.sector8 = sectors.get(7);
        this.sector9 = sectors.get(8);
        this.sector10 = sectors.get(9);
        this.sector11 = sectors.get(10);
        this.sector12 = sectors.get(11);
    }

    public String getUuid() {
        return uuid;
    }
}

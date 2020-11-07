package com.example.tfg_urjc_appfirstrun.Classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Session {
    public String _id;
    public int replays;
    public ArrayList<String> distance;
    public Date sessionDay;
    public String recoveryTime;
    public Sector sector1;
    public Sector sector2;
    public Sector sector3;
    public Sector sector4;
    public Sector sector5;
    public Sector sector6;
    public Sector sector7;
    public Sector sector8;
    public Sector sector9;
    public Sector sector10;
    public Sector sector11;
    public Sector sector12;

    public Session () {
        this._id = UUID.randomUUID().toString();
        this.replays = 0;
        this.distance = null;
        this.sessionDay = new Date();
        this.recoveryTime = null;
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
    // Constructor for only one session
    public Session (int replays, ArrayList<String> distance, Date sessionDay, String recoveryTime, Sector sector){
        this._id = UUID.randomUUID().toString();
        this.replays = replays;
        this.distance = distance;
        this.sessionDay = sessionDay;
        this.recoveryTime = recoveryTime;
        this.sector1 = sector;
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

    // Constructor for 2..N sectors
    public Session (int replays, ArrayList<String> distance, Date sessionDay, String recoveryTime, ArrayList<Sector> sectors){
        this._id = UUID.randomUUID().toString();
        this.replays = replays;
        this.distance = distance;
        this.sessionDay = sessionDay;
        this.recoveryTime = recoveryTime;
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

    public String get_id() {
        return _id;
    }
}

package com.example.tfg_urjc_appfirstrun.Classes;

import android.util.Log;

import java.util.UUID;

public class Week {
    public String uuid;
    public Session session1;
    public Session session2;
    public Session session3;

    public Week () {
        this.uuid = UUID.randomUUID().toString();
        this.session1 = new Session();
        this.session2 = new Session();
        this.session3 = new Session();
    }

    public Week (Session session1, Session session2, Session session3){
        this.uuid = UUID.randomUUID().toString();
        this.session1 = session1;
        this.session2 = session2;
        this.session3 = session3;
    }

    public String getUuid() {
        return uuid;
    }

    public Session getSessionById (String id){
        Session selectSession = null;
        if (this.session1.getUuid().equals(id)){
            selectSession = this.session1;
        } else if (this.session2.getUuid().equals(id)) {
            selectSession = this.session2;
        } else if (this.session3.getUuid().equals(id)){
            selectSession = this.session3;
        }
        return selectSession;
    }
}

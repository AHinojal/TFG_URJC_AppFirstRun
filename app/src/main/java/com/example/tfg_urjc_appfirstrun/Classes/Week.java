package com.example.tfg_urjc_appfirstrun.Classes;

import android.util.Log;

import java.util.UUID;

public class Week {
    public String _id;
    public Session session1;
    public Session session2;
    public Session session3;

    public Week () {
        this._id = UUID.randomUUID().toString();
        this.session1 = new Session();
        this.session2 = new Session();
        this.session3 = new Session();
    }

    public Week (Session session1, Session session2, Session session3){
        this._id = UUID.randomUUID().toString();
        this.session1 = session1;
        this.session2 = session2;
        this.session3 = session3;
    }

    public Session getSessionById (String id){
        Session selectSession = null;
        if (this.session1.get_id().equals(id)){
            selectSession = this.session1;
        } else if (this.session2.get_id().equals(id)) {
            selectSession = this.session2;
        } else if (this.session3.get_id().equals(id)){
            selectSession = this.session3;
        }
        return selectSession;
    }
}

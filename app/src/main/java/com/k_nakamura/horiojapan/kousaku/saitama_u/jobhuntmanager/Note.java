package com.k_nakamura.horiojapan.kousaku.saitama_u.jobhuntmanager;

import java.io.Serializable;

public class Note implements Serializable {
    protected int id;
    protected String note;
    protected String lastupdate;

    public Note(int id, String note, String lastupdate){
        this.id = id;
        this.note = note;
        this.lastupdate = lastupdate;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setNote(String note){
        this.note = note;
    }
    public void setLastupdate(String lastupdate){
        this.lastupdate = lastupdate;
    }

    public String getNote(){
        return note;
    }
    public String getLastupdate(){
        return lastupdate;
    }
    public int getId(){
        return id;
    }
}
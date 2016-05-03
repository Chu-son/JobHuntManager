package com.k_nakamura.horiojapan.kousaku.saitama_u.jobhuntmanager;

import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;

public class Company implements Serializable {
    protected int id;
    protected String company;
    protected String memo;
    protected String lastupdate;
    protected String kana;
    protected String scheduleDB;

    public Company(int id, String company, String lastupdate,String memo,String kana,String scheduleDB){
        this.id = id;
        this.company = company;
        this.lastupdate = lastupdate;
        this.memo = memo;
        this.kana = kana;
        this.scheduleDB = scheduleDB;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setCompany(String company){
        this.company = company;
    }
    public void setLastupdate(String lastupdate){
        this.lastupdate = lastupdate;
    }
    public void setMemo(String memo){
        this.memo = memo;
    }
    public void setScheduleDB(String scheduleDB){
        this.scheduleDB = scheduleDB;
    }
    public void setKana(String kana){
        this.kana = kana;
    }

    public String getCompany(){
        return company;
    }
    public String getLastupdate(){
        return lastupdate;
    }
    public int getId(){
        return id;
    }
    public String getMemo(){
        return memo;
    }
    public String getKana(){
        return kana;
    }
    public String getScheduleDB(){
        return scheduleDB;
    }
}
package com.k_nakamura.horiojapan.kousaku.saitama_u.jobhuntmanager;

import java.io.Serializable;

public class Company implements Serializable {
    protected int id;
    protected String company;
    protected String memo;
    protected String lastupdate;

    public Company(int id, String company, String lastupdate,String memo){
        this.id = id;
        this.company = company;
        this.lastupdate = lastupdate;
        this.memo = memo;
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
}
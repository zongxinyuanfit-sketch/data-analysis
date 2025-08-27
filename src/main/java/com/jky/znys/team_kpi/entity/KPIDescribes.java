package com.jky.znys.team_kpi.entity;

public class KPIDescribes {
    private int id;
    private String udescribe;

    private int vtype;

    private int kpimembers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVtype() {
        return vtype;
    }

    public void setVtype(int vtype) {
        this.vtype = vtype;
    }

    public String getUdescribe() {
        return udescribe;
    }

    public void setUdescribe(String udescribe) {
        this.udescribe = udescribe;
    }

    public int getKpimembers() {
        return kpimembers;
    }

    public void setKpimembers(int kpimembers) {
        this.kpimembers = kpimembers;
    }
}

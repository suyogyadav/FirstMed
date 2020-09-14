package com.kernel.firstmed;

import java.util.List;

class MedicinePOJO {
    int pid;
    String date;
    List<String> old_med;
    String old_des;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getOld_med() {
        return old_med;
    }

    public void setOld_med(List<String> old_med) {
        this.old_med = old_med;
    }

    public String getOld_des() {
        return old_des;
    }

    public void setOld_des(String old_des) {
        this.old_des = old_des;
    }
}

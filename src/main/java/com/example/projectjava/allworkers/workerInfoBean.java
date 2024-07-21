package com.example.projectjava.allworkers;

public class workerInfoBean {
        String wname;
        String mobile;
        String wadd;
        String splz;


    public workerInfoBean(String wname, String mobile, String wadd, String splz) {
        this.wname = wname;
        this.mobile = mobile;
        this.wadd = wadd;
        this.splz = splz;
    }
    public workerInfoBean() {

    }


    public String getWname() {
        return wname;
    }
    public void setWname(String wname) {
        this.wname = wname;
    }

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWadd() {
        return wadd;
    }
    public void setWadd(String wadd) {
        this.wadd = wadd;
    }

    public String getSplz() {
        return splz;
    }
    public void setSplz(String splz) {
        this.splz = splz;
    }
}

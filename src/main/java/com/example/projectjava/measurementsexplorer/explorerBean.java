package com.example.projectjava.measurementsexplorer;

import java.util.Date;

public class explorerBean {
    int orderId;
    String mobile;
    String dress;
    Date dod;
    String design_filename;
    int  qty;
    int ppu;
    int bill;
    String workerass;
    String msrmnts;
    int status;

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }



    public String getWorkerass() {
        return workerass;
    }
    public void setWorkerass(String workerass) {
        this.workerass = workerass;
    }

    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDress() {
        return dress;
    }
    public void setDress(String dress) {
        this.dress = dress;
    }

    public Date getDod() {
        return dod;
    }
    public void setDod(Date dod) {
        this.dod = dod;
    }

    public String getDesign_filename() {
        return design_filename;
    }
    public void setDesign_filename(String design_filename) {
        this.design_filename = design_filename;
    }

    public int getQty() {
        return qty;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPpu() {
        return ppu;
    }
    public void setPpu(int ppu) {
        this.ppu = ppu;
    }

    public int getBill() {
        return bill;
    }
    public void setBill(int bill) {
        this.bill = bill;
    }

    public String getMsrmnts() {
        return msrmnts;
    }
    public void setMsrmnts(String msrmnts) {
        this.msrmnts = msrmnts;
    }


    public explorerBean(int orderId, String mobile, String dress, Date dod, String design_filename, int qty, int ppu, int bill, String msrmnts,String workerass,int status) {
        this.orderId = orderId;
        this.mobile = mobile;
        this.dress = dress;
        this.dod = dod;
        this.design_filename = design_filename;
        this.qty = qty;
        this.ppu = ppu;
        this.bill = bill;
        this.msrmnts = msrmnts;
        this.workerass = workerass;
        this.status=status;
    }
    public explorerBean()
    {

    }
}

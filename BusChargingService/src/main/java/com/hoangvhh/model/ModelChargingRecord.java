/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hoangvhh.model;

import java.util.Date;

/**
 *This class define the model for each charging record in output CSV file
 * @author HoangVHH
 */
public class ModelChargingRecord {
    private Date Started;
    private Date Finished;
    private long DurationSecs;
    private String FromStopId;
    private String ToStopId;
    private float ChargeAmount;
    private String CompanyId;
    private String BusID;
    private String PAN;
    private STATUS Status;
    
    public enum STATUS {COMPLETED,INCOMPLETE,CANCELLED};

    public Date getStarted() {
        return Started;
    }

    public void setStarted(Date Started) {
        this.Started = Started;
    }

    public Date getFinished() {
        return Finished;
    }

    public void setFinished(Date Finished) {
        this.Finished = Finished;
    }

    public long getDurationSecs() {
        return DurationSecs;
    }

    public void setDurationSecs(long DurationSecs) {
        this.DurationSecs = DurationSecs;
    }

    public String getFromStopId() {
        return FromStopId;
    }

    public void setFromStopId(String FromStopId) {
        this.FromStopId = FromStopId;
    }

    public String getToStopId() {
        return ToStopId;
    }

    public void setToStopId(String ToStopId) {
        this.ToStopId = ToStopId;
    }

    public float getChargeAmount() {
        return ChargeAmount;
    }

    public void setChargeAmount(float ChargeAmount) {
        this.ChargeAmount = ChargeAmount;
    }

    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String CompanyId) {
        this.CompanyId = CompanyId;
    }

    public String getBusID() {
        return BusID;
    }

    public void setBusID(String BusID) {
        this.BusID = BusID;
    }

    public String getPAN() {
        return PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public STATUS getStatus() {
        return Status;
    }

    public void setStatus(STATUS Status) {
        this.Status = Status;
    }
    
    
}

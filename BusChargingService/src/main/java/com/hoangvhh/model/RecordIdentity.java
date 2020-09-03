/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hoangvhh.model;

import java.util.Objects;

/**
 *
 * @author HoangVHH
 * Each record will be identified based on 3 fields PAN,BusID, and CompanyID
 */
public class RecordIdentity {
    private String PAN;
    private String BusID;
    private String CompanyID;

    public RecordIdentity(String PAN, String BusID, String CompanyID) {
        this.PAN = PAN;
        this.BusID = BusID;
        this.CompanyID = CompanyID;
    }

    public String getPAN() {
        return PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public String getBusID() {
        return BusID;
    }

    public void setBusID(String BusID) {
        this.BusID = BusID;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String CompanyID) {
        this.CompanyID = CompanyID;
    }
    
    @Override
    public boolean equals(Object obj){
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RecordIdentity)) {
            return false;
        }
        RecordIdentity temp = (RecordIdentity)obj;
        return PAN.equals(temp.PAN)&&BusID.equals(temp.BusID)&&CompanyID.equals(temp.CompanyID);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.PAN);
        hash = 53 * hash + Objects.hashCode(this.BusID);
        hash = 53 * hash + Objects.hashCode(this.CompanyID);
        return hash;
    }
}

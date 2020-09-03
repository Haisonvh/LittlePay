/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hoangvhh.model;

import com.hoangvhh.utilities.Utils;
import java.util.Date;

/**
 * This class define the model for each route record in input CSV file
 * @author HoangVHH
 */
public class ModelRoute {
    double id;
    TAP_STATUS tapStatus;
    Date dateTime;
    String stopID;
    RecordIdentity identity;
    
    public enum TAP_STATUS{ON,OFF};
    
    public ModelRoute(double id, TAP_STATUS tapStatus, String dateTime, String stopID, RecordIdentity identity) {
        this.id = id;
        this.tapStatus = tapStatus;
        setDateTime(dateTime);
        this.stopID = stopID;
        this.identity = identity;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public TAP_STATUS getTapStatus() {
        return tapStatus;
    }

    public void setTapStatus(TAP_STATUS tapStatus) {
        this.tapStatus = tapStatus;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public final void setDateTime(String dateTime) {
        this.dateTime = Utils.stringToDate(dateTime);
    }

    public String getStopID() {
        return stopID;
    }

    public void setStopID(String stopID) {
        this.stopID = stopID;
    }

    public RecordIdentity getIdentity() {
        return identity;
    }

    public void setIdentity(RecordIdentity identity) {
        this.identity = identity;
    }
}

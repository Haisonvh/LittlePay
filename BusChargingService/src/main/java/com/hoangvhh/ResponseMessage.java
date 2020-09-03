/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hoangvhh;

/**
 *
 * @author HoangVHH
 */
public class ResponseMessage {
    private String messString;

    public ResponseMessage() {
    }

    public ResponseMessage(String messString) {
        this.messString = messString;
    }
    
    public String getMessString() {
        return messString;
    }

    public void setMessString(String messString) {
        this.messString = messString;
    }
    
}

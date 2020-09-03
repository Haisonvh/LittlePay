/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hoangvhh.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HoangVHH
 */
public class Utils {

    private static final Logger LOG = Logger.getLogger(Utils.class.getName());
    
    public static Date stringToDate(String sDate){
        Date response = null;
        try {
            
            DateFormat formatter=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            response = formatter.parse(sDate);
        } catch (ParseException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    public static String dateToString(Date date){
        DateFormat formatter=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(date);
    }
    
    public static long difInSecCalculate(Date fromDate, Date toDate){
        long diffInMillies = toDate.getTime() - fromDate.getTime();
        return TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}

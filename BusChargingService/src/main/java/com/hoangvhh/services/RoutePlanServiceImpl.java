package com.hoangvhh.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author HoangVHH
 * This bean use to read the route information which is store in RoutePlan.csv
 * so that the service can be easily update the route data.
 * Assume that stopID always start with "Stop" and it increases continuously
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RoutePlanServiceImpl implements RoutePlanService{

    private static final Logger LOG = Logger.getLogger(RoutePlanServiceImpl.class.getName());

    private final MultiKeyMap multiKeyMap;
    private int maxID = 1;

    public RoutePlanServiceImpl() {
        super();
        multiKeyMap = readRoutePlanCSVfile();
    }
    
    
    
    @Override
    public float getCompletedRouteCost(String fromStopID, String toStopID) {
        float response = 0;
        String cost = (String)multiKeyMap.get(fromStopID, toStopID);
        if (cost!=null)
            response = Float.parseFloat(cost);
        return response;
    }

    @Override
    public float getUncompletedRouteCost(String stopID) {
        float response = 0;
        //assume that the longest route in each side is most expensive
        float maxLeft = getCompletedRouteCost(stopID, "Stop1");
        float maxRight = getCompletedRouteCost(stopID, "Stop"+maxID);
        response = Float.max(maxLeft, maxRight);
        return response;
    }
    
    /**
     * Read the csv file which contains information of charging per route in the resources folder of project
     * @return MultiKeyMap with keys are fromStopID and toStopID 
     * @throws IOException 
     */
    private MultiKeyMap readRoutePlanCSVfile() throws RuntimeException{
        MultiKeyMap routeMap = new MultiKeyMap();
        try {            
            InputStream inputStream = getClass().getResourceAsStream("/RoutePlan.csv");
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream));
            CSVParser csvParser = new CSVParser(fileReader
                    , CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            List<CSVRecord> csvRecords = csvParser.getRecords();
            
            csvRecords.stream().forEach((record)->{
                String stopID1 = record.get("StopID1");
                String stopID2 = record.get("StopID2");
                String cost = record.get("Cost");
                
                int id = Integer.parseInt(stopID1.substring(4));
                maxID = Integer.max(id,maxID);
                
                id = Integer.parseInt(stopID2.substring(4));
                maxID = Integer.max(id,maxID);
                
                routeMap.put(stopID1, stopID2, cost);
                //add reverse route
                routeMap.put(stopID2, stopID1, cost);
            });
            
        } catch (IOException ex) {
            LOG.log(Level.CONFIG, "fail to parse CSV file: {0}", ex.getMessage());
            throw new RuntimeException("fail to parse CSV file: " + ex.getMessage());
        }
        return routeMap;
    }

}

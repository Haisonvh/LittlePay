/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hoangvhh.services;

import com.hoangvhh.model.ModelChargingRecord;
import com.hoangvhh.model.ModelRoute;
import java.io.FileNotFoundException;
import java.util.List;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

/**
 *This interface define business logic for this application
 * @author HoangVHH
 */
public interface LittlePayService {
    
    /**
     * Read file which is upload from client
     * @param file
     * @return data of file in List
     */
    List<ModelRoute> readCSVFile(MultipartFile file);
    
    /**
     * define the aggregation process to calculate the charging
     * @param datas
     * @return the charging record in List
     */
    List<ModelChargingRecord> aggregateData(List<ModelRoute> datas);
    
    /**
     * Allow to save to CSV file based on the input data with the reference 
     * @param datas the charging data
     * @param reference number which is used to create CSV file
     */
    void saveCSVFile(List<ModelChargingRecord> datas, String reference);
    
    /**
     * Return the CSV file in InputStreamResource format
     * @param reference which is used to create CSV file
     * @return
     * @throws FileNotFoundException 
     */
    InputStreamResource loadCSVFile(String reference) throws FileNotFoundException;
}

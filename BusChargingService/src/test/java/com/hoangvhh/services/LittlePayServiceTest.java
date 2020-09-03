/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoangvhh.services;

import com.hoangvhh.model.ModelChargingRecord;
import com.hoangvhh.model.ModelRoute;
import com.hoangvhh.model.RecordIdentity;
import com.hoangvhh.utilities.CSVRecordHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import org.junit.Assert;
import org.junit.Before;
import org.springframework.core.io.InputStreamResource;

/**
 *
 * @author HoangVHH
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class LittlePayServiceTest {

    @Autowired
    private LittlePayService littlePayService;

    @Test
    public void shouldCallCSVHelperToReadfileSuccessfully() {
        FileInputStream input = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("testData.csv").getFile());
            input = new FileInputStream(file); 
            MultipartFile mockMultipartFile = new MockMultipartFile("testData.csv",
                    "testData.csv", "text/csv", input);
            List<ModelRoute> datas = littlePayService.readCSVFile(mockMultipartFile);
            Assert.assertEquals(9, datas.size());        
        } catch (IOException ex) {
            Logger.getLogger(LittlePayServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(LittlePayServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    @Test
    public void shouldAggregateWithOnRecordSuccessfully() {           
        List<ModelRoute> datas = new ArrayList<>();
        datas.add(new ModelRoute(0, ModelRoute.TAP_STATUS.ON, "22-01-2018 13:00:00", "Stop1", new RecordIdentity("01", "C01", "B01")));
        List<ModelChargingRecord> output = littlePayService.aggregateData(datas);
        Assert.assertEquals(1, output.size()); 
        Assert.assertTrue(output.get(0).getStatus() == ModelChargingRecord.STATUS.INCOMPLETE);
    }
    
    @Test
    public void shouldAggregateWithCompletedRouteSuccessfully() {           
        List<ModelRoute> datas = new ArrayList<>();
        datas.add(new ModelRoute(0, ModelRoute.TAP_STATUS.ON, "22-01-2018 13:00:00", "Stop1", new RecordIdentity("01", "C01", "B01")));
        datas.add(new ModelRoute(1, ModelRoute.TAP_STATUS.OFF, "22-01-2018 13:10:00", "Stop2", new RecordIdentity("01", "C01", "B01")));
        List<ModelChargingRecord> output = littlePayService.aggregateData(datas);
        Assert.assertEquals(1, output.size()); 
        Assert.assertTrue(output.get(0).getStatus() == ModelChargingRecord.STATUS.COMPLETED);
    }
    
    @Test
    public void shouldAggregateWithCancelledRouteSuccessfully() {           
        List<ModelRoute> datas = new ArrayList<>();
        datas.add(new ModelRoute(0, ModelRoute.TAP_STATUS.ON, "22-01-2018 13:00:00", "Stop1", new RecordIdentity("01", "C01", "B01")));
        datas.add(new ModelRoute(1, ModelRoute.TAP_STATUS.OFF, "22-01-2018 13:00:20", "Stop1", new RecordIdentity("01", "C01", "B01")));
        List<ModelChargingRecord> output = littlePayService.aggregateData(datas);
        Assert.assertEquals(1, output.size()); 
        Assert.assertTrue(output.get(0).getStatus() == ModelChargingRecord.STATUS.CANCELLED);
    }
    
    @Test
    public void shouldAggregateWithMixRouteSuccessfully() {    
        
        FileInputStream input = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("testData.csv").getFile());
            input = new FileInputStream(file); 
            
            List<ModelRoute> datas = CSVRecordHelper.readCSVFile(input);
            List<ModelChargingRecord> output = littlePayService.aggregateData(datas);
            Assert.assertEquals(6, output.size());               
        } catch (IOException ex) {
            Logger.getLogger(LittlePayServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(LittlePayServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    @Test
    public void shouldSaveFileSuccessfully(){
        List<ModelChargingRecord> datas = new ArrayList<>();
        ModelChargingRecord data = new ModelChargingRecord();
        data.setBusID("B01");
        data.setChargeAmount(3.0f);
        data.setCompanyId("C01");
        data.setDurationSecs(30);
        data.setFinished(new Date());
        data.setFromStopId("Stop1");
        data.setPAN("P01");
        data.setStarted(new Date());
        data.setStatus(ModelChargingRecord.STATUS.COMPLETED);
        data.setToStopId("Stop2");
        datas.add(data);
        littlePayService.saveCSVFile(datas, "123456");
        File file = new File("./123456.csv");      
        Assert.assertTrue(file.getAbsolutePath().endsWith("123456.csv"));
    }
    
    @Test
    public void shouldLoadFileSuccessfully(){
        try {
            InputStreamResource out = littlePayService.loadCSVFile("123456");
            Assert.assertTrue(out.exists());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LittlePayServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void shouldThrowExceptionLoadFileUnsuccessfully(){
       Assert.assertThrows(FileNotFoundException.class, ()-> littlePayService.loadCSVFile("12345678"));
    }
}

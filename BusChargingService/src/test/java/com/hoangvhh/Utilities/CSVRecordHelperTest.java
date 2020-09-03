/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hoangvhh.Utilities;

import com.hoangvhh.model.ModelChargingRecord;
import com.hoangvhh.model.ModelRoute;
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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author HoangVHH
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CSVRecordHelperTest {
    @Test
    public void shouldReadFullTestFile(){
        FileInputStream inputStream = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("testData.csv").getFile());
            inputStream = new FileInputStream(file);
            List<ModelRoute> datas = CSVRecordHelper.readCSVFile(inputStream);
            Assert.assertEquals(0, datas.size(),9);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVRecordHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(CSVRecordHelperTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Test
    public void shouldWriteFileSuccess(){
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
        CSVRecordHelper.writeCSVFile(datas, "testOut.csv");
        File file = new File("./testOut.csv");        
        Assert.assertTrue(file.getAbsolutePath().endsWith("testOut.csv"));
    }
}

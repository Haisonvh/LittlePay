/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoangvhh;

import com.hoangvhh.services.LittlePayServiceTest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 *
 * @author HoangVHH
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  classes = Application.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ControllerTest {

    @Autowired
    private MockMvc mvc;
    
    @Test
    public void testUploadFile() {
        FileInputStream input = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("testData.csv").getFile());
            input = new FileInputStream(file); 
            MockMultipartFile mockMultipartFile = new MockMultipartFile("file",
                    "testData.csv", "text/csv", input);
            
            MvcResult result = mvc.perform(MockMvcRequestBuilders.multipart("/api/upload")
                    .file(mockMultipartFile))
                    .andExpect(status().is(200))
                    .andReturn();
            Assert.assertNotNull(result.getResponse().getContentAsString());
        } catch (IOException ex) {
            Logger.getLogger(LittlePayServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(LittlePayServiceTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Test
    public void testDownloadFile() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/download/123456")
                .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().is(200)).andReturn();
        
        Assert.assertTrue(result.getResponse().getContentAsByteArray().length > 0);
        Assert.assertEquals("application/csv", result.getResponse().getContentType());
    }
}

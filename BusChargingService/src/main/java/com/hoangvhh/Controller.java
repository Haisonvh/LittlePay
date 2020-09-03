/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoangvhh;

import com.hoangvhh.model.ModelChargingRecord;
import com.hoangvhh.model.ModelRoute;
import com.hoangvhh.services.LittlePayService;
import com.hoangvhh.utilities.CSVRecordHelper;
import com.hoangvhh.utilities.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author HoangVHH
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    LittlePayService littlePayService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String reference = Utils.genReference();
        if (CSVRecordHelper.isCSVFormat(file)) {
            CompletableFuture.runAsync(() -> {
                List<ModelRoute> routeDatas = littlePayService.readCSVFile(file);
                List<ModelChargingRecord> ChargingRecords = littlePayService.aggregateData(routeDatas);
                littlePayService.saveCSVFile(ChargingRecords, reference);
            });
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(reference));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Please upload a csv file!"));
    }
    
    @GetMapping("/download/{reference}")
    public ResponseEntity<Object> download(@PathVariable String reference) throws FileNotFoundException {
        String fileName = "./" + reference + ".csv";
        File file = new File(fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length()).
                contentType(MediaType.parseMediaType("application/csv")).body(resource);
        return responseEntity;
    }
}

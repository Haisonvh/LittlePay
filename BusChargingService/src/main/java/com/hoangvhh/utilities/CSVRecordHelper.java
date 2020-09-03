/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoangvhh.utilities;

import com.hoangvhh.model.ModelChargingRecord;
import com.hoangvhh.model.ModelRoute;
import com.hoangvhh.model.RecordIdentity;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import org.springframework.web.multipart.MultipartFile;

/**
 * This class define methods to read CSV file and write CSV file
 *
 * @author HoangVHH
 */
public class CSVRecordHelper {

    private static final Logger LOG = Logger.getLogger(CSVRecordHelper.class.getName());

    public static boolean isCSVFormat(MultipartFile file) {
        return file.getContentType().equals("text/csv");
    }

    public static List<ModelRoute> readCSVFile(InputStream inputStream) {
        List<ModelRoute> response = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            CSVParser csvParser = new CSVParser(reader
                    , CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            csvParser.getRecords().parallelStream().forEach((record) -> { 
                ModelRoute temp = new ModelRoute(Long.parseLong(record.get("ID")),
                        record.get("TapType").equals("ON") ? ModelRoute.TAP_STATUS.ON : ModelRoute.TAP_STATUS.OFF,
                        record.get("DateTimeUTC"),
                        record.get("StopId"),
                        new RecordIdentity(record.get("PAN"), record.get("CompanyId"), record.get("BusID")));
                response.add(temp);
            });
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    public static void writeCSVFile(List<ModelChargingRecord> dataList, String fileName) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL)
                .withHeader("Started", "Finished", "DurationSecs", "FromStopId", "ToStopId", "ChargeAmount", "CompanyId", "BusID", "PAN", "Status");

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);
            dataList.forEach((record) -> {
                try {                    
                    List<String> data = Arrays.asList(
                            Utils.dateToString(record.getStarted()),
                            Utils.dateToString(record.getFinished()),
                            String.valueOf(record.getDurationSecs()),
                            record.getFromStopId(),
                            record.getToStopId(),
                            String.valueOf(record.getChargeAmount()),
                            record.getFromStopId(),
                            record.getCompanyId(),
                            record.getBusID(),
                            record.getPAN(),
                            record.getStatus().name()
                    );
                    csvPrinter.printRecord(data);
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            });
            csvPrinter.flush();
            File convertFile = new File("./" + fileName);
            convertFile.createNewFile();
            FileOutputStream fout = new FileOutputStream(convertFile);
            out.writeTo(fout);
            fout.close();

        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
}

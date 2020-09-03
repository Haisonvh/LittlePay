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
import com.hoangvhh.utilities.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author HoangVHH
 */
@Service
public class LittlePayServiceImpl implements LittlePayService{

    private static final Logger LOG = Logger.getLogger(LittlePayServiceImpl.class.getName());

    @Autowired
    RoutePlanService routePlanService;
    
    @Override
    public List<ModelRoute> readCSVFile(MultipartFile file) {
        List<ModelRoute> response = new ArrayList<>();
        try {
            response = CSVRecordHelper.readCSVFile(file.getInputStream());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return response;
    }

    @Override
    public List<ModelChargingRecord> aggregateData(List<ModelRoute> datas) {
        //sort and groupby identity
        Map<RecordIdentity, List<ModelRoute>> groupMap = datas.stream().parallel()
                .sorted(Comparator.comparing(date -> date.getDateTime()))
                .collect(groupingBy(ModelRoute::getIdentity));
        
        //aggregate each group and merge the final result
        List<ModelChargingRecord> response = groupMap.entrySet().stream().parallel()
                .map((data) -> aggregate(data.getValue()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return response;     
    }

    @Override
    public void saveCSVFile(List<ModelChargingRecord> datas, String reference) {
        String fileName = reference + ".csv";
        CSVRecordHelper.writeCSVFile(datas, fileName);
    }

    @Override
    public InputStreamResource loadCSVFile(String reference) throws FileNotFoundException {
        String fileName = "./" + reference + ".csv";
        File file = new File(fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return resource;
    }
    
    private List<ModelChargingRecord> aggregate(List<ModelRoute> datas){
        List<ModelChargingRecord> records = new ArrayList<>();
        int index = 0;
        for (index = 0; index < datas.size() - 1; index++) {
            ModelRoute route = datas.get(index);
            ModelRoute route_next = datas.get(index + 1);
            ModelChargingRecord record = new ModelChargingRecord();
            record.setBusID(route.getIdentity().getBusID());
            record.setCompanyId(route.getIdentity().getCompanyID());
            record.setPAN(route.getIdentity().getPAN());
            record.setFromStopId(route.getStopID());
            record.setStarted(route.getDateTime());
            if (route_next.getTapStatus() == ModelRoute.TAP_STATUS.OFF) {
                record.setFinished(route_next.getDateTime());
                record.setToStopId(route_next.getStopID());
                record.setStatus(route_next.getStopID().equals(route.getStopID())
                        ? ModelChargingRecord.STATUS.CANCELLED : ModelChargingRecord.STATUS.COMPLETED);
                //call service get cost
                record.setChargeAmount(routePlanService.getCompletedRouteCost(record.getFromStopId(), record.getToStopId()));
                index += 1;
            } else {
                record.setFinished(route.getDateTime());
                record.setToStopId(route.getStopID());
                record.setStatus(ModelChargingRecord.STATUS.INCOMPLETE);
                //call service get cost
                record.setChargeAmount(routePlanService.getUncompletedRouteCost(record.getFromStopId()));
            }
            //calculate diffent time
            record.setDurationSecs(Utils.difInSecCalculate(record.getStarted(), record.getFinished()));
            records.add(record);
        }

        //the last record which remains after the above process is a incomplet route
        if (index < datas.size()) {
            ModelRoute route = datas.get(index);
            ModelChargingRecord record = new ModelChargingRecord();
            record.setBusID(route.getIdentity().getBusID());
            record.setCompanyId(route.getIdentity().getCompanyID());
            record.setPAN(route.getIdentity().getPAN());
            record.setFromStopId(route.getStopID());
            record.setStarted(route.getDateTime());
            record.setFinished(route.getDateTime());
            record.setToStopId(route.getStopID());
            record.setStatus(ModelChargingRecord.STATUS.INCOMPLETE);
            record.setDurationSecs(0);
            //call service get cost
            record.setChargeAmount(routePlanService.getUncompletedRouteCost(record.getFromStopId()));
            records.add(record);
        }
        return records;
    }
}

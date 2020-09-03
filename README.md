# LittlePay
----------------
Project overview
----------------
This project is a simple RESTful WS that receive a CSV file in order to aggregate a new CSV file that contains charging detail.
Assumption:	
 - The input file is well format so it will not lost record or data.
 - The header of the input file is not changed. 
 - The "stopID" data will start with "Stop" and it increases continuously, starts with "Stop1".
 - The further stop has a larger ID and the cost also is more expensive (eg: Stop1 to Stop3(cost:7.25) is further than Stop1 to Stop2(cost:5.5)).
 - After a certain time, if the customers forget to "tap off", the next tap will change to "tap on" and record in the database. Or when they change the bus. Hence, all the "tap off" record always has matched "tap on" record which is written before.     

This service will simply receive a CSV file then responses a reference number which is used to download the aggregated CSV file. The receiver will run in async mode to ensure that it can read a large input file.
 RESTful WS link:
 /api/upload : upload csv file and response a reference number.(with RequestParam "file")
 /api/download/{reference}: download the aggregated file.
 
 -- Source structure --
  
    .
    ├── model      
    │   ├── ModelRoute.java            # the model of each route record in input CSV file  
    │   ├── ModelChargingRecord.java   # the model of each charging record in output CSV file  
    │   └── RecordIdentity.java        # class defines identity of each record.
    ├── exception
    │   └── FileNotFoundAdvice.java    # define error message when does not find the file in download      
    ├── services
    │   ├── LittlePayService.java      # interface defines business logic  
    │   ├── LittlePayServiceImpl.java    
    │   ├── RoutePlanService.java      # interface defines methods regarding getting cost based on stopID  
    │   └── RoutePlanServiceImpl.java  
    ├── utilities
    │   ├── CSVRecordHelper.java       # class contains methods to read and write csv file  
    │   └── Utils.java                 # define support methods      
    ├── Application.java               # Main Spring boot Application 
    ├── ResponseMessage.java           # Define response for upload file 
    └── Controller.java                # Main RESful controller

-- Resources structure --
   
    .
    ├── testData.csv                  # test data 
    ├── RoutePlan.csv                 # information of the cost for each route 
    └── application.properties        # propertites of the proejct
    
-- Test packages structure --
   
    .
    ├── services
    │   ├── LittlePayServiceTest.java # testcase for LittlePayService  
    │   └── RoutePlanServiceTest.java # testcase for RoutePlanService 
    ├── utilities
    │   └── CSVRecordHelperTest.java  # testcase for CSVRecordHelper 
    └── ControllerTest.java           # testcase for Application
    
    
-------------------
Develop environment
-------------------
- Language: Java(Spring boot) with Maven
- IDE: Netbeans 11

--------
Testing
--------
Using JUnit
Unit test for each method approached with white box testing to cover all branch

----------
Versioning
----------
Using Git for version control

-------
License
-------
LittlePay

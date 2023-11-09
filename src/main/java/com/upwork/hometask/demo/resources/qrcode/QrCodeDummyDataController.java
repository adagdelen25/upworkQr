package com.upwork.hometask.demo.resources.qrcode;

import com.upwork.hometask.demo.resources.qrcode.model.ScheduleOutput;
import com.upwork.hometask.demo.services.qrCode.DemoDataService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
/*
This controller was written for demo purposes only and to be compatible with dummy data.
It has no function other than that. the real functionality is in the QrCodeController
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api/qrCode/dummy", produces = "application/json")
@Slf4j
@RequiredArgsConstructor
public class QrCodeDummyDataController {
    private final DemoDataService demoDataService;

    @ApiOperation(value = "Create Dummy Data For Testing")
    @PostMapping("/createData")
    public ResponseEntity<Void> createData(HttpServletRequest request) {
        demoDataService.create();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "List Dummy Schedule Programs")
    @GetMapping("/listSchedule")
    public ResponseEntity<ScheduleOutput> getData(HttpServletRequest request) {
        ScheduleOutput resp = demoDataService.getData();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

}

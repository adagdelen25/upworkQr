package com.upwork.hometask.demo.resources.qrcode;

import com.upwork.hometask.demo.resources.qrcode.model.CheckInInput;
import com.upwork.hometask.demo.resources.qrcode.model.CurrentActivityInput;
import com.upwork.hometask.demo.resources.qrcode.model.CurrentActivityOutput;
import com.upwork.hometask.demo.services.qrCode.CheckInService;
import com.upwork.hometask.demo.services.qrCode.CurrentActivityService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/qrCode", produces = "application/json")
@Slf4j
@RequiredArgsConstructor
public class QrCodeController {
    private final CurrentActivityService currentActivityService;
    private final CheckInService checkInService;

    @ApiOperation(value = "List Current Activity")
    @GetMapping("/listCurrentActivity")
    public ResponseEntity<CurrentActivityOutput> listCurrentActivity(HttpServletRequest request, @Valid CurrentActivityInput input) {
        log.info("student : {} , qrcode : {}", input.getStudentId(), input.getQrCode());
        CurrentActivityOutput resp = currentActivityService.listCurrentActivity(input);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @ApiOperation(value = "Check In Current Activity")
    @PutMapping("/checkIn")
    public ResponseEntity<Void> checkIn(HttpServletRequest request, @Valid @RequestBody CheckInInput input) {
        log.info("student : {} , scheduleId - {} , query : {}", input.getStudentId(), input.getScheduleId(), input.getCorrelationID());
        checkInService.checkIn(input);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

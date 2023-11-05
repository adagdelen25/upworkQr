package com.upwork.hometask.demo.resources.iprule;

import com.upwork.hometask.demo.models.dto.IdOutput;
import com.upwork.hometask.demo.resources.iprule.model.InsertInput;
import com.upwork.hometask.demo.resources.iprule.model.InsertSubnetInput;
import com.upwork.hometask.demo.resources.iprule.model.UpdateInput;
import com.upwork.hometask.demo.resources.iprule.model.UpdateSubnetInput;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/iprule/synchronize", produces = "application/json")
@Slf4j
@RequiredArgsConstructor
@ApiOperation(value = "This Controller is empty, If you are going to use a distribution system, try to send request with Synchronize Controller")
public class IpRuleSynchronizeController {

    @ApiOperation(value = "Add IP Filter Rule With Synchronize")
    @PostMapping
    public ResponseEntity<IdOutput> create(HttpServletRequest request, @Valid @RequestBody InsertInput input) {
        //todo If you are going to use a distribution system, send the changes to the center from here.
        return new ResponseEntity<>(new IdOutput(1L), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Add Subnet IP Filter Rule  With Synchronize")
    @PostMapping("/subnet")
    public ResponseEntity<IdOutput> createSubMask(HttpServletRequest request, @Valid @RequestBody InsertSubnetInput input) {
        //todo If you are going to use a distribution system, send the changes to the center from here.
        return new ResponseEntity<>(new IdOutput(1L), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update IP Filter Rule With Synchronize")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(HttpServletRequest request, @Valid @Min(1) @PathVariable Long id, @Valid @RequestBody UpdateInput input) {
        //todo If you are going to use a distribution system, send the changes to the center from here.
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Update Subnet IP Filter Rule  With Synchronize")
    @PutMapping("/subnet/{id}")
    public ResponseEntity<Void> updateSubnet(HttpServletRequest request, @Valid @Min(1) @PathVariable Long id, @Valid @RequestBody UpdateSubnetInput input) {
        //todo If you are going to use a distribution system, send the changes to the center from here.
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Delete IP Filter Rule  With Synchronize")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(HttpServletRequest request, @PathVariable @Valid @Min(1) Long id) {
        //todo If you are going to use a distribution system, send the changes to the center from here.
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

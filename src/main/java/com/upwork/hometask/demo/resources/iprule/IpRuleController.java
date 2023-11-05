package com.upwork.hometask.demo.resources.iprule;

import com.upwork.hometask.demo.models.dto.IdOutput;
import com.upwork.hometask.demo.models.resp.PageableResponse;
import com.upwork.hometask.demo.resources.iprule.model.*;
import com.upwork.hometask.demo.services.iprule.IpRuleCheckService;
import com.upwork.hometask.demo.services.iprule.IpRuleService;
import com.upwork.hometask.demo.services.iprule.SubnetRuleService;
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
@RequestMapping(value = "/api/iprule", produces = "application/json")
@Slf4j
@RequiredArgsConstructor
public class IpRuleController {
    private final IpRuleService ipRuleService;
    private final IpRuleCheckService ipRuleCheckService;
    private final SubnetRuleService subnetRuleService;

    @ApiOperation(value = "Ip Rule List")
    @GetMapping
    public PageableResponse<SearchOutput.SearchOutputData> search(SearchInput input) {
        SearchOutput list = ipRuleService.search(input);
        PageableResponse pageableResponse = new PageableResponse<>(list.getData(), list.getPageNumber(), list.getTotalPages(), list.getTotalCount());
        return pageableResponse;
    }

    @ApiOperation(value = "Add IP Filter Rule")
    @PostMapping
    public ResponseEntity<IdOutput> create(HttpServletRequest request, @Valid @RequestBody InsertInput input) {
        IdOutput resp = ipRuleService.create(input);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Add Subnet IP Filter Rule")
    @PostMapping("/subnet")
    public ResponseEntity<IdOutput> createSubMask(HttpServletRequest request, @Valid @RequestBody InsertSubnetInput input) {
        IdOutput resp = subnetRuleService.create(input);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update IP Filter Rule ")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(HttpServletRequest request, @Valid @Min(1) @PathVariable Long id, @Valid @RequestBody UpdateInput input) {
        ipRuleService.update(id, input);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Update Subnet IP Filter Rule")
    @PutMapping("/subnet/{id}")
    public ResponseEntity<Void> updateSubnet(HttpServletRequest request, @Valid @Min(1) @PathVariable Long id, @Valid @RequestBody UpdateSubnetInput input) {
        subnetRuleService.update(id, input);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Delete IP Filter Rule")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(HttpServletRequest request, @PathVariable @Valid @Min(1) Long id) {
        ipRuleService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Ip Rule List Check")
    @GetMapping("/check")
    public ResponseEntity<Boolean> check(CheckInput input) {
        Boolean check = ipRuleCheckService.executeFromDb(input.getSource(), input.getDestination());
        return new ResponseEntity<>(check, HttpStatus.OK);
    }
}

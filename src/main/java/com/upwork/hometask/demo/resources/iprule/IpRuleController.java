package com.upwork.hometask.demo.resources.iprule;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import com.upwork.hometask.demo.models.dto.IdOutput;
import com.upwork.hometask.demo.models.resp.CreatedResponse;
import com.upwork.hometask.demo.models.resp.NoContentResponse;
import com.upwork.hometask.demo.models.resp.OKResponse;
import com.upwork.hometask.demo.models.resp.PageableResponse;
import com.upwork.hometask.demo.models.resp.VgpResponse;
import com.upwork.hometask.demo.resources.iprule.model.CheckInput;
import com.upwork.hometask.demo.resources.iprule.model.InsertInput;
import com.upwork.hometask.demo.resources.iprule.model.InsertSubnetInput;
import com.upwork.hometask.demo.resources.iprule.model.SearchInput;
import com.upwork.hometask.demo.resources.iprule.model.SearchOutput;
import com.upwork.hometask.demo.resources.iprule.model.UpdateInput;
import com.upwork.hometask.demo.resources.iprule.model.UpdateSubnetInput;
import com.upwork.hometask.demo.services.iprule.IpRuleCheckService;
import com.upwork.hometask.demo.services.iprule.IpRuleService;
import com.upwork.hometask.demo.services.iprule.SubnetRuleService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public VgpResponse<PageableResponse> search(SearchInput input) {
    SearchOutput list = ipRuleService.search(input);
    PageableResponse pageableResponse =
        new PageableResponse<>(
            new PageableResponse.PageableResponseData(
                list.getData(), list.getPageNumber(), list.getPageSize(), list.getTotalCount()));
    return pageableResponse;
  }

  @ApiOperation(value = "Add IP filter rule")
  @PostMapping
  public VgpResponse<IdOutput> create(
      HttpServletRequest request, @Valid @RequestBody InsertInput input) {
    IdOutput resp = ipRuleService.create(request, input);
    return new CreatedResponse<>(resp);
  }

  @ApiOperation(value = "Add Subnet IP filter rule")
  @PostMapping("/subnet")
  public VgpResponse<IdOutput> createSubMask(
      HttpServletRequest request, @Valid @RequestBody InsertSubnetInput input) {
    IdOutput resp = subnetRuleService.create(request, input);
    return new CreatedResponse<>(resp);
  }

  @ApiOperation(value = "Update IP filter rule ")
  @PutMapping("/{id}")
  public NoContentResponse update(
      HttpServletRequest request,
      @Valid @Min(1) @PathVariable Long id,
      @Valid @RequestBody UpdateInput input) {
    ipRuleService.update(request, id, input);
    return new NoContentResponse();
  }

  @ApiOperation(value = "Update Subnet IP filter rule")
  @PutMapping("/subnet/{id}")
  public NoContentResponse updateSubnet(
      HttpServletRequest request,
      @Valid @Min(1) @PathVariable Long id,
      @Valid @RequestBody UpdateSubnetInput input) {
    subnetRuleService.update(request, id, input);
    return new NoContentResponse();
  }

  @ApiOperation(value = "Delete IP filter rule")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(HttpServletRequest request, @PathVariable @Valid @Min(1) Long id) {
    ipRuleService.delete(request, id);
    return new NoContentResponse();
  }

  @ApiOperation(value = "Ip Rule List Check")
  @GetMapping("/check")
  public VgpResponse<Boolean> check(CheckInput input) {
//    return new OKResponse<>(ipRuleCheckService.executeFromCash(input.getSource(), input.getDestination()));
    return new OKResponse<>(ipRuleCheckService.executeFromDb(input.getSource(), input.getDestination()));
  }

}

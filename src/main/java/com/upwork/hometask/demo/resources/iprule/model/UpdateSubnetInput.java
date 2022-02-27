package com.upwork.hometask.demo.resources.iprule.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "com.upwork.hometask.demo.resources.filter.model.UpdateSubnetInput")
public class UpdateSubnetInput {
  private String specificName;
  private String subnetSource;
  private String subnetDestination;
  private Boolean allow;
}

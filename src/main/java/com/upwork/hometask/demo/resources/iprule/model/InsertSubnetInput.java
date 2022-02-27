package com.upwork.hometask.demo.resources.iprule.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "com.upwork.hometask.demo.resources.filter.model.InsertSubnetInput")
public class InsertSubnetInput {
  private String specificName;
  private String subnetSource;
  private String subnetDestination;
  private Boolean allow;
}

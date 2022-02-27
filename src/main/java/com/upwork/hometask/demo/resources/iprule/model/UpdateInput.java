package com.upwork.hometask.demo.resources.iprule.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "com.upwork.hometask.demo.resources.filter.model.UpdateInput")
public class UpdateInput {
  private String specificName;

  @NotNull
  @Size(min = 1, max = 15)
  private String sourceStart;

  @NotNull
  @Size(min = 1, max = 15)
  private String sourceEnd;

  @NotNull
  @Size(min = 1, max = 15)
  private String destinationStart;

  @NotNull
  @Size(min = 1, max = 15)
  private String destinationEnd;

  private Boolean allow;
}

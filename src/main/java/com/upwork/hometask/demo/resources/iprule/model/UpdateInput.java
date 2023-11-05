package com.upwork.hometask.demo.resources.iprule.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.upwork.hometask.demo.utils.PatternValidator;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "com.upwork.hometask.demo.resources.filter.model.UpdateInput")
public class UpdateInput {
  private String specificName;

  @NotEmpty
  @Pattern(regexp = PatternValidator.IP_PATTERN, message = PatternValidator.IP_PATTERN_MESSAGE)
  private String sourceStart;

  @NotEmpty
  @Pattern(regexp = PatternValidator.IP_PATTERN, message = PatternValidator.IP_PATTERN_MESSAGE)
  private String sourceEnd;

  @NotEmpty
  @Pattern(regexp = PatternValidator.IP_PATTERN, message = PatternValidator.IP_PATTERN_MESSAGE)
  private String destinationStart;

  @NotEmpty
  @Pattern(regexp = PatternValidator.IP_PATTERN, message = PatternValidator.IP_PATTERN_MESSAGE)
  private String destinationEnd;

  private Boolean allow;
}

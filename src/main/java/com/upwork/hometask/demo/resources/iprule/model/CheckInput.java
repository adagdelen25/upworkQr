package com.upwork.hometask.demo.resources.iprule.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.upwork.hometask.demo.utils.PatternValidator;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "com.upwork.hometask.demo.resources.filter.model.CheckInput")
public class CheckInput {

  @NotEmpty
  @Pattern(regexp = PatternValidator.IP_PATTERN, message = PatternValidator.IP_PATTERN_MESSAGE)
  private String source;

  @NotEmpty
  @Pattern(regexp = PatternValidator.IP_PATTERN, message = PatternValidator.IP_PATTERN_MESSAGE)
  private String destination;

}

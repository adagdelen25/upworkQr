package com.upwork.hometask.demo.resources.iprule.model;

import com.upwork.hometask.demo.models.req.PageableInput;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "com.upwork.hometask.demo.resources.filter.model.SearchInput")
public class SearchInput extends PageableInput {
  private String name;
}

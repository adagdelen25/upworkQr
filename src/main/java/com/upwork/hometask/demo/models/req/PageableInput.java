package com.upwork.hometask.demo.models.req;

import lombok.Data;

@Data
public class PageableInput {
  private Integer pageNumber = 0;
  private Integer pageSize = 10;
  private String sortBy = "id";
  private String sortDirection = "asc";
  private Boolean unPaged = false;
}

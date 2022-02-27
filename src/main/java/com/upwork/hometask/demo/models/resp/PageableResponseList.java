package com.upwork.hometask.demo.models.resp;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class PageableResponseList<T> extends OKResponse<PageableResponseList.PageableResponseData> {
  public PageableResponseList(PageableResponseData data) {
    super(data);
  }

  @Getter
  @AllArgsConstructor
  public static class PageableResponseData<T> {
    private List<T> items;
    private T total;
    private int pageNumber = 0;
    private int pageSize = 10;
    private long totalCount = 0;
  }
}

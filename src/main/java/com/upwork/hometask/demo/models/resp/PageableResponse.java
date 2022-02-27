package com.upwork.hometask.demo.models.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class PageableResponse<T> extends OKResponse<PageableResponse.PageableResponseData> {
  public PageableResponse(PageableResponseData data) {
    super(data);
  }

  @Getter
  @AllArgsConstructor
  public static class PageableResponseData<T> {
    private T data;
    private int pageNumber = 0;
    private int pageSize = 10;
    private long totalCount = 0;
  }
}

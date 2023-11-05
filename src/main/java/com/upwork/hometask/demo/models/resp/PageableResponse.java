package com.upwork.hometask.demo.models.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageableResponse<T> {

    private T data;
    private int pageNumber = 0;
    private int totalPages = 0;
    private long totalCount = 0;
}
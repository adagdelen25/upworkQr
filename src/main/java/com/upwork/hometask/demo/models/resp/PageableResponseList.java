package com.upwork.hometask.demo.models.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageableResponseList<T> {
    private List<T> items;
    private int pageNumber = 0;
    private int pageSize = 10;
    private long totalCount = 0;
}

package com.upwork.hometask.demo.utils;

import com.upwork.hometask.demo.models.req.PageableInput;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtils {
  public static Pageable getPageable(PageableInput pageDto) {
    if (StringUtils.isNotEmpty(pageDto.getSortBy())) {
      Sort sort = null;
      if (StringUtils.equalsIgnoreCase(pageDto.getSortDirection(), "desc")) {
        sort = Sort.by(Sort.Direction.DESC, pageDto.getSortBy());
      } else {
        sort = Sort.by(Sort.Direction.ASC, pageDto.getSortBy());
      }
      Pageable paging = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
      return paging;
    } else {
      Pageable paging = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize());
      return paging;
    }
  }

  public static Pageable getPageable(PageableInput pageDto, String orderColumn) {
    if (StringUtils.isNotEmpty(pageDto.getSortBy())) {
      Sort sort = null;
      if (StringUtils.equalsIgnoreCase(pageDto.getSortDirection(), "desc")) {
        sort = Sort.by(Sort.Direction.DESC, orderColumn);
      } else {
        sort = Sort.by(Sort.Direction.ASC, orderColumn);
      }
      Pageable paging = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
      return paging;
    } else {
      Pageable paging = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize());
      return paging;
    }
  }
}

package com.upwork.hometask.demo.resources.iprule.model;

import com.upwork.hometask.demo.models.resp.PageableResponse;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel(value = "com.upwork.hometask.demo.resources.filter.model.SearchOutput")
public class SearchOutput<T extends List<SearchOutput.SearchOutputData>> extends PageableResponse<T> {

    public SearchOutput(T data, Integer pageNumber, Integer pageSize, Long totalCount) {
        super(data, pageNumber, pageSize, totalCount);
    }

    @Data
    @ApiModel(
            value = "com.upwork.hometask.demo.resources.filter.model.SearchOutput.SearchOutputData")
    public static class SearchOutputData {
        private Long id;
        private String specificName;

        private String sourceStart;
        private Long sourceStartValue;
        private String sourceEnd;
        private Long sourceEndValue;

        private String destinationStart;
        private Long destinationStartValue;
        private String destinationEnd;
        private Long destinationEndValue;

        private Boolean allow;
        private Boolean isSubnet = false;
        private String subnetSource;
        private String subnetDestination;
    }
}

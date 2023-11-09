package com.upwork.hometask.demo.resources.qrcode.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel(value = "com.upwork.hometask.demo.resources.qrcode.model.CheckInInput")
public class CheckInInput {

    @NotNull
    @Min(1)
    private Long studentId;

    @NotNull
    @Min(1)
    private Long scheduleId;

    /*
    this verificationCode created by mobile application unique student
     */
    @NotNull
    @Min(1)
    private Integer verificationCode;

    @NotEmpty
    private String correlationID;

    private Double latitude;

    private Double longitude;
}

package com.upwork.hometask.demo.resources.qrcode.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@ApiModel(value = "com.upwork.hometask.demo.resources.qrcode.model.CurrentActivityInput")
public class CurrentActivityInput {
    @NotEmpty
    private String qrCode;

    @NotNull
    @Min(1)
    private Long studentId;
}

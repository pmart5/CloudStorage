package com.pmart5a.cloudstorage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessageResponse {

    private String message;
    private Integer id;
}
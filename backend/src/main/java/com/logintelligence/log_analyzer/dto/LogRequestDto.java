package com.logintelligence.log_analyzer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LogRequestDto {

    private String serviceName;
    private String logLevel;
    private String message;
    private String stackTrace;
    private String host;
}
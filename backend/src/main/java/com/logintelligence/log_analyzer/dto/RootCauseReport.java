package com.logintelligence.log_analyzer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RootCauseReport {

    private String groupId;
    private int frequency;
    private LocalDateTime latestOccurrence;
    private String sampleMessage;

    public RootCauseReport(String groupId,
                           int frequency,
                           LocalDateTime latestOccurrence,
                           String sampleMessage)
    {
        this.groupId = groupId;
        this.frequency = frequency;
        this.latestOccurrence = latestOccurrence;
        this.sampleMessage = sampleMessage;
    }

}

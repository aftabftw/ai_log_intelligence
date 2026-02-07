package com.logintelligence.log_analyzer.controller;

import com.logintelligence.log_analyzer.dto.LogRequestDto;
import com.logintelligence.log_analyzer.dto.RootCauseReport;
import com.logintelligence.log_analyzer.model.ApplicationLog;
import com.logintelligence.log_analyzer.service.LogService;
import com.logintelligence.log_analyzer.service.analysis.AnomalyDetectionService;
import com.logintelligence.log_analyzer.service.analysis.LogSimilarityService;
import com.logintelligence.log_analyzer.service.analysis.RootCauseRankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;
    private final AnomalyDetectionService anomalyDetectionService;
    private final LogSimilarityService logSimilarityService;
    private final RootCauseRankingService rootCauseRankingService;

    public LogController(LogService logService,
                         AnomalyDetectionService anomalyDetectionService,
                         LogSimilarityService logSimilarityService,
                         RootCauseRankingService rootCauseRankingService)
    {
        this.logService = logService;
        this.anomalyDetectionService = anomalyDetectionService;
        this.logSimilarityService = logSimilarityService;
        this.rootCauseRankingService = rootCauseRankingService;
    }

    @PostMapping
    public ResponseEntity<String> ingestLog(
            @RequestBody LogRequestDto dto) {

        logService.saveLog(dto);
        return ResponseEntity.ok("Log ingested");
    }

    @GetMapping
    public List<ApplicationLog> getAll() {
        return logService.getAllLogs();
    }

    @GetMapping("/errors")
    public List<ApplicationLog> getErrors() {
        return logService.getErrorLogs();
    }

    @GetMapping("/anomaly/{serviceName}")
    public ResponseEntity<String> checkAnomaly(
            @PathVariable String serviceName) {

        boolean spike = anomalyDetectionService.isErrorSpike(serviceName);

        if (spike) {
            return ResponseEntity.ok("⚠️ ERROR SPIKE DETECTED");
        }
        return ResponseEntity.ok("Normal behavior");
    }

    @GetMapping("/similarity")
    public ResponseEntity<Map<String, List<ApplicationLog>>> groupErrors(
            @RequestParam(defaultValue = "0.4") double threshold
    ) {
        return ResponseEntity.ok(
                logSimilarityService.groupSimilarErrors(threshold)
        );
    }

    @GetMapping("/root-causes")
    public List<RootCauseReport> getRootCauses() {
        return rootCauseRankingService.generateReport();
    }

    @GetMapping("/explain/{groupId}")
    public String explainErrorGroup(@PathVariable String groupId) {
        return rootCauseRankingService.explainGroup(groupId);
    }



}

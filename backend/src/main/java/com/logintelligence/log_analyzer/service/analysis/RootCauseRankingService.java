package com.logintelligence.log_analyzer.service.analysis;

import com.logintelligence.log_analyzer.dto.RootCauseReport;
import com.logintelligence.log_analyzer.model.ApplicationLog;
import com.logintelligence.log_analyzer.service.ai.LocalAiExplanationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RootCauseRankingService {

    private final LogSimilarityService similarityService;
    private final LocalAiExplanationService aiService;
    private final Map<String, String> explanationCache = new HashMap<>();



    public RootCauseRankingService(LogSimilarityService similarityService,  LocalAiExplanationService aiService) {
        this.similarityService = similarityService;
        this.aiService = aiService;
    }

    public List<RootCauseReport> generateReport() {

        Map<String, List<ApplicationLog>> groups =
                similarityService.groupSimilarErrors(0.4);

        List<RootCauseReport> reports = new ArrayList<>();

        for (Map.Entry<String, List<ApplicationLog>> entry : groups.entrySet()) {

            String groupId = entry.getKey();
            List<ApplicationLog> logs = entry.getValue();

            int frequency = logs.size();

            LocalDateTime latest =
                    logs.stream()
                            .map(ApplicationLog::getTimestamp)
                            .max(LocalDateTime::compareTo)
                            .orElse(null);

            String sampleMessage = logs.get(0).getMessage();

            reports.add(new RootCauseReport(
                    groupId,
                    frequency,
                    latest,
                    sampleMessage
            ));
        }

        // Sort by frequency DESC
        reports.sort((a, b) ->
                Integer.compare(b.getFrequency(), a.getFrequency())
        );

        return reports;
    }
    public String explainGroup(String groupId) {

        if (explanationCache.containsKey(groupId)) {
            return explanationCache.get(groupId);
        }

        Map<String, List<ApplicationLog>> groups =
                similarityService.groupSimilarErrors(0.4);

        List<ApplicationLog> logs = groups.get(groupId);

        if (logs == null || logs.isEmpty()) {
            return "Group not found";
        }

        String sampleError = logs.get(0).getMessage();

        String explanation = aiService.explainError(sampleError);

        explanationCache.put(groupId, explanation);

        return explanation;
    }


}

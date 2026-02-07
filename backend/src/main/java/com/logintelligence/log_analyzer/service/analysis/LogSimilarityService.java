package com.logintelligence.log_analyzer.service.analysis;

import com.logintelligence.log_analyzer.model.ApplicationLog;
import com.logintelligence.log_analyzer.repository.ApplicationLogRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LogSimilarityService {

    private final ApplicationLogRepository repository;

    public LogSimilarityService(ApplicationLogRepository repository) {
        this.repository = repository;
    }

    private String normalize(String text) {
        return text
                .toLowerCase()
                .replaceAll("[^a-z ]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private double similarityScore(String a, String b) {
        Set<String> wordsA = new HashSet<>(List.of(a.split(" ")));
        Set<String> wordsB = new HashSet<>(List.of(b.split(" ")));

        Set<String> intersection = new HashSet<>(wordsA);
        intersection.retainAll(wordsB);

        Set<String> union = new HashSet<>(wordsA);
        union.addAll(wordsB);

        if (union.isEmpty()) return 0;
        return (double) intersection.size() / union.size();
    }

    public Map<String, List<ApplicationLog>> groupSimilarErrors(
            double threshold
    ) {
        List<ApplicationLog> errorLogs =
                repository.findByLogLevel("ERROR");

        Map<String, List<ApplicationLog>> groups = new LinkedHashMap<>();
        Set<Long> used = new HashSet<>();

        for (ApplicationLog base : errorLogs) {
            if (used.contains(base.getId())) continue;

            String baseText = normalize(
                    base.getMessage() + " " + base.getStackTrace()
            );

            List<ApplicationLog> group = new ArrayList<>();
            group.add(base);
            used.add(base.getId());

            for (ApplicationLog candidate : errorLogs) {
                if (used.contains(candidate.getId())) continue;

                String candidateText = normalize(
                        candidate.getMessage() + " " + candidate.getStackTrace()
                );

                double score =
                        similarityScore(baseText, candidateText);

                if (score >= threshold) {
                    group.add(candidate);
                    used.add(candidate.getId());
                }
            }

            groups.put("group-" + base.getId(), group);
        }

        return groups;
    }


}

package com.logintelligence.log_analyzer.service.analysis;

import com.logintelligence.log_analyzer.repository.ApplicationLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AnomalyDetectionService {

    private final ApplicationLogRepository repository;

    public AnomalyDetectionService(ApplicationLogRepository repository) {
        this.repository = repository;
    }

    public boolean isErrorSpike(String serviceName) {
        int windowMinutes = 1;
        int baselineWindows = 5;
        double thresholdMultiplier = 3.0;

        LocalDateTime now = LocalDateTime.now();

        // Current window
        long currentCount = repository.countErrorsInWindow(
                serviceName,
                now.minusMinutes(windowMinutes),
                now
        );

        // Baseline average
        long total = 0;
        for (int i = 1; i <= baselineWindows; i++) {
            LocalDateTime end = now.minusMinutes(i);
            LocalDateTime start = end.minusMinutes(windowMinutes);

            total += repository.countErrorsInWindow(
                    serviceName, start, end
            );
        }

        double average = total / (double) baselineWindows;

        return average > 0 && currentCount > average * thresholdMultiplier;
    }
}


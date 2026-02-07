package com.logintelligence.log_analyzer.service;
import com.logintelligence.log_analyzer.dto.LogRequestDto;
import com.logintelligence.log_analyzer.model.ApplicationLog;
import com.logintelligence.log_analyzer.repository.ApplicationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final ApplicationLogRepository repository;

    public LogService(ApplicationLogRepository repository) {
        this.repository = repository;
    }

    public void saveLog(LogRequestDto dto) {
        ApplicationLog log = new ApplicationLog();
        log.setServiceName(dto.getServiceName());
        log.setLogLevel(dto.getLogLevel());
        log.setMessage(dto.getMessage());
        log.setStackTrace(dto.getStackTrace());
        log.setHost(dto.getHost());
        log.setTimestamp(LocalDateTime.now());
        ApplicationLog savedLog = repository.save(log);
        messagingTemplate.convertAndSend("/topic/log-updates", "new-log");
        System.out.println("WEBSOCKET EVENT SEEN");
    }

    public List<ApplicationLog> getAllLogs() {
        return repository.findAll();
    }

    public List<ApplicationLog> getErrorLogs() {
        return repository.findByLogLevel("ERROR");
    }
}

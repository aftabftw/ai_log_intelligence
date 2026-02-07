package com.logintelligence.log_analyzer.repository;
import com.logintelligence.log_analyzer.model.ApplicationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ApplicationLogRepository extends JpaRepository<ApplicationLog,Long> {

    List<ApplicationLog> findByLogLevel(String logLevel);
    List<ApplicationLog> findByServiceName(String serviceName);
    @Query("""
        SELECT COUNT(l)
        FROM ApplicationLog l
        WHERE l.logLevel = 'ERROR'
        AND l.serviceName = :serviceName
        AND l.timestamp BETWEEN :start AND :end
    """)
    long countErrorsInWindow(
            @Param("serviceName") String serviceName,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}

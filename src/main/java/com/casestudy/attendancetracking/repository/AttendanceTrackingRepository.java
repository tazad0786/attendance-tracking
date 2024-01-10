package com.casestudy.attendancetracking.repository;

import com.casestudy.attendancetracking.domain.AttendanceTrackingEvent;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Repository
public interface AttendanceTrackingRepository extends ReactiveMongoRepository<AttendanceTrackingEvent, String> {

    Flux<AttendanceTrackingEvent> findByDate(LocalDate date);
    Flux<AttendanceTrackingEvent> findByEmployeeIdAndDateBetween(String employeeId, LocalDate startDate, LocalDate endDate);

}


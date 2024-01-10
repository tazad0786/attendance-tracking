package com.casestudy.attendancetracking.service;

import com.casestudy.attendancetracking.domain.AttendanceTrackingEvent;
import com.casestudy.attendancetracking.repository.AttendanceTrackingRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class MongoEventSaver {
    private final AttendanceTrackingRepository attendanceRepository;
    public Mono<AttendanceTrackingEvent> saveEventToMongo(AttendanceTrackingEvent attendanceEvent) {
        return  attendanceRepository.save(attendanceEvent);
    }
}
package com.casestudy.attendancetracking.service;

import com.casestudy.attendancetracking.domain.AttendanceTrackingEvent;
import com.casestudy.attendancetracking.domain.SwipeType;
import com.casestudy.attendencecommon.domain.SwipeEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AttendanceTrackingService {
    Mono<AttendanceTrackingEvent> addSwipe(SwipeEvent swipeEvent);
    void calculateDailyAttendanceAndStream();
}
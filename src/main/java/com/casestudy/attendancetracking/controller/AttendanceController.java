package com.casestudy.attendancetracking.controller;

import com.casestudy.attendancetracking.domain.AttendanceTrackingEvent;
import com.casestudy.attendancetracking.service.AttendanceTrackingService;
import com.casestudy.attendencecommon.domain.SwipeEvent;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@Validated
public class AttendanceController {
    private AttendanceTrackingService attendanceService;

    public AttendanceController(AttendanceTrackingService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/attendance/swipe")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<AttendanceTrackingEvent> swipe(@RequestBody SwipeEvent request) {
        return attendanceService.addSwipe(request);
    }

    @PostMapping("/attendance/calculate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void calculate() {
        attendanceService.calculateDailyAttendanceAndStream();
    }

}

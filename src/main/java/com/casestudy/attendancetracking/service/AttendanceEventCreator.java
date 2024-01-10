package com.casestudy.attendancetracking.service;

import com.casestudy.attendancetracking.domain.AttendanceTrackingEvent;
import com.casestudy.attendencecommon.domain.SwipeEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AttendanceEventCreator {
    public static AttendanceTrackingEvent createAttendanceEvent(SwipeEvent swipeEvent) {
        AttendanceTrackingEvent attendanceEvent = new AttendanceTrackingEvent();
        attendanceEvent.setEmployeeId(swipeEvent.getEmployeeId().intValue());
        attendanceEvent.setEmployeeName(swipeEvent.getEmployeeName());
        attendanceEvent.setOfficeLocation(swipeEvent.getOfficeLocation());
        attendanceEvent.setSwipeType(swipeEvent.getSwipeType());
        attendanceEvent.setSwipeTime(swipeEvent.getTimestamp());
        attendanceEvent.setDate(LocalDate.now());
        attendanceEvent.generateId();
        return attendanceEvent;
    }
}
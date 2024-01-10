package com.casestudy.attendancetracking.service;

import com.casestudy.attendancetracking.domain.AttendanceTrackingEvent;
import com.casestudy.attendencecommon.domain.AttendanceStatus;
import com.casestudy.attendencecommon.domain.DailyAttendanceEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class DailyAttendanceCalculator {
    public DailyAttendanceEvent calculateDailyAttendance(AttendanceTrackingEvent firstSwipeIn, AttendanceTrackingEvent lastSwipeOut) {
        LocalDateTime totalHours = getTotalHours(lastSwipeOut, firstSwipeIn);
        return new DailyAttendanceEvent(
                lastSwipeOut.getEmployeeId(),
                lastSwipeOut.getEmployeeName(),
                LocalDate.now(),
                firstSwipeIn.getSwipeTime(),
                lastSwipeOut.getSwipeTime(),
                totalHours,
                getAttendanceStatus(totalHours),
                lastSwipeOut.getOfficeLocation()
        );
    }

    private static LocalDateTime getTotalHours(AttendanceTrackingEvent lastSwipeOut, AttendanceTrackingEvent firstSwipeIn) {
        return lastSwipeOut.getSwipeTime().minusHours(firstSwipeIn.getSwipeTime().getHour()).minusMinutes(firstSwipeIn.getSwipeTime().getMinute());
    }

    private static AttendanceStatus getAttendanceStatus(LocalDateTime totalHours) {
        if (totalHours.isBefore(LocalDateTime.of(LocalDate.now(), LocalTime.of(4, 0)))) {
            return AttendanceStatus.ABSENT;
        } else if (totalHours.isBefore(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0)))) {
            return AttendanceStatus.HALF_DAY;
        } else {
            return AttendanceStatus.PRESENT;
        }
    }
}
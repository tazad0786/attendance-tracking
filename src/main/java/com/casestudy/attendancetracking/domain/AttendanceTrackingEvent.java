package com.casestudy.attendancetracking.domain;

import com.casestudy.attendencecommon.domain.SwipeType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "attendance_tracking")
public class AttendanceTrackingEvent {

    @Id
    private String eventId;
    private Integer employeeId;
    private String employeeName;
    private String officeLocation;
    private LocalDateTime swipeTime;
    private SwipeType swipeType;
    private LocalDate date;
    public void generateId() {
        this.eventId = UUID.randomUUID().toString();
    }
}
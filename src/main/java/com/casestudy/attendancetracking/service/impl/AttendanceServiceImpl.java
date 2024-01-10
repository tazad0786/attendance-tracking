package com.casestudy.attendancetracking.service.impl;

import com.casestudy.attendancetracking.domain.AttendanceTrackingEvent;
import com.casestudy.attendancetracking.domain.SwipeType;
import com.casestudy.attendancetracking.repository.AttendanceTrackingRepository;
import com.casestudy.attendancetracking.service.*;
import com.casestudy.attendencecommon.domain.SwipeEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;

@Service
@Slf4j
@EnableKafka
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceTrackingService {

    private final AttendanceTrackingRepository attendanceRepository;
    private final KafkaTemplate<Integer, String> kafkaTemplate;
    private final AttendanceEventCreator eventCreator;
    private final KafkaEventPublisher eventPublisher;
    private final DailyAttendanceCalculator attendanceCalculator;
    private final ObjectMapper objectMapper;
    private final MongoEventSaver mongoEventSaver;
    //@Value("${daily-attendance}")
    String dailyTopic="daily_attendance_topic";
    @Override
    public Mono<AttendanceTrackingEvent> addSwipe(SwipeEvent swipeEvent) {
        AttendanceTrackingEvent attendanceTrackingEvent = eventCreator.createAttendanceEvent(swipeEvent);
        return eventPublisher.sendEventToKafka(swipeEvent.getEmployeeId(), attendanceTrackingEvent,objectMapper);
    }

    @Scheduled(cron = "0 * * * * *")
    public void calculateDailyAttendanceAndStream() {
        log.info("Starting cron job to calculate daily attendance and send to {} kafka topic.", "daily-attendance-topic");

        attendanceRepository.findByDate(LocalDate.now())
                .groupBy(AttendanceTrackingEvent::getEmployeeId)
                .flatMap(groupedFlux -> groupedFlux
                        .collectList()
                        .flatMap(list -> {
                            Optional<AttendanceTrackingEvent> firstSwipeIn = list.stream()
                                    .filter(attendanceEvent -> attendanceEvent.getSwipeType().equals(SwipeType.SWIPE_IN))
                                    .min(Comparator.comparing(AttendanceTrackingEvent::getSwipeTime));

                            Optional<AttendanceTrackingEvent> lastSwipeOut = list.stream()
                                    .filter(attendanceEvent -> attendanceEvent.getSwipeType().equals(SwipeType.SWIPE_OUT))
                                    .sorted(Comparator.comparing(AttendanceTrackingEvent::getSwipeTime))
                                    .reduce((prev, next) -> next);

                            return firstSwipeIn.isPresent() && lastSwipeOut.isPresent() ?
                                    Mono.just(attendanceCalculator.calculateDailyAttendance(firstSwipeIn.get(), lastSwipeOut.get())) :
                                    Mono.empty();
                        }))
                .flatMap(dailyAttendanceEvent -> {
                    try {
                        kafkaTemplate.send(dailyTopic, dailyAttendanceEvent.getEmployeeId(), dailyAttendanceEvent.toString());
                        return Mono.just(dailyAttendanceEvent);
                    } catch (Exception exception) {
                        log.error("Error while serializing DailyAttendance event object: ", exception);
                        return Mono.empty();
                    }
                })
                .doOnError(error -> log.error("Error processing daily attendance and streaming to Kafka", error))
                .subscribe();
    }

}

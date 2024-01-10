package com.casestudy.attendancetracking.service;

import com.casestudy.attendancetracking.domain.AttendanceTrackingEvent;
import com.casestudy.attendancetracking.repository.AttendanceTrackingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SwipeEventSaveListener {

    private final AttendanceTrackingRepository attendanceRepository;
    private final ObjectMapper objectMapper;
    private final MongoEventSaver mongoEventSaver;

    @KafkaListener(topics = "daily_swipe_topic",groupId = "tracking-consumer-group")
    public void consume(ConsumerRecord<Integer, String> swipeRecord) {
        log.debug("Received SwipeRecord with Key --> {}, Value --> {}", swipeRecord.key(), swipeRecord.value());

        try {
            AttendanceTrackingEvent attendanceEvent = objectMapper.readValue(swipeRecord.value(), AttendanceTrackingEvent.class);

            mongoEventSaver.saveEventToMongo(attendanceEvent)
                    .doOnSuccess(saved -> log.info("Successfully saved AttendanceEvent with id --> {}", saved.getEventId()))
                    .subscribe();
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON for AttendanceEvent", e);
        }
    }
}

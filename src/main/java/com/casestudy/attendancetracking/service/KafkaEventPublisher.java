package com.casestudy.attendancetracking.service;

import com.casestudy.attendancetracking.domain.AttendanceTrackingEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class KafkaEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
   // @Value("${daily-swipe}")
    String swipeTopic = "daily_swipe_topic";

    public KafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public Mono<AttendanceTrackingEvent> sendEventToKafka(long employeeId, AttendanceTrackingEvent event, ObjectMapper objectMapper) {
        try {
            kafkaTemplate.send(swipeTopic,derivePartition(employeeId,3),event.getEmployeeId().toString(), objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException exception) {
            log.error("Error while serializing Attendance object: ", exception);
            return Mono.error(new RuntimeException("Error while serializing the SwipeEvent: " + exception.getMessage()));
        } catch (Exception exception) {
            return Mono.error(new RuntimeException("Error while saving the SwipeEvent: " + exception.getMessage()));
        }
        return Mono.justOrEmpty(event);
    }

    private static Integer derivePartition(long id, int numPartitions) {
        if (numPartitions <= 0) {
            throw new IllegalArgumentException("Number of partitions must be greater than 0");
        }
        return Math.floorMod(id, numPartitions);
    }
}
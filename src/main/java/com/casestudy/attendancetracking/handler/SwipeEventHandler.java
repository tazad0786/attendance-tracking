package com.casestudy.attendancetracking.handler;

import com.casestudy.attendancetracking.service.AttendanceTrackingService;
import com.casestudy.attendencecommon.domain.SwipeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SwipeEventHandler {

    private final AttendanceTrackingService swipeEventService;

    public Mono<ServerResponse> addSwipe(ServerRequest request) {
        return request.bodyToMono(SwipeEvent.class)
                .flatMap(event -> swipeEventService.addSwipe(event))
                .flatMap(event -> ServerResponse.status(HttpStatus.CREATED).bodyValue(event));
    }
}

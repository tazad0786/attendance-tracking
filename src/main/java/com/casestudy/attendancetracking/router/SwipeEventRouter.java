package com.casestudy.attendancetracking.router;

import com.casestudy.attendancetracking.handler.SwipeEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class SwipeEventRouter {

    @Bean
    public RouterFunction<ServerResponse> swipeRoute(SwipeEventHandler swipeEventHandler) {
        return route(POST("/api/v1/swipe"), swipeEventHandler::addSwipe);
    }
}

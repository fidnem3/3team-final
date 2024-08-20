package com.javalab.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.javalab.board.service.ApplicationService;

import java.io.IOException;

@RestController
public class NotificationController {

    private final ApplicationService applicationService;

    @Autowired
    public NotificationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/notifications")
    public SseEmitter streamNotifications() {
        SseEmitter emitter = new SseEmitter();

        applicationService.registerEmitter(emitter);

        return emitter;
    }
}

package com.samrat.simple_messenger.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/sse")
public class SseController {

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    @GetMapping("/stream")
    public SseEmitter streamSseEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        executorService.execute(() -> {
            while (true) {
                try {
                    String formattedTime = LocalDateTime.now()
                            .atZone(ZoneId.of("Asia/Dhaka"))
                            .format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                    Thread.sleep(10_000L);
                    emitter.send("SSE TimeEvent-" + formattedTime);
//                    emitter.complete(); //will end the sse emitter
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    emitter.completeWithError(e);
                }
            }
        });
        return emitter;
    }

}

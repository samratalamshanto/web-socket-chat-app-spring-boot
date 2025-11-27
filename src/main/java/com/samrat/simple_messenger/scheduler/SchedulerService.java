package com.samrat.simple_messenger.scheduler;

import com.samrat.simple_messenger.dto.ChatMessage;
import com.samrat.simple_messenger.enums.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {
    private final SimpMessageSendingOperations messageTemplate;

    @Scheduled(fixedRate = 10000)
    public void sendMessage() {
        String formattedTime = LocalDateTime.now()
                .atZone(ZoneId.of("Asia/Dhaka"))
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        log.info("Sending a message to scheduler. Time={}", formattedTime);
        var chatMessage = ChatMessage.builder()
                .type(MessageType.TIME)
                .content("Cur Time(GMT+6): " + formattedTime)
                .build();
        messageTemplate.convertAndSend("/topic/chat", chatMessage);
    }
}

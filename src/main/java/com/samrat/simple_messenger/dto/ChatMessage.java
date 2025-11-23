package com.samrat.simple_messenger.dto;

import com.samrat.simple_messenger.enums.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;

    @Builder.Default
    private LocalDateTime createdDt =  LocalDateTime.now();
}

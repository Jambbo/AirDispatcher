package com.example.ship.listener.processor;

import com.example.common.messages.BoardStateMessage;
import com.example.common.messages.OfficeStateMessage;
import com.example.common.processor.MessageConverter;
import com.example.common.processor.MessageProcessor;
import com.example.ship.provider.BoardProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
//here office asks for info about the state of all planes(boards)
@Slf4j
@Component("OFFICE_STATE")
@RequiredArgsConstructor
public class OfficeStateProcessor implements MessageProcessor<OfficeStateMessage> {
    private final MessageConverter converter;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final BoardProvider boardProvider;
    @Override
    public void process(String jsonMessage) {
        boardProvider.getBoards().forEach(board -> {
            kafkaTemplate.sendDefault(converter.toJson(new BoardStateMessage(board)));
        });
    }
}

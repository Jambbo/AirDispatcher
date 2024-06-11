package com.example.ship.listener;

import com.example.common.messages.Message;
import com.example.common.processor.MessageConverter;
import com.example.common.processor.MessageProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessagesListener {

    private final MessageConverter converter;

    @Autowired
    private Map<String, MessageProcessor<? extends Message>> processors = new HashMap<>();

    @KafkaListener(id="BoardId", topics = "officeRoutes")
    public void radarListener(String data){
        String code = converter.extractCode(data);
        try {
            processors.get(code).process(data);
        } catch (Exception e) {
           log.error("Code: "+code+". "+e.getLocalizedMessage());
        }

    }

}

package com.example.office.listener;

import com.example.common.messages.Message;
import com.example.common.processor.MessageConverter;
import com.example.common.processor.MessageProcessor;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {

    private final MessageConverter messageConverter;
    private final Cache<String, WebSocketSession> sessionCache;

    @Autowired
    private Map<String, MessageProcessor<? extends Message>> processors = new HashMap<>();

    @KafkaListener(id = "OfficeGroupId", topics = "officeRoutes")
    public void kafkaListen(String data) {
        String code = messageConverter.extractCode(data);
        sendKafkaMessageToSocket(data);
        try {
            processors.get(code).process(data);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    private void sendKafkaMessageToSocket(String data) {
        //asMap() - converts to the regular Java map. values() - map's method that iterates on each
        // map's value, in this case it is WebSocketSession objects.
        sessionCache.asMap().values().forEach(webSocketSession -> {
            if (webSocketSession.isOpen()) {
                try {
                    webSocketSession.sendMessage(new TextMessage(data));
                } catch (IOException e) {
                    log.error(e.getLocalizedMessage());
                }
            }
        });
    }

}

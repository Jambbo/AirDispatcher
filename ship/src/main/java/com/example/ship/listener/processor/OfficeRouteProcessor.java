package com.example.ship.listener.processor;

import com.example.common.messages.OfficeRouteMessage;
import com.example.common.processor.MessageProcessor;
import org.springframework.stereotype.Component;

@Component("OFFICE_ROUTE")
public class OfficeRouteProcessor implements MessageProcessor<OfficeRouteMessage> {
    @Override
    public void process(String jsonMessage) {

    }
}

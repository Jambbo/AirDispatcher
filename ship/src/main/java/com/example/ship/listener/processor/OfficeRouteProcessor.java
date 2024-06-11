package com.example.ship.listener.processor;

import com.example.common.bean.Board;
import com.example.common.bean.Route;
import com.example.common.messages.OfficeRouteMessage;
import com.example.common.processor.MessageConverter;
import com.example.common.processor.MessageProcessor;
import com.example.ship.provider.BoardProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component("OFFICE_ROUTE")
@RequiredArgsConstructor
public class OfficeRouteProcessor implements MessageProcessor<OfficeRouteMessage> {

    private final BoardProvider boardProvider;
    private final MessageConverter converter;

    @Override
    public void process(String jsonMessage) {
        OfficeRouteMessage officeRouteMessage = converter.extractMessage(jsonMessage, OfficeRouteMessage.class);
        Route route = officeRouteMessage.getRoute();
        boardProvider.getBoards().stream().filter(
                board -> board.notBusy() && route.getBoardName().equals(board.getName())
        ).findFirst().ifPresent(board -> {
            board.setRoute(route);
            board.setBusy(true);
            board.setLocation(null);
            //so the plain takes off
        });
    }
}

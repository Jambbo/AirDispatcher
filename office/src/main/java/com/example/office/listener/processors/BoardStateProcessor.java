package com.example.office.listener.processors;

import com.example.common.bean.AirPort;
import com.example.common.bean.Board;
import com.example.common.bean.Route;
import com.example.common.messages.AirPortStateMessage;
import com.example.common.messages.BoardStateMessage;
import com.example.common.processor.MessageConverter;
import com.example.common.processor.MessageProcessor;
import com.example.office.provider.AirPortsProvider;
import com.example.office.provider.BoardsProvider;
import com.example.office.service.WaitingRoutesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

//to top up by cache info
@Slf4j
@Component("BOARD_STATE")
@RequiredArgsConstructor
public class BoardStateProcessor implements MessageProcessor<BoardStateMessage> {

    private final MessageConverter converter;
    private final KafkaTemplate<String, String> kafkaTemplate;

    //to throw from waitingRoutesService the routes that board took
    private final WaitingRoutesService waitingRoutesService;
    //to update info on the current plane
    private final BoardsProvider boardsProvider;
    //towards us came certain state, plane arrived to another airport, and we will add info
    //by using airPortsProvider, that to the airport came certain plane (would be added new plane)
    private final AirPortsProvider airPortsProvider;

    @Override
    public void process(String jsonMessage) {
        BoardStateMessage message  = converter.extractMessage(
                jsonMessage,BoardStateMessage.class
        );
        Board board = message.getBoard();
        //to compare with a previous condition
        Optional<Board> previousOptional = boardsProvider.getBoardByName(board.getName());

        AirPort airPort = airPortsProvider.getAirPort(board.getLocation());

        boardsProvider.addBoard(board);
        //means that got the new route
        if(previousOptional.isPresent() && board.hasRoute() && !previousOptional.get().hasRoute()){
            Route route = board.getRoute();
            waitingRoutesService.remove(route); // removing the route from waitingList, cos it is currently working, the new route in process
        }

        if(previousOptional.isEmpty() || !board.isBusy() && previousOptional.get().isBusy()){
            airPort.addBoard(board.getName());
            //airport state was changed, new plane arrived, and then we are telling to kafka about this =>
            kafkaTemplate.sendDefault(converter.toJson(new AirPortStateMessage(airPort)));
        }

    }
}

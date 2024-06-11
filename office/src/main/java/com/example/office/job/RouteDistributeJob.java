package com.example.office.job;

import com.example.common.bean.AirPort;
import com.example.common.bean.Board;
import com.example.common.bean.Route;
import com.example.common.bean.RoutePath;
import com.example.common.messages.AirPortStateMessage;
import com.example.common.messages.OfficeRouteMessage;
import com.example.common.processor.MessageConverter;
import com.example.office.provider.AirPortsProvider;
import com.example.office.provider.BoardsProvider;
import com.example.office.service.PathService;
import com.example.office.service.WaitingRoutesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class RouteDistributeJob {

    private final PathService pathService;
    private final WaitingRoutesService waitingRoutesService;
    private final AirPortsProvider airPortsProvider;
    private final BoardsProvider boardsProvider;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MessageConverter converter;


    @Scheduled(initialDelay = 500, fixedDelay = 2500)
    private void distribute(){
        waitingRoutesService.getList()
                .stream()
                .filter(Route::isNotAssigned)
                .forEach(route -> {
                    //to find a plane who is on this location
                    String startLocation = route.getPath().get(0).getFrom().getName();

                    boardsProvider.getBoards().stream()
                            .filter(board -> startLocation.equals(board.getLocation())&& board.notBusy())
                            .findFirst()
                            .ifPresent(board -> sendBoardToRoute(route,board));

                    if (route.isNotAssigned()){
                        boardsProvider.getBoards().stream().filter(Board::notBusy)
                                .findFirst()
                                .ifPresent(board -> {
                                    String currentLocation = board.getLocation();
                                    if(!currentLocation.equals(startLocation)){
                                        RoutePath routePath = pathService.makePath(currentLocation,startLocation);
                                        route.getPath().add(0, routePath);
                                    }
                                    sendBoardToRoute(route,board);
                                });
                    }
                });
    }

    private void sendBoardToRoute(Route route, Board board){
        String boardName = board.getName();
        route.setBoardName(boardName);
        //The plane takes off, updating airport
        AirPort airPort = airPortsProvider.findAirPortAndRemoveBoard(boardName);
        board.setLocation(null);//plane is flying
        //updating information via route in kafka
        kafkaTemplate.sendDefault(converter.toJson(new OfficeRouteMessage(route)));
        kafkaTemplate.sendDefault(converter.toJson(new AirPortStateMessage(airPort)));
    }

}

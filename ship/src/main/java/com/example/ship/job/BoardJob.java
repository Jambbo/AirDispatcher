package com.example.ship.job;

import com.example.common.bean.Board;
import com.example.common.bean.RoutePath;
import com.example.common.messages.BoardStateMessage;
import com.example.common.processor.MessageConverter;
import com.example.ship.provider.BoardProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@EnableScheduling
@EnableAsync
@RequiredArgsConstructor
public class BoardJob {

    private final BoardProvider boardProvider;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MessageConverter converter;

    //processing the aircraft's movement while he's flying
    @Scheduled(initialDelay = 0, fixedDelay = 250)
    public void fly(){
        boardProvider.getBoards().stream()
                .filter(Board::hasRoute)
                .forEach(board -> {
                    board.getRoute()
                            .getPath()
                            .stream().filter(RoutePath::isInProgress)
                            .findFirst()
                            .ifPresent(routePath -> {
                                board.calculatePosition(routePath);
                                routePath.addProgress(board.getSpeed());
                                if(routePath.isDone()){
                                    board.setLocation(routePath.getTo().getName());
                                }
                            });
                });
    }
    @Async
    @Scheduled(initialDelay = 0, fixedDelay = 1000)
    public void notifyState(){
        boardProvider.getBoards().stream().filter(Board::isBusy)
                .forEach(board -> {
                    Optional<RoutePath> routePath = board.getRoute()
                            .getPath()
                            .stream()
                            .filter(RoutePath::isInProgress)
                            .findAny();//to get the part of route that is in process atm, line segment, where the plane is flying atm
                    if(routePath.isEmpty()){
                        List<RoutePath> pathList = board.getRoute().getPath();
                        board.setLocation(pathList.get(pathList.size()-1).getTo().getName());
                        board.setBusy(false);
                    }

                    BoardStateMessage msg = new BoardStateMessage();
                    kafkaTemplate.sendDefault(converter.toJson(msg));
                });
    }

}

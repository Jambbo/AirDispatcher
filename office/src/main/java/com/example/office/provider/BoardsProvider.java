package com.example.office.provider;

import com.example.common.bean.Board;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class BoardsProvider {

    @Getter
    private final List<Board> boards = new ArrayList<>();
    private final Lock lock = new ReentrantLock(true); //true cos we  care about order
    public Optional<Board> getBoardByName(String boardName){
        return boards.stream().filter(b ->
                b.getName().equals(boardName)
        )
                .findFirst();
    }

    public void addBoard(Board board){
        try {
            lock.lock();
            Optional<Board> optionalBoard = getBoardByName(board.getName());
            if (optionalBoard.isPresent()) {
                int index = boards.indexOf(optionalBoard.get());
                boards.set(index, board);
            } else {
                boards.add(board);
            }
        }finally{
            lock.unlock();
        }
    }

}

package com.example.common.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode // to compare airports in list
public class AirPort {

    private String name;
    private List<String> boards = new ArrayList<>(); // list of planes that r atm at airport
    private int x;
    private int y;

    public void addBoard(String boardName){
        int ind = boards.indexOf(boardName);
        if(ind >= 0){
            boards.set(ind,boardName);
        }else{
            boards.add(boardName);
        }
    }

    public void removeBoard(String boardName){
        boards.remove(boardName);
    }

}

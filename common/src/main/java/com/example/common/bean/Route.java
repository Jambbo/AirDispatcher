package com.example.common.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Route {

    private String boardName;//board to which is assigned current route
    private List<RoutePath> path = new ArrayList<>(); //line segments

    //this routes we will assign in advance and send to the queue

    public boolean isNotAssigned(){
        return boardName == null;
    }

}

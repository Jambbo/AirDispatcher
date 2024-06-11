package com.example.common.messages;

import com.example.common.bean.Board;
import com.example.common.bean.Source;
import com.example.common.bean.Type;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BoardStateMessage extends Message{

    private Board board;

    public BoardStateMessage(){
        this.source = Source.BOARD;
        this.type = Type.STATE;
    }

    public BoardStateMessage(Board val) {
        this();
        this.board = val;
    }
}

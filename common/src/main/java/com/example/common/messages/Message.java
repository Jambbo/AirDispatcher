package com.example.common.messages;

import com.example.common.bean.Source;
import com.example.common.bean.Type;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Message {

    protected Type type;
    protected Source source;

    //to figure out what handler to use for the current message type
    public String getCode(){
        return source.name()+"_"+type.name();
    }
}

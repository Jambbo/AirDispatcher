package com.example.common.messages;

import com.example.common.bean.AirPort;
import com.example.common.bean.Source;
import com.example.common.bean.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirPortStateMessage extends Message{

    private AirPort airPort; //AirPortStateMessage class will contain airport as json

    public AirPortStateMessage() {
        this.source = Source.AIRPORT;
        this.type = Type.STATE;
    }

    public AirPortStateMessage(AirPort airPort) {
        this();
        this.airPort = airPort;
    }
}

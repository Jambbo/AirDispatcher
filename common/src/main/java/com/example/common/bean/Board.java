package com.example.common.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Board {

    private String name;
    private String location;
    private Route route;
    private boolean isBusy;
    private double speed;
    private double x;
    private double y;
    private double angle; // to turn in on UI

    //Needs, cos for example when I will find it in a list, would be comfy to use "board::noBusy", using streams
    public boolean notBusy(){
        return !isBusy;
    }

    public boolean hasRoute(){
        return route!=null;
    }

    public void calculatePosition(RoutePath routeDirection){
        double t = routeDirection.getProgress()/100;

        double toX = (1-t)*routeDirection.getFrom().getX() + t*routeDirection.getTo().getX();
        double toY = (1-t)*routeDirection.getFrom().getY() + t*routeDirection.getTo().getY();

        //things to find angle of rotation
        double deltaX = this.x-toX;
        double deltaY = this.y-toY;

        this.angle = Math.toDegrees(Math.atan2(deltaY, deltaX));

        if(this.angle < 0){
            this.angle = 360 + this.angle;
        }

        this.x = toX;
        this.y = toY;
    }

}

package com.example.common.bean;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RoutePath {

    private RoutePoint from;
    private RoutePoint to;
    private double progress;//on each line segment we will check in which condition it is now(0to100)

    //What does progress depend on? On speed.
    public void addProgress(double speed) {
        progress += speed;
        if (progress > 100) {
            progress = 100;
        }
    }

    public boolean isInProgress() {
        return progress < 100;
    }

    public boolean isDone() {
        return progress == 100;
    }

}

package com.example.office.service;

import com.example.common.bean.Route;
import com.example.common.bean.RoutePath;
import com.example.common.bean.RoutePoint;
import com.example.office.provider.AirPortsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PathService {

    private final AirPortsProvider airPortsProvider;

    public RoutePath makePath(String from, String to){
        return new RoutePath(airPortsProvider.getRoutePoint(from),airPortsProvider.getRoutePoint(to),0);
    }

    //There is in arguments a list of airports that plane should visit via route and based on
    // this list this method will generate a full route
    public Route convertLocationsToRoute(List<String> locations){
        Route route = new Route();
        List<RoutePath> path = new ArrayList<>();
        List<RoutePoint> points = new ArrayList<>();

        locations.forEach(location -> {
            airPortsProvider.getPorts().stream()
                    .filter(airPort -> airPort.getName().equals(location))
                    .findFirst()
                    .ifPresent(airPort -> {
                        points.add(RoutePoint.builder()
                                .x(airPort.getX())
                                .y(airPort.getY())
                                .name(airPort.getName())
                                .build());
                    });
        });

        for (int i=0; i<points.size()-1;i++){
            path.add(new RoutePath());
        }

        path.forEach(row -> {
            int index = path.indexOf(row);
            if(row.getFrom()==null){
                row.setFrom(points.get(index));
                if(points.size()>index+1){
                    row.setTo(points.get(index+1));
                }else{
                    row.setTo(points.get(index));
                }
            }
        });

        route.setPath(path);

        return route;
    }
}

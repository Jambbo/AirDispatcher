package com.example.office.controllers;

import com.example.common.bean.Route;
import com.example.office.service.PathService;
import com.example.office.service.WaitingRoutesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/routes")
public class RouteRest {

    private final PathService pathService;

    //To store the routes in waiting list
    private final WaitingRoutesService waitingRoutesService;

    //routeLocations - list of airports that plain should visit
    @PostMapping(path = "route")
    public void addRoute(@RequestBody List<String> routeLocations){
        Route route = pathService.convertLocationsToRoute(routeLocations);
        waitingRoutesService.add(route);
    }

}

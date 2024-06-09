package com.example.office.service;

import com.example.common.bean.AirPort;
import com.example.common.bean.Route;
import com.example.common.bean.RoutePath;
import com.example.common.bean.RoutePoint;
import com.example.office.provider.AirPortsProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class WaitingRoutesService {

    private final Lock lock = new ReentrantLock(true);
    @Getter
    private final List<Route> list = new ArrayList<>();

    //Adding the route
    public void add(Route route){
        try{
            lock.lock();
            list.add(route);
        }finally{
            lock.unlock();
        }
    }

    public void remove(Route route){
        try{
            lock.lock();
            list.removeIf(route::equals);
        }finally{
            lock.unlock();
        }
    }


}

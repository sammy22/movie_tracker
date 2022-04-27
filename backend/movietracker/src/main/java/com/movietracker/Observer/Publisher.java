package com.movietracker.Observer;

import java.util.ArrayList;
import java.util.List;

public class Publisher implements Subject {
    private List<Consumer> Consumers = new ArrayList<>();
 
    @Override
    public void attach(Consumer o) {
        Consumers.add(o);
        System.out.printf("Consumers in attach :%d \n",Consumers.size());
    }
 
    @Override
    public void detach(Consumer o) {
        Consumers.remove(o);
        System.out.printf("Consumers in detach :%d \n",Consumers.size());
    }
 
    @Override
    public void notify(String message) {
        for(Consumer o: Consumers) {
            o.update(message);
        }
    }

    public void printConsumerCount(){
        System.out.println(Consumers.size());
    }
}

package com.movietracker.Observer;

import java.util.HashMap;
import java.util.Map;

public class Tracker extends Consumer {

    private static Tracker instance;

    private Tracker() {
    };

    public static Tracker getInstance() {
        if (instance == null) {
            instance = new Tracker();
        }
        return instance;
    }

    private Map<String, Map<String, Fields>> summary = new HashMap<String, Map<String, Fields>>();

    @Override
    public void update(String m) {
        
    }

    public void printSummary() {

    }
}

class Fields {
    int itemsDamaged;
    int itemsSold;
    int itemsPurchased;
}

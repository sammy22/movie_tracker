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
        // Fields f;
        // Map<String, Fields> storeSummary = summary.get(m.getStoreName());
        // if (!summary.containsKey(m.getStoreName())) {
        //     storeSummary = new HashMap<String,  Fields>();
        // }

        // if (m.getName() != null) {
        //     if (storeSummary.containsKey(m.getName())) {
        //         f = storeSummary.get(m.getName());
        //     } else {
        //         f = new Fields();
        //     }

        //     f.itemsDamaged += m.getItemsDamaged();
        //     f.itemsSold += m.getItemsSold();
        //     f.itemsPurchased += m.getItemsPurchased();

        //     storeSummary.put(m.getName(), f);
        //     summary.put(m.getStoreName(), storeSummary);
        // }
    }

    public void printSummary() {

        // for (String key : this.summary.keySet()) {
        //     Map<String, Fields> storeSummary = summary.get(key);
        //     System.out.println("Summary for Store " + key);
        //     System.out.println("Name itemsSold itemsPurchased   itemsDamaged");

        //     for (String k : storeSummary.keySet()) {
        //         Fields f = storeSummary.get(k);
        //         System.out.printf(
        //                 k + " \t" + f.itemsSold + "\t\t\t " + f.itemsPurchased + "\t\t\t " + f.itemsDamaged + "\n");
        //     }
        // }
    }
}

class Fields {
    int itemsDamaged;
    int itemsSold;
    int itemsPurchased;
}

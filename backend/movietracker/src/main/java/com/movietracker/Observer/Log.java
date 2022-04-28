package com.movietracker.Observer;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log extends Consumer {
    private static Log instance = new Log();
    private Log(){};

    
    Logger logger = Logger.getLogger(Log.class.getName());

    public static Log getInstance(){
        return instance;
    }
         
    @Override
    public void update (String m ){
        logger.info(m);
    }
}

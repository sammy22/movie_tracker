package com.movietracker.Observer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log extends Consumer {
    private static Log instance = new Log();
    private Log(){};

    private Logger logger = Logger.getLogger("outputLogs"); ;
    private FileHandler handler;
    

    public void open(int index){
        logger.setUseParentHandlers(false);
        
        if (Files.notExists(Paths.get("logs"))){
            new File("logs").mkdir();   
        }

        try{
        this.handler = new FileHandler("logs/Logger-"+Integer.toString(index)+".txt", true);
        // this.handler.setFormatter(new SimpleFormatter());
        this.handler.setFormatter(new SimpleFormatter() {
            private static final String format = "[%1$tF %1$tT] [%2$-1s] %3$s %n";
  
            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format,
                        new Date(lr.getMillis()),
                        lr.getLevel().getLocalizedName(),
                        lr.getMessage()
                );
            }
        });
        this.logger.addHandler(handler);

        
        } catch(IOException e){
            System.out.println("Failed to create Log file");
            return;
        }
        
    }

    public static Log getInstance(){
        return instance;
    }

    public void close(){
        this.logger.removeHandler(this.handler);
        this.handler.close();
    }
         
    @Override
    public void update (String m ){
        this.logger.log(Level.INFO, m);
    }
}

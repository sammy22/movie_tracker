package com.movietracker.Observer;

// import java.io.File;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.util.Date;
// import java.util.logging.FileHandler;
// import java.util.logging.Level;
// import java.util.logging.LogRecord;
// import java.util.logging.Logger;
// import java.util.logging.SimpleFormatter;

public class Log extends Consumer {
    private static Log instance = new Log();
    private Log(){};

    //private Logger logger = Logger.getLogger("outputLogs"); ;
    //private FileHandler handler;
    

    //public void open(int index){
    //    
    //}

    public static Log getInstance(){
        return instance;
    }

    //public void close(){
        //this.logger.removeHandler(this.handler);
        //this.handler.close();
    //}
         
    @Override
    public void update (String m ){
        System.out.println(m);
    }
}

package com.cobra.tracker.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtil {
 //   private static final String LOG_BINARY = "%s:%s%s";
    private static final String LOG_STRING = "%s:%s";
    private final static Logger LOGGER = Logger.getLogger(LogUtil.class.getName());

    public static void info(String label, String msg) {
        LOGGER.log(Level.INFO, String.format(LOG_STRING, label, msg));
    }

    public static void debug(String label, String msg){
        LOGGER.log(Level.FINE, String.format(LOG_STRING, label, msg));
    }

    public static void error(String message, Throwable ex){
        LOGGER.log(Level.SEVERE, message, ex);
    }

    public static void info(String msg){
        LOGGER.log(Level.INFO, msg);
    }

    public static void warn(String msg){
        LOGGER.log(Level.WARNING, msg);
    }

    public static void debug(String msg){
        LOGGER.log(Level.FINE, msg);
    }

   /* public static void info(String label, byte [] data){
        log(Level.INFO, label, data);
    }

    public static void debug(String label, byte [] data){
        log(Level.FINE, label, data);
    }

    private static void log(Level level, String label, byte [] data){
        if (LOGGER.isLoggable(level)){
            LOGGER.log(level,String.format(LOG_BINARY,label,System.lineSeparator(),
                    DataConverter.binaryToHexForDisplay(data)));
        }

    }*/
}

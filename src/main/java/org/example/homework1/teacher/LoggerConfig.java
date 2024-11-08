package org.example.homework1.teacher;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerConfig {

    // ANSI escape codes for colors
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";

    public static void configureLogger(Logger logger) {
        logger.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);

        handler.setFormatter(new ColorLogFormatter());

        logger.addHandler(handler);

        logger.setLevel(Level.ALL);
    }

    static class ColorLogFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            String levelColor;
            if (record.getLevel() == Level.SEVERE) {
                levelColor = RED;
            } else if (record.getLevel() == Level.WARNING) {
                levelColor = YELLOW;
            } else if (record.getLevel() == Level.INFO) {
                levelColor = GREEN;
            } else {
                levelColor = RESET;
            }

            return levelColor + record.getLevel() + ": " + record.getMessage() + RESET + "\n";
        }
    }


}

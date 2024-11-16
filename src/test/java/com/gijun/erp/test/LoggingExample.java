package com.gijun.erp.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingExample {
    private static final Logger logger = LoggerFactory.getLogger(LoggingExample.class);

    public void logExample() {
        logger.info("This is an INFO log");
        logger.debug("This is a DEBUG log");
        logger.error("This is an ERROR log");
    }
}

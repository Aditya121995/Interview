package com.problems.loggingFrameWork;

public class LoggingFrameWorkDemo {
    public static void main(String[] args) {
        Logger logger = new Logger("DemoFile");

        logger.changeLogLevel(LoggerLevel.DEBUG);
        logger.clearAppenders();
        logger.addAppender(new ConsoleAppender());
        logger.addAppender(new FileAppender("/Users/adi/Documents/Interview Prep/Interview/abc.log"));

        Thread thread1 = new Thread( () -> {
            for(int i=0; i<10; i++){
                logger.info("Info is getting printed :: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            }
        });

        Thread thread2 = new Thread( () -> {
            for(int i=0; i<10; i++){
                logger.error("Error is getting printed :: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            }
        });

        Thread thread3 = new Thread( () -> {
            for(int i=0; i<10; i++){
                logger.debug("Debug is getting printed :: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
//        logger.addAppender(new FileAppender("abc.log"));
        logger.changeLogLevel(LoggerLevel.INFO);

        logger.fatal("Critical System Failure");
        logger.info("Application started successfully");
    }
}

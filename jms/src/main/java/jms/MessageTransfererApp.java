package jms;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The following commandline args must be provided:
 *
 * args[0] : Spring context file name on the classpath : i.e., classpath:my.app.context.xml
 *
 * args[1] : Spring context MessageTransferer bean name : i.e., messageTransferer
 *
 * Log4j logging is implemented. To enable, set the log4j system property : i.e.,
 *
 *     -Dlog4j.configuration=file:///mylocation/log4j.properties
 *
 * on the commandline.
 */
public class MessageTransfererApp {
    private static final Logger logger = Logger.getLogger(MessageTransfererApp.class);

    public static void main(String[] args) {
        String contextName = args[0];
        String beanName = args[1];
        validate(contextName, beanName);
        transfer(contextName, beanName);
    }

    private static void transfer(String contextName, String beanName) {
        double startTimeInMillis = System.currentTimeMillis();
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(contextName);
            MessageTransferer messageTransferer = (MessageTransferer) context.getBean(beanName);
            context.registerShutdownHook();
            logger.info("*** Transferring messages...");
            int count = messageTransferer.transfer();
            logger.info("*** Messages transferred: " + count);
        } catch (Throwable t) {
            logger.error("*** Message transfer failed:\n" + t);
        } finally {
            double elapsedTimeInMillis = (System.currentTimeMillis() - startTimeInMillis);
            logger.info("*** Elapsed time: " + (elapsedTimeInMillis / 1000) + "s : " + (elapsedTimeInMillis / 1000) / 60 + "m");
        }
        System.exit(0);
    }

    private static void validate(String contextName, String beanName) {
        if (contextName == null) {
            logger.error("*** Spring (classpath) context file name required: " + contextName);
            System.exit(1);
        }
        if (beanName == null) {
            logger.error("*** MessageTransferer bean name required: " + beanName);
            System.exit(1);
        }
    }
}
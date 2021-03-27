package jms;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MessageTransfererTestApp {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:message.transfer.test.context.xml");
        MessageTransferer messageTransferer = (MessageTransferer) context.getBean("templateMessageTransferer");
        context.registerShutdownHook();
        double startTimeInMillis = System.currentTimeMillis();
        try {
            System.out.println("*** Transferring messages...");
            int count = messageTransferer.transfer();
            System.out.println("*** Messages transferred: " + count);
        } catch (Throwable t) {
            System.out.println("*** Message transfer failed: " + t);
        } finally {
            double elapsedTimeInMillis = (System.currentTimeMillis() - startTimeInMillis);
            System.out.println("*** Elapsed time: " + (elapsedTimeInMillis / 1000) + "s : " + (elapsedTimeInMillis / 1000) / 60 + "m");
        }
        System.exit(0);
    }
}
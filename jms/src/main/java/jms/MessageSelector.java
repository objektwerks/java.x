package jms;

import java.util.Date;

public class MessageSelector {
    private static final MessageSelector instance = new MessageSelector();
    private static final long oneMinuteOffset = (60 * 1000);

    private MessageSelector() {
    }

    public static String jmsTimestampLessThanEqualDateTime() {
        return "JMSTimestamp <= " + new Date().getTime() + oneMinuteOffset;
    }
}
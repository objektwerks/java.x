package jms;

import javax.jms.JMSException;

public interface MessageTransferer {
    public int transfer() throws JMSException;
}
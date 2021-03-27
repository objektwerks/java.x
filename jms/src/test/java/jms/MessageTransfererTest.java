package jms;

import javax.annotation.Resource;
import javax.jms.JMSException;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/message.transfer.test.context.xml"})
public class MessageTransfererTest {
    @Resource private JmsTemplate sourceJmsTemplate;
    @Resource private JmsTemplate targetJmsTemplate;
    @Resource private MessageTransferer pooledMessageTransferer;

    @Test
    public void transferByPooledMessageTransferer() throws JMSException {
        int numberOfProducerMessages = 1000;
        for (int i = 0; i < numberOfProducerMessages; i++) {
            sourceJmsTemplate.convertAndSend("message");
        }
        assertEquals(numberOfProducerMessages, pooledMessageTransferer.transfer());
        int numberOfConsumerMessages = 0;
        Object message;
        while (true) {
            message = targetJmsTemplate.receive();
            if (message != null) {
                numberOfConsumerMessages++;
            } else {
                break;
            }
        }
        assertEquals(numberOfProducerMessages, numberOfConsumerMessages);
    }
}
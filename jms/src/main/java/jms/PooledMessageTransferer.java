package jms;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.ProducerCallback;

public class PooledMessageTransferer implements MessageTransferer {
    private static final Logger logger = Logger.getLogger(PooledMessageTransferer.class);
    private static final int defaultCorePoolSize = 2;
    private static final int defaultMaxPoolSize = 4;
    private static final int defaultQueueSize = 4;
    private static final int poolThreadTimeout = 60;

    private JmsTemplate sourceJmsTemplate;
    private JmsTemplate targetJmsTemplate;
    private int corePoolSize;
    private int maxPoolSize;
    private int queueSize;
    private String messageSelector;

    public PooledMessageTransferer(JmsTemplate sourceJmsTemplate,
                                   JmsTemplate targetJmsTemplate) {
        this.sourceJmsTemplate = sourceJmsTemplate;
        this.targetJmsTemplate = targetJmsTemplate;
        this.corePoolSize = defaultCorePoolSize;
        this.maxPoolSize = defaultMaxPoolSize;
        this.queueSize = defaultQueueSize;
        this.messageSelector = "";
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public void setMessageSelector(String messageSelector) {
        this.messageSelector = messageSelector;
    }

    public int transfer() throws JMSException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize,
                                                             maxPoolSize,
                                                             poolThreadTimeout,
                                                             TimeUnit.SECONDS,
                                                             new LinkedBlockingQueue<Runnable>(queueSize));
        List<Callable<Class<Void>>> messageTasks = new ArrayList<Callable<Class<Void>>>(queueSize);
        AtomicInteger consumerMessageCount = new AtomicInteger(0);
        AtomicInteger producerMessageCount = new AtomicInteger(0);
        for (int i = 0; i < queueSize; i++) {
            messageTasks.add(new MessageTask(consumerMessageCount, producerMessageCount));
        }
        try {
            logger.info("Transferring messages...");
            logger.info("\t[task -> consumed:produced]");
            executor.invokeAll(messageTasks);
            if (consumerMessageCount.intValue() != producerMessageCount.intValue()) {
                logger.error("Message transfer partially failed: " + consumerMessageCount + ":" + producerMessageCount);
            }
        } catch (Throwable t) {
            logger.error("Message transfer failed: " + consumerMessageCount + ":" + producerMessageCount, t);
            throw new JMSException(t.getMessage());
        } finally {
            executor.shutdown();
        }
        logger.info("Messages transferred: " + consumerMessageCount + ":" + producerMessageCount);
        return consumerMessageCount.intValue();
    }

    private class MessageTask implements Callable<Class<Void>> {
        private AtomicInteger consumerMessageCount;
        private AtomicInteger producerMessageCount;

        public MessageTask(AtomicInteger consumerMessageCount,
                           AtomicInteger producerMessageCount) {
            this.consumerMessageCount = consumerMessageCount;
            this.producerMessageCount = producerMessageCount;
        }

        public Class<Void> call() {
            consume();
            return Void.TYPE;
        }

        private void consume() {
            List<Message> messages = new ArrayList<Message>();
            Message message;
            while (true) {
                if (messageSelector == null || messageSelector.isEmpty()) {
                    message = sourceJmsTemplate.receive();
                } else {
                    message = sourceJmsTemplate.receiveSelected(messageSelector);
                }
                if (message != null) {
                    messages.add(message);
                    if (consumerMessageCount.incrementAndGet() % 100 == 0) {
                        produce(messages);
                        messages.clear();
                        logger.info("\t\t" + currentThreadName() + " -> " + consumerMessageCount + ":" + producerMessageCount);
                    }
                } else {
                    produce(messages);
                    break;
                }
            }
        }

        private void produce(final List<Message> messages) {
            targetJmsTemplate.execute(new ProducerCallback() {
                public Object doInJms(Session session, MessageProducer producer) throws JMSException {
                    for (Message message : messages) {
                        producer.send(message);
                        producerMessageCount.incrementAndGet();
                    }
                    return null;
                }
            });
        }

        private String currentThreadName() {
            String threadName = Thread.currentThread().getName();
            if (threadName.length() > 2) {
                threadName = threadName.substring(threadName.length() - 1);
            }
            return threadName;
        }
    }
}
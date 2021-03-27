package xml.processing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.transform.TransformerConfigurationException;

import org.apache.commons.io.IOUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.xml.sax.SAXException;

public class XmlProcessingTest {
    private static final int corePoolSize = 2;
    private static final int maxPoolSize = 4;
    private static final int queueSize = 10000;
    private static final int poolThreadTimeout = 60;

    @Test
    public void validate() throws InterruptedException, IOException, SAXException {
        XsdValidator xsdValidator = new PooledXsdValidator(true, "/ping.xsd", 1);
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/ping.xml"));
        ThreadPoolExecutor executor = createThreadPoolExecutor();
        List<Callable<Class<Void>>> xsdValidators = new ArrayList<Callable<Class<Void>>>(queueSize);
        AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < queueSize; i++) {
            xsdValidators.add(new XsdValidatorTask(xsdValidator, xml, count));
        }
        executor.invokeAll(xsdValidators);
        assertEquals(queueSize, count.intValue());
    }

    @Test
    public void transform() throws InterruptedException, IOException, TransformerConfigurationException {
        XsltTransformer xsltTransformer = new PooledXsltTransformer("/echo.xsl", 1);
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/ping.xml"));
        ThreadPoolExecutor executor = createThreadPoolExecutor();
        List<Callable<Class<Void>>> xsltTransformers = new ArrayList<Callable<Class<Void>>>(queueSize);
        AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < queueSize; i++) {
            xsltTransformers.add(new XsltTransformerTask(xsltTransformer, xml, count));
        }
        executor.invokeAll(xsltTransformers);
        assertEquals(queueSize, count.intValue());
    }

    private ThreadPoolExecutor createThreadPoolExecutor() {
        return new ThreadPoolExecutor(corePoolSize,
                                      maxPoolSize,
                                      poolThreadTimeout,
                                      TimeUnit.SECONDS,
                                      new LinkedBlockingQueue<Runnable>(queueSize));
    }

    private class XsdValidatorTask implements Callable<Class<Void>> {
        private XsdValidator xsdValidator;
        private String xml;
        private AtomicInteger count;

        public XsdValidatorTask(XsdValidator xsdValidator, String xml, AtomicInteger count) {
            this.xsdValidator = xsdValidator;
            this.xml = xml;
            this.count = count;
        }

        public Class<Void> call() {
            xsdValidator.validate(xml);
            count.incrementAndGet();
            return Void.TYPE;
        }
    }

    private class XsltTransformerTask implements Callable<Class<Void>> {
        private XsltTransformer xsltTransformer;
        private String xml;
        private AtomicInteger count;

        public XsltTransformerTask(XsltTransformer xsltTransformer, String xml, AtomicInteger count) {
            this.xsltTransformer = xsltTransformer;
            this.xml = xml;
            this.count = count;
        }

        public Class<Void> call() {
            assertTrue(xsltTransformer.transform(xml).indexOf("</Echo>") > 0);
            count.incrementAndGet();
            return Void.TYPE;
        }
    }
}
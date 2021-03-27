package xml.processing;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

public class PooledXsltTransformer implements XsltTransformer {
    private static final Logger logger = Logger.getLogger(PooledXsltTransformer.class);

    private TransformerPool poolOfTransformers;

    public PooledXsltTransformer(String xslt,
                                 int poolSize) throws TransformerConfigurationException {
        Source source = new StreamSource(this.getClass().getResourceAsStream(xslt));
        TransformerFactory factory = TransformerFactory.newInstance();
        Templates templates = factory.newTemplates(source);
        poolOfTransformers = new TransformerPool(new PoolableTransformerFactory(templates), poolSize);
    }

    public String transform(String xml) {
        Transformer transformer = null;
        String targetXml;
        try {
            StreamSource source = new StreamSource(new StringReader(xml));
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer = (Transformer) poolOfTransformers.borrowObject();
            transformer.transform(source, result);
            targetXml = writer.toString();
        } catch (TransformerException e) {
            throw new IllegalArgumentException("Failed to transform xml.", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to get transformer from pool: " + e.getMessage(), e);
        } finally {
            if (transformer != null) transformer.reset();
            poolOfTransformers.returnObject(transformer);
            logger.trace("TransformerPool size: " + poolOfTransformers.getNumIdle());
        }
        return targetXml;
    }

    private static class TransformerPool extends GenericObjectPool {
        private TransformerPool(PoolableObjectFactory factory, int poolSize) {
            super(factory);
            GenericObjectPool.Config config = new GenericObjectPool.Config();
            config.maxActive = poolSize;
            config.maxIdle = -1;
            setConfig(config);
        }

        @Override
        public void returnObject(Object object) {
            try {
                super.returnObject(object);
            } catch (Exception e) {
                logger.warn("Failed to return object to pool.", e);
                addObject();
            }
        }

        @Override
        public void addObject() {
            try {
                super.addObject();
            } catch (Exception e) {
                logger.warn("Failed to add object to pool.", e);
            }
        }
    }

    private static class PoolableTransformerFactory extends BasePoolableObjectFactory {
        private Templates templates;

        private PoolableTransformerFactory(Templates templates) {
            this.templates = templates;
        }

        public synchronized Object makeObject() throws Exception {
            return templates.newTransformer();
        }

        public synchronized boolean validateObject(Object object) {
            return (object != null);
        }
    }
}
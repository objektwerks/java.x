package xml.processing;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public class PooledXsdValidator implements XsdValidator {
    private static final Logger logger = Logger.getLogger(PooledXsdValidator.class);

    private boolean isEnabled;
    private int poolSize;
    private ValidatorPool poolOfValidators;

    public PooledXsdValidator(boolean isEnabled,
                              String xsd,
                              int poolSize) throws SAXException {
        this.isEnabled = isEnabled;
        this.poolSize = poolSize;
        List<String> xsds = new ArrayList<String>(1);
        xsds.add(xsd);
        this.poolOfValidators = new ValidatorPool(new PoolableValidatorFactory(xsds), poolSize);
    }

    public PooledXsdValidator(boolean isEnabled,
                              List<String> xsds,
                              int poolSize) throws SAXException {
        this.isEnabled = isEnabled;
        this.poolSize = poolSize;
        this.poolOfValidators = new ValidatorPool(new PoolableValidatorFactory(xsds), poolSize);
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void validate(String xml) {
        if (isEnabled && isEmpty(xml)) {
            Validator validator = null;
            try {
                validator = (Validator) poolOfValidators.borrowObject();
                try {
                	validator.validate(new StreamSource(new StringReader(xml)));
                } catch (Exception e) {
                    poolOfValidators.returnObject(validator);
                    validator = (Validator) poolOfValidators.borrowObject();
                    retry(validator, xml);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to validate xml: " + e.getMessage(), e);
            } finally {
                poolOfValidators.returnObject(validator);
            }
        }
    }

    private void retry(Validator validator, String xml) throws IOException, SAXException {
        validator.validate(new StreamSource(new StringReader(xml)));
    }

    private boolean isEmpty(String xml) {
        if (xml == null || xml.length() == 0) {
            throw new IllegalArgumentException("Failed to validate xml. Xml is null or length is zero: " + xml);
        }
        return true;
    }

    private static class ValidatorPool extends GenericObjectPool {
        public ValidatorPool(PoolableValidatorFactory factory, int poolSize) {
            super(factory);
            GenericObjectPool.Config config = new GenericObjectPool.Config();
            config.maxActive = poolSize;
            config.maxIdle = -1;
            setConfig(config);
        }

        @Override
        public void returnObject(Object object) {
            try {
                if (object instanceof Validator) {
                    Validator validator = (Validator) object;
                    validator.reset();
                }
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

    private static class PoolableValidatorFactory extends BasePoolableObjectFactory {
        private Schema schema;

        public PoolableValidatorFactory(List<String> xsds) throws SAXException {
            this.schema = createSchema(xsds);
        }

        @Override
        public synchronized Object makeObject() throws Exception {
            return schema.newValidator();
        }

        @Override
        public synchronized boolean validateObject(Object object) {
            return (object != null);
        }

        private Schema createSchema(List<String> xsds) throws SAXException {
            List<Source> list = new ArrayList<Source>(xsds.size());
            for (String xsd : xsds) {
                list.add(new StreamSource(this.getClass().getResourceAsStream(xsd)));
            }
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            return schemaFactory.newSchema(list.toArray(new Source[list.size()]));
        }
    }
}
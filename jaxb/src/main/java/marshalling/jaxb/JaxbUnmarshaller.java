package marshalling.jaxb;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class JaxbUnmarshaller {
    private JAXBContext context;

    public JaxbUnmarshaller() {
    }

    public JaxbUnmarshaller(String packageName) throws JAXBException {
        this.context = JAXBContext.newInstance(packageName);
    }

    @SuppressWarnings("unchecked")
	public <E> E unmarshal(String xml) {
        E entity;
        try {
            Unmarshaller unmarshaller = context.createUnmarshaller();
            entity = ((JAXBElement<E>) unmarshaller.unmarshal(new StringReader(xml))).getValue();
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Failed to unmarshal xml.", e);
        }
        return entity;
    }
}
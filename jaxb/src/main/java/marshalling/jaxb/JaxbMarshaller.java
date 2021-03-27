package marshalling.jaxb;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JaxbMarshaller {
    private JAXBContext context;

    public JaxbMarshaller() {
    }

    public JaxbMarshaller(String packageName) throws JAXBException {
        this.context = JAXBContext.newInstance(packageName);
    }

    public <E> String marshal(E entity) {
        StringWriter writer = new StringWriter();
        try {
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(entity, writer);
        } catch (JAXBException e) {
            String message = (entity == null ? "" : entity.getClass().getName());
            throw new IllegalArgumentException("Failed to marshal: " + message, e);
        }
        return writer.toString();
    }
}
package marshalling.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
    private final static QName QNAME = new QName("http://javawerks/ping", "Ping");

    public ObjectFactory() {
    }

    @XmlElementDecl(namespace = "http://javawerks/ping", name = "Ping")
    public JAXBElement<Ping> createBooking(Ping ping) {
        return new JAXBElement<Ping>(QNAME, Ping.class, null, ping);
    }
}
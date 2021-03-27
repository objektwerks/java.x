package marshalling.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="Ping", namespace="http://javawerks/ping")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ping", propOrder = {
        "value"
})
public class Ping {
    @XmlElement(name = "value")
    private String value;

    public Ping() {
    }

    public Ping(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
package persistence.hibernate;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name="list", query="from Ping"),
    @NamedQuery(name="findById", query="from Ping p where p.id = ?")
})
@XmlRootElement(name="Ping", namespace="http://javawerks/ping")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ping", propOrder = { "id", "version", "value"})
public class Ping implements Serializable {
	private static final long serialVersionUID = 8356122459527809326L;

	@Id
    @Column(length = 36)
    @XmlElement()
    private String id;

    @Version
    @XmlElement()
    private int version;

    @XmlElement()
    private String value;

    private Ping() {
        this.id = UUID.randomUUID().toString();
    }

    public Ping(String value) {
        this();
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public int getVersion() {
		return version;
	}

	public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        Ping ping = (Ping) object;
        return new EqualsBuilder()
            .append(id, ping.getId())
            .append(value, ping.getValue())
            .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("value", value)
            .toString();
    }
}
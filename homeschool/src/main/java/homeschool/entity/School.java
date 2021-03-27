package homeschool.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "school")
@EntityListeners({ AuditableEntityListener.class })
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "school")
public class School implements Auditable, Comparable<School>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlAttribute(name = "id")
    private long id;

    @Version
    @XmlAttribute(name = "version")
    private int version;

    @Column(name = "name", length = 64, nullable = false)
    @NotBlank
    @XmlElement(name = "name")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "modified", nullable = false)
    @XmlAttribute(name = "modified")
    private Date modified;

    @Column(name = "modifiedBy", nullable = false)
    @NotBlank
    @XmlAttribute(name = "modifiedby")
    private String modifiedBy;

    private School() {
    }

    public School(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void modified(Date modified) {
        this.modified = modified;
    }

    @Override
    public void modifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public int compareTo(School other) {
        return new CompareToBuilder()
                .append(getId(), other.getId())
                .append(getName(), other.getName())
                .toComparison();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (this == object) {
            return true;
        }
        School other = (School) object;
        return new EqualsBuilder()
                .append(getId(), other.getId())
                .append(getName(), other.getName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .append(getName())
                .hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(getId())
                .append(getName())
                .toString();
    }
}
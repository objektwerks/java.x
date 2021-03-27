package homeschool.entity;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "student")
@EntityListeners({ AuditableEntityListener.class })
@XmlRootElement(name = "student", namespace = "http://homschool/schema/v1_0/student")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "student")
public class Student implements Auditable, Comparable<Student>, Serializable {
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("year ASC")
    @XmlElement(name = "grade")
    private List<Grade> grades;

    @Temporal(TemporalType.DATE)
    @Column(name = "modified", nullable = false)
    @XmlAttribute(name = "modified")
    private Date modified;

    @Column(name = "modifiedBy", nullable = false)
    @NotBlank
    @XmlAttribute(name = "modifiedby")
    private String modifiedBy;

    private Student() {
    }

    public Student(String name) {
        grades = new ArrayList<>();
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

    public List<Grade> getGrades() {
        return grades;
    }

    public boolean addGrade(Grade grade) {
        return grades.add(grade);
    }

    public boolean removeGrade(Grade grade) {
        return grades.remove(grade);
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
    public int compareTo(Student other) {
        return new CompareToBuilder()
                .append(getId(), other.getId())
                .append(getName(), other.getName())
                .append(getGrades(), other.getGrades())
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
        Student other = (Student) object;
        return new EqualsBuilder()
                .append(getId(), other.getId())
                .append(getName(), other.getName())
                .append(getGrades(), other.getGrades())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .append(getName())
                .append(getGrades())
                .hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(getId())
                .append(getName())
                .append(getGrades())
                .toString();
    }

    public String marshal() {
        String studentXml;
        try {
            JAXBContext context = JAXBContext.newInstance(ObjectFactory.packageName);
            Marshaller marshaller = context.createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(this, writer);
            studentXml = writer.toString();
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Failed to marshal student.", e);
        }
        return studentXml;
    }

    public Student unmarshal(String studentXml) {
        Student student;
        try {
            JAXBContext context = JAXBContext.newInstance(ObjectFactory.packageName);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            student = (Student) unmarshaller.unmarshal(new StringReader(studentXml));
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Failed to unmarshal student xml.", e);
        }
        return student;
    }
}
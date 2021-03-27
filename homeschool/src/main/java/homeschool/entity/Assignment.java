package homeschool.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
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
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "assignment")
@EntityListeners({ AuditableEntityListener.class })
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "assignment")
public class Assignment implements Auditable, Comparable<Assignment>, Serializable {
    public static enum Type {Quiz, Standard, Test}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlAttribute(name = "id")
    private long id;

    @Version
    @XmlAttribute(name = "version")
    private int version;

    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id")
    @NotNull
    @XmlElement(name = "teacher")
    private Teacher teacher;

    @Temporal(TemporalType.DATE)
    @Column(name = "assigned", nullable = false)
    @XmlAttribute(name = "assigned")
    private Date assigned;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 8, nullable = false)
    @NotNull
    @XmlAttribute(name = "type")
    private Type type;

    @Column(name = "desc", nullable = false)
    @NotBlank
    @XmlElement(name = "description")
    private String description;

    @Column(name = "score", nullable = false)
    @Range(min = 0, max = 100)
    @XmlAttribute(name = "score")
    private int score;

    @Temporal(TemporalType.DATE)
    @Column(name = "modified", nullable = false)
    @XmlAttribute(name = "modified")
    private Date modified;

    @Column(name = "modifiedBy", nullable = false)
    @NotBlank
    @XmlAttribute(name = "modifiedby")
    private String modifiedBy;

    private Assignment() {
    }

    public Assignment(Teacher teacher, Type type, String description) {
        this.assigned = new Date();
        this.teacher = teacher;
        this.type = type;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public Date getAssigned() {
        return new Date(assigned.getTime());
    }

    public void setAssigned(Date assigned) {
        this.assigned = assigned;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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
    public int compareTo(Assignment other) {
        return new CompareToBuilder()
                .append(getId(), other.getId())
                .append(getTeacher(), other.getTeacher())
                .append(getAssigned(), other.getAssigned())
                .append(getType(), other.getType())
                .append(getDescription(), other.getDescription())
                .append(getScore(), other.getScore())
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
        Assignment other = (Assignment) object;
        return new EqualsBuilder()
                .append(getId(), other.getId())
                .append(getTeacher(), other.getTeacher())
                .append(getAssigned(), other.getAssigned())
                .append(getType(), other.getType())
                .append(getDescription(), other.getDescription())
                .append(getScore(), other.getScore())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .append(getTeacher())
                .append(getAssigned())
                .append(getType())
                .append(getDescription())
                .append(getScore())
                .hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(getId())
                .append(getTeacher())
                .append(getAssigned())
                .append(getType())
                .append(getDescription())
                .append(getScore())
                .toString();
    }
}
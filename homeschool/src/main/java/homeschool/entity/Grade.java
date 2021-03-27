package homeschool.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Future;
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
@Table(name = "grade")
@EntityListeners({ AuditableEntityListener.class })
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "grade")
public class Grade implements Auditable, Comparable<Grade>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlAttribute(name = "id")
    private long id;

    @Version
    @XmlAttribute(name = "version")
    private int version;

    @Column(name = "year", nullable = false)
    @Range(min = 1, max = 12)
    @XmlAttribute(name = "year")
    private int year;

    @Temporal(TemporalType.DATE)
    @Column(name = "start", nullable = false)
    @XmlAttribute(name = "start")
    private Date start;

    @Temporal(TemporalType.DATE)
    @Column(name = "end", nullable = false)
    @Future
    @XmlAttribute(name = "end")
    private Date end;

    @ManyToOne(optional = false)
    @JoinColumn(name = "school_id")
    @NotNull
    @XmlElement(name = "school")
    private School school;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderBy("name ASC")
    @XmlElement(name = "course")
    private List<Course> courses;

    @Temporal(TemporalType.DATE)
    @Column(name = "modified", nullable = false)
    @XmlAttribute(name = "modified")
    private Date modified;

    @Column(name = "modifiedBy", nullable = false)
    @NotBlank
    @XmlAttribute(name = "modifiedby")
    private String modifiedBy;

    private Grade() {
    }

    public Grade(int year, Date start, Date end, School school) {
        courses = new ArrayList<>();
        this.year = year;
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
        this.school = school;
    }

    public long getId() {
        return id;
    }

    public long getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Date getStart() {
        return new Date(start.getTime());
    }

    public void setStart(Date start) {
        this.start = new Date(start.getTime());
    }

    public Date getEnd() {
        return new Date(end.getTime());
    }

    public void setEnd(Date end) {
        this.end = new Date(end.getTime());
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public boolean addCourse(Course course) {
        return courses.add(course);
    }

    public boolean removeCourse(Course course) {
        return courses.remove(course);
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
    public int compareTo(Grade other) {
        return new CompareToBuilder()
                .append(getId(), other.getId())
                .append(getYear(), other.getYear())
                .append(getStart(), other.getStart())
                .append(getEnd(), other.getEnd())
                .append(getSchool(), other.getSchool())
                .append(getCourses(), other.getCourses())
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
        Grade other = (Grade) object;
        return new EqualsBuilder()
                .append(getId(), other.getId())
                .append(getYear(), other.getYear())
                .append(getStart(), other.getStart())
                .append(getEnd(), other.getEnd())
                .append(getSchool(), other.getSchool())
                .append(getCourses(), other.getCourses())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .append(getYear())
                .append(getStart())
                .append(getEnd())
                .append(getSchool())
                .append(getCourses())
                .hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(getId())
                .append(getYear())
                .append(getStart())
                .append(getEnd())
                .append(getSchool())
                .append(getCourses())
                .toString();
    }
}
package model.beer;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "style")
@NamedQueries({
        @NamedQuery(name = "listStyles", query = "select s from Style s")
})
public class Style implements Comparable<Style>, Serializable {
    private static final long serialVersionUID = 2959111213957417339L;

    @Id
    @Column(length = 64)
    private String name;

    @Version
    private int version;

    private String description;

    protected Style() {
    }

    public Style(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int compareTo(Style otherStyle) {
        return new CompareToBuilder()
                .append(name, otherStyle.getName())
                .toComparison();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        Style otherStyle = (Style) object;
        return new EqualsBuilder()
                .append(name, otherStyle.getName())
                .append(description, otherStyle.getDescription())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(name)
                .append(description)
                .toString();
    }
}
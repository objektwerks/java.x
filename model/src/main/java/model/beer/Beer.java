package model.beer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "beer")
@NamedQueries({
        @NamedQuery(name = "listBeersByStyle", query = "select b from Beer b where b.style = ?1")
})
public class Beer implements Comparable<Beer>, Serializable {
    private static final long serialVersionUID = -3843359309960819387L;

    @Id
    @Column(length = 36)
    private String id;

    @Version
    private int version;

    @Column(length = 64)
    private String name;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "style_id", nullable = false)
    private Style style;

    @Column(precision = 2, scale = 2)
    private BigDecimal abv;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "beer")
    private Set<Review> reviews;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "brewery_id", nullable = false)
    private Brewery brewery;

    protected Beer() {
        this.id = UUID.randomUUID().toString();
    }

    public Beer(String name, Style style, BigDecimal abv, Brewery brewery) {
        this();
        this.name = name;
        this.style = style;
        this.abv = abv;
        this.reviews = new HashSet<Review>();
        this.brewery = brewery;
        brewery.addBeer(this);
    }

    public String getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public Style getStyle() {
        return style;
    }

    public BigDecimal getAbv() {
        return abv;
    }

    public Brewery getBrewery() {
        return brewery;
    }

    public Set<Review> getReviews() {
        return Collections.unmodifiableSet(reviews);
    }

    public Set<Review> addReview(Review review) {
        reviews.add(review);
        return Collections.unmodifiableSet(reviews);
    }

    public int compareTo(Beer otherBeer) {
        return new CompareToBuilder()
                .append(name, otherBeer.getName())
                .append(style, otherBeer.getStyle())
                .toComparison();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        Beer otherBeer = (Beer) object;
        return new EqualsBuilder()
                .append(id, otherBeer.getId())
                .append(name, otherBeer.getName())
                .append(style, otherBeer.getStyle())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(id)
                .append(name)
                .append(style)
                .append(abv)
                .append(reviews)
                .toString();
    }
}
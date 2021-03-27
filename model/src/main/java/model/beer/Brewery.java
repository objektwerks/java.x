package model.beer;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "brewery")
@NamedQueries({
        @NamedQuery(name = "listBreweries", query = "select b from Brewery b left join fetch b.beers")
})
public class Brewery implements Comparable<Brewery>, Serializable {
    private static final long serialVersionUID = 7342470366711136250L;

    @Id
    @Column(length = 36)
    private String id;

    @Version
    private int version;

    @Column(length = 64)
    private String name;

    @Column(length = 64)
    private String city;

    @Column(length = 64)
    private String region;

    @Enumerated(EnumType.STRING)
    private Country country;

    private String website;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "brewery")
    private Set<Beer> beers;

    protected Brewery() {
        this.id = UUID.randomUUID().toString();
    }

    public Brewery(String name, String city, String region, Country country, String website) {
        this();
        this.name = name;
        this.city = city;
        this.region = region;
        this.country = country;
        this.website = website;
        this.beers = new HashSet<Beer>();
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

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public Country getCountry() {
        return country;
    }

    public String getWebsite() {
        return website;
    }

    public Set<Beer> getBeers() {
        return Collections.unmodifiableSet(beers);
    }

    public Set<Beer> addBeer(Beer beer) {
        beers.add(beer);
        return Collections.unmodifiableSet(beers);
    }

    public Set<Beer> removeBeer(Beer beer) {
        beers.remove(beer);
        return Collections.unmodifiableSet(beers);
    }

    public int compareTo(Brewery otherBrewery) {
        return new CompareToBuilder()
                .append(name, otherBrewery.getName())
                .toComparison();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        Brewery otherBrewery = (Brewery) object;
        return new EqualsBuilder()
                .append(id, otherBrewery.getId())
                .append(name, otherBrewery.getName())
                .append(city, otherBrewery.getCity())
                .append(region, otherBrewery.getRegion())
                .append(country, otherBrewery.getCountry())
                .append(website, otherBrewery.getWebsite())
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
                .append(city)
                .append(region)
                .append(country)
                .append(website)
                .append(beers)
                .toString();
    }
}
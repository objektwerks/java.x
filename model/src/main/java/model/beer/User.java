package model.beer;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "user")
public class User implements Comparable<User>, Serializable {
    private static final long serialVersionUID = -3993359309960819433L;

    @Id
    @Column(length = 36)
    private String id;

    @Version
    private int version;

    @Column(length = 64)
    private String name;

    @Column(length = 128)
    private String email;

    @Column(length = 64)
    private String city;

    @Column(length = 64)
    private String region;

    @Enumerated(EnumType.STRING)
    private Country country;

    private Date memberSince;

    private Style favoriteStyle;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Review> reviews;

    protected User() {
        this.id = UUID.randomUUID().toString();
    }

    public User(String name, String city, String region, Country country, Style favoriteStyle) {
        this();
        this.memberSince = new Date();
        this.name = name;
        this.city = city;
        this.region = region;
        this.country = country;
        this.favoriteStyle = favoriteStyle;
        this.reviews = new HashSet<Review>();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Date getMemberSince() {
        return memberSince;
    }

    public Style getFavoriteStyle() {
        return favoriteStyle;
    }

    public void setFavoriteStyle(Style favoriteStyle) {
        this.favoriteStyle = favoriteStyle;
    }

    public Set<Review> getReviews() {
        return Collections.unmodifiableSet(reviews);
    }

    public Set<Review> addReview(Review review) {
        reviews.add(review);
        return Collections.unmodifiableSet(reviews);
    }

    public int compareTo(User otherUser) {
        return new CompareToBuilder()
                .append(name, otherUser.getName())
                .append(email, otherUser.getEmail())
                .append(city, otherUser.getCity())
                .append(region, otherUser.getRegion())
                .append(country, otherUser.getCountry())
                .append(memberSince, otherUser.getMemberSince())
                .append(favoriteStyle, otherUser.getFavoriteStyle())
                .toComparison();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        User otherUser = (User) object;
        return new EqualsBuilder()
                .append(id, otherUser.getId())
                .append(name, otherUser.getName())
                .append(email, otherUser.getEmail())
                .append(city, otherUser.getCity())
                .append(region, otherUser.getRegion())
                .append(country, otherUser.getCountry())
                .append(memberSince, otherUser.getMemberSince())
                .append(favoriteStyle, otherUser.getFavoriteStyle())
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
                .append(email)
                .append(city)
                .append(region)
                .append(country)
                .append(memberSince)
                .append(favoriteStyle)
                .append(reviews)
                .toString();
    }
}
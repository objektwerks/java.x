package model.beer;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "review")
public class Review implements Serializable {
    private static final long serialVersionUID = -3843359309960819433L;

    @Id
    @Column(length = 36)
    private String id;

    @Version
    private int version;

    private String review;

    @Enumerated(EnumType.STRING)
    private Serving serving;

    @Enumerated(EnumType.STRING)
    private Grade look;

    private String lookReview;

    @Enumerated(EnumType.STRING)
    private Grade smell;

    private String smellReview;

    @Enumerated(EnumType.STRING)
    private Grade taste;

    private String tasteReview;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "beer_id", nullable = false)
    private Beer beer;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected Review() {
        this.id = UUID.randomUUID().toString();
    }

    public Review(String review, Beer beer, User user) {
        this();
        this.review = review;
        this.serving = Serving.bottle;
        this.beer = beer;
        beer.addReview(this);
        this.user = user;
        user.addReview(this);
        this.look = Grade.A;
        this.smell = Grade.A;
        this.taste = Grade.A;
    }

    public Review(String review, Beer beer, User user, Grade look, Grade smell, Grade taste) {
        this(review, beer, user);
        this.look = look;
        this.smell = smell;
        this.taste = taste;
    }

    public String getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Serving getServing() {
        return serving;
    }

    public void setServing(Serving serving) {
        this.serving = serving;
    }

    public Grade getLook() {
        return look;
    }

    public void setLook(Grade look) {
        this.look = look;
    }

    public String getLookReview() {
        return lookReview;
    }

    public void setLookReview(String lookReview) {
        this.lookReview = lookReview;
    }

    public Grade getSmell() {
        return smell;
    }

    public void setSmell(Grade smell) {
        this.smell = smell;
    }

    public String getSmellReview() {
        return smellReview;
    }

    public void setSmellReview(String smellReview) {
        this.smellReview = smellReview;
    }

    public Grade getTaste() {
        return taste;
    }

    public void setTaste(Grade taste) {
        this.taste = taste;
    }

    public String getTasteReview() {
        return tasteReview;
    }

    public void setTasteReview(String tasteReview) {
        this.tasteReview = tasteReview;
    }

    public Beer getBeer() {
        return beer;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (object.getClass() != getClass()) return false;
        Review otherReview = (Review) object;
        return new EqualsBuilder()
                .append(id, otherReview.getId())
                .append(review, otherReview.getReview())
                .append(serving, otherReview.getServing())
                .append(look, otherReview.getLook())
                .append(lookReview, otherReview.getLookReview())
                .append(smell, otherReview.getSmell())
                .append(smellReview, otherReview.getSmellReview())
                .append(taste, otherReview.getTaste())
                .append(tasteReview, otherReview.getTasteReview())
                .append(beer, otherReview.getBeer())
                .append(user, otherReview.getUser())
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
                .append(review)
                .append(serving)
                .append(look)
                .append(lookReview)
                .append(smell)
                .append(smellReview)
                .append(taste)
                .append(tasteReview)
                .append(beer)
                .append(user)
                .toString();
    }
}
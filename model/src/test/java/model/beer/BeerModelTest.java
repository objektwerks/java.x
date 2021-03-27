package model.beer;

import java.math.BigDecimal;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import persistence.jpa.JpaGenericEntityManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test.context.xml"})
@TransactionConfiguration()
@Transactional(propagation = Propagation.REQUIRED)
public class BeerModelTest {
    @Resource(name = "entityManager") private JpaGenericEntityManager entityManager;

    @Test
    public void persistStyles() {
        Style style = new Style("American IPA", "Hoppy!");
        style = entityManager.persist(style);
        Style copy = entityManager.findById(Style.class, style.getName());
        assertEquals(style, copy);
        assertTrue(entityManager.listByNamedQuery("listStyles").size() > 0);
    }

    @Test
    public void persistBeers() {
        Style style = new Style("American IPA", "Hoppy!");
        style = entityManager.persist(style);
        Brewery brewery = new Brewery("Stone", "Escondido", "California", Country.US, "http://www.stonebrew.com/");
        Beer beer = new Beer("Stone IPA", style, new BigDecimal(6.2), brewery);
        entityManager.persist(beer);
        Beer copy = entityManager.findById(Beer.class, beer.getId());
        assertEquals(beer, copy);
        assertTrue(entityManager.listByNamedValueQuery("listBeersByStyle", style).size() > 0);
    }

    @Test
    public void persistBreweries() {
        Brewery brewery = new Brewery("Stone", "Escondido", "California", Country.US, "http://www.stonebrew.com/");
        brewery = entityManager.persist(brewery);
        Brewery copy = entityManager.findById(Brewery.class, brewery.getId());
        assertEquals(brewery, copy);
        assertTrue(entityManager.listByNamedQuery("listBreweries").size() > 0);
    }

    @Test
    public void persistReviews() {
        Style style = new Style("American IPA", "Hoppy!");
        style = entityManager.persist(style);
        Brewery brewery = new Brewery("Stone", "Escondido", "California", Country.US, "http://www.stonebrew.com/");
        Beer beer = new Beer("Stone IPA", style, new BigDecimal(6.2), brewery);
        entityManager.persist(beer);
        User user = new User("Joe Case", "Tampa", "FL", Country.US, style);
        entityManager.persist(user);
        Review review = new Review("Great beer!", beer, user, Grade.A, Grade.A, Grade.A);
        entityManager.persist(review);
        Review copy = entityManager.findById(Review.class, review.getId());
        assertEquals(review, copy);
    }

    public void persistUsers() {
        Style style = new Style("American IPA", "Hoppy!");
        style = entityManager.persist(style);
        User user = new User("Joe Case", "Tampa", "FL", Country.US, style);
        entityManager.persist(user);
        User copy = entityManager.findById(User.class, user.getId());
        assertEquals(user, copy);
    }

    @Test
    public void persistAll() {
        Style style = new Style("American IPA", "Hoppy!");
        style = entityManager.persist(style);
        Brewery brewery = new Brewery("Stone", "Escondido", "California", Country.US, "http://www.stonebrew.com/");
        Beer beer = new Beer("Stone IPA", style, new BigDecimal(6.2), brewery);
        entityManager.persist(beer);
        User user = new User("Joe Case", "Tampa", "FL", Country.US, style);
        entityManager.persist(user);
        Review review = new Review("Great beer!", beer, user, Grade.A, Grade.A, Grade.A);
        entityManager.persist(review);
    }
}
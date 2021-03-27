package persistence.jpa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test.context.xml"})
@Transactional(propagation = Propagation.REQUIRED)
public class PingTest {
    @Resource(name = "entityManager") private JpaGenericEntityManager entityManager;

    @Test
    public void persist() {
        entityManager.persist(new Ping("ping"));
        Set<Ping> pings = new HashSet<Ping> (2);
        pings.add(new Ping("ping"));
        pings.add(new Ping("ping"));
        entityManager.persist(pings);
        entityManager.persist(new Ping("ping"), new Ping("ping"));
    }

    @Test
    public void merge() {
        Ping ping = new Ping("ping");
        entityManager.persist(ping);
        ping.setValue("ping pong");
        entityManager.merge(ping);
        ping.setValue("pong ping");
        Set<Ping> pings = new HashSet<Ping> (1);
        pings.add(ping);
        entityManager.merge(pings);
        ping.setValue("ping pong ping");
        entityManager.merge(ping, new Ping("ping"));
    }

    @Test
    public void remove() {
        Ping ping = new Ping("ping");
        entityManager.persist(ping);
        entityManager.remove(Ping.class, ping.getId());
        ping = new Ping("ping");
        entityManager.persist(ping);
        Set<Serializable> ids = new HashSet<Serializable>(1);
        ids.add(ping.getId());
        entityManager.remove(Ping.class, ids);
        ping = new Ping("ping");
        entityManager.persist(ping);
        entityManager.remove(ping);        
    }

    @Test
    public void getById() {
        Ping ping = new Ping("ping");
        entityManager.persist(ping);
        assertNotNull("Ping is null.", entityManager.getById(Ping.class, ping.getId()));
    }

    @Test
    public void findById() {
        Ping ping = new Ping("ping");
        entityManager.persist(ping);
        assertNotNull(entityManager.findById(Ping.class, ping.getId()));
    }

    @Test
    public void findByQuery() {
        entityManager.persist(new Ping("ping"));
        assertNotNull(entityManager.findByQuery("select p from Ping p where p.value = 'ping'"));
    }
    
    @Test
    public void findByValueQuery() {
        Ping ping = new Ping("ping");
        entityManager.persist(ping);
        assertNotNull(entityManager.findByValueQuery("select p from Ping p where p.id = ?1", ping.getId()));
    }

    @Test
    public void findByNamedQuery() {
        entityManager.persist(new Ping("ping"));
        assertNotNull(entityManager.findByNamedQuery("find"));
    }

    @Test
    public void findByNamedValueQuery() {
        Ping ping = new Ping("ping");
        entityManager.persist(ping);
        assertNotNull(entityManager.findByNamedValueQuery("findById", ping.getId()));
    }

    @Test
    public void listByNamedQuery() {
        Ping ping = new Ping("ping");
        entityManager.persist(ping);
        assertTrue(!entityManager.listByNamedQuery("list").isEmpty());
    }

    @Test
    public void listByNamedValueQuery() {
        Ping ping = new Ping("ping");
        entityManager.persist(ping);
        assertTrue(!entityManager.listByNamedValueQuery("listById", ping.getId()).isEmpty());
    }

    @Test
    public void listByNamedParameterQuery() {
        Ping ping = new Ping("ping");
        entityManager.persist(ping);
        Map<String, Object> parameters = new HashMap<String, Object>(1);
        parameters.put("id", ping.getId());
        assertTrue(!entityManager.listByNamedParameterQuery("listByParameterId", parameters).isEmpty());        
    }

    @Test
    public void execute() {
        entityManager.execute(new JpaCallback() {
            public Object doInJpa(EntityManager entityManager) throws PersistenceException {
                Ping ping = new Ping("ping");
                entityManager.persist(ping);
                return entityManager.find(Ping.class, ping.getId());
            }
        });
    }
}
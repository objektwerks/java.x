package persistence.hibernate;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.orm.hibernate3.HibernateCallback;
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
    @Resource(name = "entityManager") private HibernateGenericEntityManager entityManager;

    @Test
    public void save() {
        Ping ping = new Ping("ping");
        entityManager.save(ping);
        ping.setValue("ping pong");
        entityManager.save(ping);
        Set<Ping> pings = new HashSet<Ping>(2);
        pings.add(new Ping("ping"));
        pings.add(new Ping("ping"));
        entityManager.save(pings);
    }

    @Test
    public void delete() {
        Ping ping = new Ping("ping");
        entityManager.save(ping);
        entityManager.delete(ping);
        ping = new Ping("ping");
        entityManager.save(ping);
        entityManager.delete(Ping.class, ping.getId());
    }

    @Test
    public void find() {
        Ping ping = new Ping("ping");
        entityManager.save(ping);
        assertNotNull(entityManager.find(Ping.class, ping.getId()));
    }

    @Test
    public void list() {
        Ping ping = new Ping("ping");
        entityManager.save(ping);
        assertNotNull(!entityManager.list(Ping.class).isEmpty());
    }

    @Test
    public void query() {
        Ping ping = new Ping("ping");
        entityManager.save(ping);
        assertNotNull(!entityManager.list(Ping.class).isEmpty());
        assertTrue(!entityManager.query("list").isEmpty());
        assertTrue(!entityManager.query("findById", ping.getId()).isEmpty());
    }

    @Test
    public void execute() {
        entityManager.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Ping ping = new Ping("ping");
                session.save(ping);
                return session.load(Ping.class, ping.getId());
            }
        });
    }
}
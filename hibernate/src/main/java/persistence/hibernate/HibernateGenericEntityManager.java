package persistence.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateGenericEntityManager {
    private HibernateTemplate hibernateTemplate;

    public HibernateGenericEntityManager() {
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public Object execute(HibernateCallback callback) {
        return hibernateTemplate.execute(callback);
    }

    public <E> void save(E entity) {
        hibernateTemplate.saveOrUpdate(entity);
    }

    public <E> void save(Collection<E> entities) {
        hibernateTemplate.saveOrUpdateAll(entities);
    }

    public <E> void delete(E entity) {
        hibernateTemplate.delete(entity);
    }

    public <T> void delete(Class<T> type, Serializable id) {
        delete(find(type, id));
    }

    @SuppressWarnings("unchecked")
    public <E> E find(Class type, Serializable id) {
        return (E) hibernateTemplate.get(type, id);
    }

    @SuppressWarnings("unchecked")
    public <E> List<E> list(Class type) {
        return hibernateTemplate.loadAll(type);
    }

    @SuppressWarnings("unchecked")
    public <E> List<E> query(String namedQuery) {
        return hibernateTemplate.findByNamedQuery(namedQuery);
    }

    @SuppressWarnings("unchecked")
    public <E> List<E> query(String namedQuery, Object... values) {
        return hibernateTemplate.findByNamedQuery(namedQuery, values);
    }
}
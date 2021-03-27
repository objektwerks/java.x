package persistence.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JpaGenericEntityManager {
    private JpaTemplate jpaTemplate;

    public JpaGenericEntityManager() {
    }

    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        this.jpaTemplate = jpaTemplate;
    }

    public Object execute(JpaCallback jpaCallback) {
        return jpaTemplate.execute(jpaCallback);
    }

    public <E> E persist(E entity) {
        jpaTemplate.persist(entity);
        return entity;
    }

    public <E> void persist(Collection<E> entities) {
        for (E entity : entities) {
            jpaTemplate.persist(entity);
        }
    }

    public <E> void persist(E... entities) {
        for (E entity : entities) {
            jpaTemplate.persist(entity);
        }
    }

    public <E> E merge(E entity) {
        return jpaTemplate.merge(entity);
    }

    public <E> Collection<E> merge(Collection<E> entities) {
        Collection<E> mergedEntities = new ArrayList<E>();
        for (E entity : entities) {
            mergedEntities.add(jpaTemplate.merge(entity));
        }
        return mergedEntities;
    }

    public <E> Collection<E> merge(E... entities) {
        Collection<E> mergedEntities = new ArrayList<E>();
        for (E entity : entities) {
            mergedEntities.add(jpaTemplate.merge(entity));
        }
        return mergedEntities;
    }

    public <E> void remove(E entity) {
        jpaTemplate.remove(entity);
    }

    public <T> void remove(Class<T> type, Serializable id) {
        jpaTemplate.remove(jpaTemplate.getReference(type, id));
    }

    public <T> void remove(Class<T> type, Set<Serializable> ids) {
        for (Serializable id : ids) {
            remove(type, id);
        }
    }

    public <T> T getById(Class<T> type, Serializable id) {
        return jpaTemplate.getReference(type, id);
    }

    public <T> T findById(Class<T> type, Serializable id) {
        return jpaTemplate.find(type, id);
    }

    @SuppressWarnings("unchecked")
    public <E> E findByQuery(final String query) {
        return (E) execute(new JpaCallback() {
            public Object doInJpa(EntityManager entityManager) throws PersistenceException {
                return entityManager.createQuery(query).getSingleResult();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <E> E findByValueQuery(final String query, final Object... values) {
        return (E) execute(new JpaCallback() {
            public Object doInJpa(EntityManager entityManager) throws PersistenceException {
                Query q = entityManager.createQuery(query);
                addQueryParameters(q, values);
                return q.getSingleResult();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <E> E findByNamedQuery(final String queryName) {
        return (E) execute(new JpaCallback() {
            public Object doInJpa(EntityManager entityManager) throws PersistenceException {
                return entityManager.createNamedQuery(queryName).getSingleResult();
            }
        });
     }

    @SuppressWarnings("unchecked")
    public <E> E findByNamedValueQuery(final String queryName, final Object... values) {
        return (E) execute(new JpaCallback() {
            public Object doInJpa(EntityManager entityManager) throws PersistenceException {
                Query query = entityManager.createNamedQuery(queryName);
                addQueryParameters(query, values);
                return query.getSingleResult();
            }
        });
     }

    @SuppressWarnings("unchecked")
    public <E> List<E> listByNamedQuery(String queryName) {
        return jpaTemplate.findByNamedQuery(queryName);
    }

    @SuppressWarnings("unchecked")
    public <E> List<E> listByNamedValueQuery(String queryName, Object... values) {
        return jpaTemplate.findByNamedQuery(queryName, values);
    }

    @SuppressWarnings("unchecked")
    public <E> List<E> listByNamedParameterQuery(String queryName, Map<String, Object> parameters) {
        return jpaTemplate.findByNamedQueryAndNamedParams(queryName, parameters);
    }

    private void addQueryParameters(Query query, Object... values) {
        int position = 1;
        for (Object value : values) {
            query.setParameter(position, value);
            ++position;
        }
    }
}
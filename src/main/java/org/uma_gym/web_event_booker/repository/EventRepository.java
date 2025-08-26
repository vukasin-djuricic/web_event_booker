package org.uma_gym.web_event_booker.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import org.uma_gym.web_event_booker.model.Event;
import org.uma_gym.web_event_booker.util.JPAUtil;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EventRepository {

    // da ne bi bilo lazy koristimo join fetch
    public List<Event> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // JOIN FETCH govori Hibernate-u da odmah učita i podatke za author-a i category-u
            // DISTINCT sprečava duplikate u rezultatu koji mogu nastati zbog join-ova
            return em.createQuery(
                    "SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.author LEFT JOIN FETCH e.category LEFT JOIN FETCH e.tags",
                    Event.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    // same, zbog lazy
    public Optional<Event> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Event event = em.createQuery(
                    "SELECT e FROM Event e LEFT JOIN FETCH e.author LEFT JOIN FETCH e.category LEFT JOIN FETCH e.tags WHERE e.id = :id",
                    Event.class
            ).setParameter("id", id).getSingleResult();
            return Optional.of(event);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    public Event save(Event event) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(event);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
        return event;
    }
}
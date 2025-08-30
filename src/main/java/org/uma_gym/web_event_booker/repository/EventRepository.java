// src/main/java/org/uma_gym/web_event_booker/repository/EventRepository.java

package org.uma_gym.web_event_booker.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import org.uma_gym.web_event_booker.model.Event;
import org.uma_gym.web_event_booker.util.JPAUtil;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EventRepository {

    public List<Event> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.author LEFT JOIN FETCH e.category LEFT JOIN FETCH e.tags",
                    Event.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

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

    // ... metode save() i deleteById() ostaju iste ...

    public Event save(Event event) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (event.getId() == null) {
                em.persist(event);
            } else {
                em.merge(event);
            }
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

    public void deleteById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Event event = em.find(Event.class, id);
            if (event != null) {
                em.createQuery("DELETE FROM Comment c WHERE c.event.id = :eventId")
                        .setParameter("eventId", id)
                        .executeUpdate();
                em.remove(event);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Event> findByCategoryId(Long categoryId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.author LEFT JOIN FETCH e.category LEFT JOIN FETCH e.tags WHERE e.category.id = :categoryId ORDER BY e.datumOdrzavanja DESC",
                    Event.class
            ).setParameter("categoryId", categoryId).getResultList();
        } finally {
            em.close();
        }
    }

}
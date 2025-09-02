// src/main/java/org/uma_gym/web_event_booker/repository/EventRepository.java

package org.uma_gym.web_event_booker.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import org.uma_gym.web_event_booker.model.Event;
import org.uma_gym.web_event_booker.util.JPAUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EventRepository {

    public List<Event> findRelatedEvents(Long currentEventId, List<Long> tagIds) {
        // Ako događaj nema tagove, ne možemo naći srodne
        if (tagIds == null || tagIds.isEmpty()) {
            return new ArrayList<>();
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT DISTINCT e FROM Event e JOIN e.tags t " +
                                    "WHERE e.id != :currentEventId AND t.id IN :tagIds " + // Uslovi: nije trenutni dog. I ima bar jedan od tagova
                                    "ORDER BY e.vremeKreiranja DESC", // Sortiramo po najnovijim
                            Event.class)
                    .setParameter("currentEventId", currentEventId)
                    .setParameter("tagIds", tagIds)
                    .setMaxResults(3) // Ograničavamo na 3 rezultata
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Event> findTopReactedEvents() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT e FROM Event e " +
                                    "LEFT JOIN FETCH e.author " +
                                    "LEFT JOIN FETCH e.category " +
                                    "LEFT JOIN FETCH e.tags " +
                                    "ORDER BY (e.likeCount + e.dislikeCount) DESC, e.vremeKreiranja DESC", // Sortiranje po sumi reakcija
                            Event.class)
                    .setMaxResults(3) // Ograničavamo na top 3
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Event> findMostVisitedInLast30Days() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

            return em.createQuery(
                            "SELECT e FROM Event e " +
                                    "LEFT JOIN FETCH e.author " +
                                    "LEFT JOIN FETCH e.category " +
                                    "LEFT JOIN FETCH e.tags " +
                                    "WHERE e.vremeKreiranja >= :thirtyDaysAgo " + // Uslov za poslednjih 30 dana
                                    "ORDER BY e.brojPoseta DESC, e.vremeKreiranja DESC", // Sortiranje po posetama, pa po datumu
                            Event.class)
                    .setParameter("thirtyDaysAgo", thirtyDaysAgo)
                    .setMaxResults(10) // Ograničavamo na top 10
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Event> findAll(int page, int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.author LEFT JOIN FETCH e.category LEFT JOIN FETCH e.tags ORDER BY e.vremeKreiranja DESC", Event.class)
                    .setFirstResult((page - 1) * limit)
                    .setMaxResults(limit)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    public long countAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(e) FROM Event e", Long.class).getSingleResult();
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
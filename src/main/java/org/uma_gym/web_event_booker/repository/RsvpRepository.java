// src/main/java/org/uma_gym/web_event_booker/repository/RsvpRepository.java
package org.uma_gym.web_event_booker.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.uma_gym.web_event_booker.model.Rsvp;
import org.uma_gym.web_event_booker.util.JPAUtil;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RsvpRepository {

    public Rsvp save(Rsvp rsvp) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(rsvp);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
        return rsvp;
    }

    public List<Rsvp> findByEventId(Long eventId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT r FROM Rsvp r WHERE r.event.id = :eventId ORDER BY r.rsvpDate ASC", Rsvp.class)
                    .setParameter("eventId", eventId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public long countByEventId(Long eventId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(r) FROM Rsvp r WHERE r.event.id = :eventId", Long.class)
                    .setParameter("eventId", eventId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public Optional<Rsvp> findByEventIdAndUserIdentifier(Long eventId, String userIdentifier) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Rsvp rsvp = em.createQuery("SELECT r FROM Rsvp r WHERE r.event.id = :eventId AND r.userIdentifier = :userIdentifier", Rsvp.class)
                    .setParameter("eventId", eventId)
                    .setParameter("userIdentifier", userIdentifier)
                    .getSingleResult();
            return Optional.of(rsvp);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}
package org.uma_gym.web_event_booker.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import org.uma_gym.web_event_booker.model.Comment;
import org.uma_gym.web_event_booker.util.JPAUtil;
import java.util.List;

@ApplicationScoped
public class CommentRepository {

    public List<Comment> findByEventId(Long eventId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Comment c WHERE c.event.id = :eventId ORDER BY c.datumKreiranja DESC", Comment.class)
                    .setParameter("eventId", eventId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Comment save(Comment comment) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(comment);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
        return comment;
    }
}
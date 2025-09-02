package org.uma_gym.web_event_booker.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import org.uma_gym.web_event_booker.model.Comment;
import org.uma_gym.web_event_booker.util.JPAUtil;
import java.util.List;
import java.util.Optional;

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

    public Optional<Comment> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Comment.class, id));
        } finally {
            em.close();
        }
    }


    public Comment save(Comment comment) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            // Ako ID postoji, radi merge (update), ako ne, radi persist (create)
            if (comment.getId() == null) {
                em.persist(comment);
            } else {
                em.merge(comment);
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
        return comment;
    }
}
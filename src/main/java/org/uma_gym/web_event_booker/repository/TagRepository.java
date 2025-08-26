package org.uma_gym.web_event_booker.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import org.uma_gym.web_event_booker.model.Tag;
import org.uma_gym.web_event_booker.util.JPAUtil;

import java.util.List;

@ApplicationScoped
public class TagRepository {

    public List<Tag> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Tag t", Tag.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Tag> findByIds(List<Long> ids) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM Tag t WHERE t.id IN :ids", Tag.class)
                    .setParameter("ids", ids)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Tag save(Tag tag) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (tag.getId() == null) {
                em.persist(tag);
            } else {
                em.merge(tag);
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
        return tag;
    }
}
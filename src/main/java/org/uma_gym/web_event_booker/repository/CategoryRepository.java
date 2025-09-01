package org.uma_gym.web_event_booker.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import org.uma_gym.web_event_booker.model.Category;
import org.uma_gym.web_event_booker.util.JPAUtil;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CategoryRepository {

    // Zamenite staru findAll() sa ovom
    public List<Category> findAll(int page, int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Category c ORDER BY c.name", Category.class)
                    .setFirstResult((page - 1) * limit) // Odakle da počne (npr. (2-1)*10 = 10)
                    .setMaxResults(limit)              // Koliko rezultata da vrati
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Dodajte ovu novu metodu
    public long countAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(c) FROM Category c", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    public Optional<Category> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Category.class, id));
        } finally {
            em.close();
        }
    }

    public Category save(Category category) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (category.getId() == null) {
                em.persist(category); // Kreira novi zapis
            } else {
                em.merge(category); // Ažurira postojeći
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e; // Prosledi grešku dalje
        } finally {
            em.close();
        }
        return category;
    }

    public void deleteById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Category category = em.find(Category.class, id);
            if (category != null) {
                em.remove(category);
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
}
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
            // IZMENA: Ako ID postoji, radi merge (update), ako ne, radi persist (create)
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

    // --- NOVA METODA ---
    public void deleteById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Event event = em.find(Event.class, id);
            if (event != null) {
                // Pre brisanja događaja, moramo obrisati sve komentare vezane za njega
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
    public List<Event> search(String query) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // IZMENA 1: Uklonili smo LOWER() oko :query parametra
            String searchQuery = "SELECT DISTINCT e FROM Event e " +
                    "LEFT JOIN FETCH e.author " +
                    "LEFT JOIN FETCH e.category " +
                    "LEFT JOIN FETCH e.tags " +
                    "WHERE LOWER(e.naslov) LIKE :query OR LOWER(e.opis) LIKE :query " + // Uklonjen LOWER() oko :query
                    "ORDER BY e.datumOdrzavanja DESC";

            return em.createQuery(searchQuery, Event.class)
                    // IZMENA 2: Prebacujemo query string u mala slova pre postavljanja parametra
                    .setParameter("query", "%" + query.toLowerCase() + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
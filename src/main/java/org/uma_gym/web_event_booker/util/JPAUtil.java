package org.uma_gym.web_event_booker.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("rafEventBookerPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
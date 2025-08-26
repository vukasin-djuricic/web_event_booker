package org.uma_gym.web_event_booker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider // Ova anotacija je ključna! Govori JAX-RS-u da koristi ovu klasu.
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper objectMapper;

    public ObjectMapperContextResolver() {
        this.objectMapper = new ObjectMapper();
        // Registrujemo modul koji "uči" ObjectMapper kako da radi sa LocalDateTime
        this.objectMapper.registerModule(new JavaTimeModule());
        // Opciono: Lepše formatiranje datuma u izlaznom JSON-u (npr. "2025-10-20T10:00:00")
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}
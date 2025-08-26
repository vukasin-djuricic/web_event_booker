package org.uma_gym.web_event_booker.controller;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotacija koja se koristi za označavanje JAX-RS resursa koji zahtevaju
 * da ulogovani korisnik ima jednu od navedenih uloga.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RolesAllowed {
    String[] value(); // Niz stringova koji predstavlja dozvoljene uloge
}
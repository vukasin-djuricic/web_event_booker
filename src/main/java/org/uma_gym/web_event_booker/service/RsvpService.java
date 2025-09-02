// src/main/java/org/uma_gym/web_event_booker/service/RsvpService.java
package org.uma_gym.web_event_booker.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.uma_gym.web_event_booker.controller.dto.RsvpCreateDTO;
import org.uma_gym.web_event_booker.model.Event;
import org.uma_gym.web_event_booker.model.Rsvp;
import org.uma_gym.web_event_booker.repository.EventRepository;
import org.uma_gym.web_event_booker.repository.RsvpRepository;

@ApplicationScoped
public class RsvpService {

    @Inject private RsvpRepository rsvpRepository;
    @Inject private EventRepository eventRepository;

    public Rsvp createRsvp(Long eventId, RsvpCreateDTO dto) {
        // 1. Pronađi događaj
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new WebApplicationException("Događaj nije pronađen.", Response.Status.NOT_FOUND));

        // 2. Proveri da li događaj uopšte ima ograničen kapacitet
        if (event.getMaxKapacitet() == null) {
            throw new WebApplicationException("Ovaj događaj ne podržava prijave.", Response.Status.BAD_REQUEST);
        }

        // 3. Proveri da li je korisnik već prijavljen
        if (rsvpRepository.findByEventIdAndUserIdentifier(eventId, dto.getUserIdentifier()).isPresent()) {
            throw new WebApplicationException("Već ste prijavljeni za ovaj događaj.", Response.Status.CONFLICT);
        }

        // 4. Proveri da li je kapacitet popunjen
        long currentRsvpCount = rsvpRepository.countByEventId(eventId);
        if (currentRsvpCount >= event.getMaxKapacitet()) {
            throw new WebApplicationException("Kapacitet za ovaj događaj je popunjen.", Response.Status.CONFLICT);
        }

        // 5. Kreiraj i sačuvaj novu prijavu
        Rsvp newRsvp = new Rsvp();
        newRsvp.setEvent(event);
        newRsvp.setUserIdentifier(dto.getUserIdentifier());

        return rsvpRepository.save(newRsvp);
    }

    public long getRsvpCount(Long eventId) {
        // Proveravamo da li događaj postoji pre nego što vratimo broj prijava
        eventRepository.findById(eventId)
                .orElseThrow(() -> new WebApplicationException("Događaj nije pronađen.", Response.Status.NOT_FOUND));
        return rsvpRepository.countByEventId(eventId);
    }
}
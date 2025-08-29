package org.uma_gym.web_event_booker.service;

import com.auth0.jwt.interfaces.DecodedJWT; // Dodaj import
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException; // Dodaj import
import jakarta.ws.rs.NotFoundException;
import org.uma_gym.web_event_booker.controller.dto.EventCreateDTO;
import org.uma_gym.web_event_booker.model.*; // Dodaj import
import org.uma_gym.web_event_booker.repository.CategoryRepository;
import org.uma_gym.web_event_booker.repository.EventRepository;
import org.uma_gym.web_event_booker.repository.TagRepository;
import org.uma_gym.web_event_booker.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EventService {

    @Inject private EventRepository eventRepository;
    @Inject private UserRepository userRepository;
    @Inject private CategoryRepository categoryRepository;
    @Inject private TagRepository tagRepository;
    @Inject private JwtService jwtService; // Dodaj JwtService

    // ... getAllEvents, getEventById, createEvent ostaju isti ...
    public List<Event> getAllEvents() { return eventRepository.findAll(); }
    public Optional<Event> getEventById(Long id) { return eventRepository.findById(id); }
    public Event createEvent(EventCreateDTO dto) {
        User author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Autor sa ID " + dto.getAuthorId() + " nije pronađen."));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Kategorija sa ID " + dto.getCategoryId() + " nije pronađena."));
        List<Tag> tags = tagRepository.findByIds(dto.getTagIds());
        if (tags.size() != dto.getTagIds().size()) { throw new NotFoundException("Jedan ili više tagova nisu pronađeni."); }
        Event event = new Event();
        event.setNaslov(dto.getNaslov());
        event.setOpis(dto.getOpis());
        event.setLokacija(dto.getLokacija());
        event.setDatumOdrzavanja(dto.getDatumOdrzavanja());
        event.setMaxKapacitet(dto.getMaxKapacitet());
        event.setAuthor(author);
        event.setCategory(category);
        event.setTags(tags);
        return eventRepository.save(event);
    }

    // --- NOVA METODA ZA UPDATE ---
    public Event updateEvent(Long eventId, EventCreateDTO dto, String token) {
        // 1. Pronađi događaj koji treba izmeniti
        Event eventToUpdate = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Događaj sa ID " + eventId + " nije pronađen."));

        // 2. Proveri dozvole (autorizacija)
        checkPermissions(eventToUpdate, token);

        // 3. Pronađi povezane entitete (ako su se promenili)
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Kategorija sa ID " + dto.getCategoryId() + " nije pronađena."));
        List<Tag> tags = tagRepository.findByIds(dto.getTagIds());

        // 4. Ažuriraj polja na postojećem događaju
        eventToUpdate.setNaslov(dto.getNaslov());
        eventToUpdate.setOpis(dto.getOpis());
        eventToUpdate.setLokacija(dto.getLokacija());
        eventToUpdate.setDatumOdrzavanja(dto.getDatumOdrzavanja());
        eventToUpdate.setMaxKapacitet(dto.getMaxKapacitet());
        eventToUpdate.setCategory(category);
        eventToUpdate.setTags(tags);
        // Ne menjamo autora niti vreme kreiranja

        return eventRepository.save(eventToUpdate);
    }

    // --- NOVA METODA ZA DELETE ---
    public void deleteEvent(Long eventId, String token) {
        Event eventToDelete = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Događaj sa ID " + eventId + " nije pronađen."));

        // Proveri dozvole pre brisanja
        checkPermissions(eventToDelete, token);

        eventRepository.deleteById(eventId);
    }

    // --- NOVA POMOĆNA METODA ZA PROVERU DOZVOLA ---
    private void checkPermissions(Event event, String token) {
        try {
            DecodedJWT decodedJWT = jwtService.validateToken(token);
            Long userIdFromToken = jwtService.getUserId(decodedJWT);
            UserType userRole = jwtService.getUserRole(decodedJWT)
                    .orElseThrow(() -> new ForbiddenException("Invalid role in token."));

            // Dozvola je data ako je korisnik ADMIN ILI ako je ID korisnika iz tokena
            // jednak ID-ju autora događaja.
            if (userRole != UserType.ADMIN && !event.getAuthor().getId().equals(userIdFromToken)) {
                throw new ForbiddenException("You do not have permission to modify this event.");
            }
        } catch (Exception e) {
            // Pretvori bilo koju grešku sa tokenom u Forbidden izuzetak
            throw new ForbiddenException("Permission check failed: " + e.getMessage());
        }
    }

    // --- NOVA METODA ZA PRETRAGU (FILTRIRANJE U MEMORIJI) ---
    public List<Event> searchEvents(String query) {
        if (query == null || query.trim().isEmpty()) {
            return eventRepository.findAll();
        }

        // 1. Dohvati SVE događaje iz baze
        List<Event> allEvents = eventRepository.findAll();

        // Pripremi string za pretragu (pretvori ga u mala slova)
        String lowerCaseQuery = query.toLowerCase();

        // 2. Filtriraj listu u memoriji koristeći Java Stream
        return allEvents.stream()
                .filter(event ->
                        // Proveri da li naslov ili opis sadrže traženi pojam (case-insensitive)
                        (event.getNaslov() != null && event.getNaslov().toLowerCase().contains(lowerCaseQuery)) ||
                                (event.getOpis() != null && event.getOpis().toLowerCase().contains(lowerCaseQuery))
                )
                .collect(java.util.stream.Collectors.toList());
    }
}
package org.uma_gym.web_event_booker.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.uma_gym.web_event_booker.controller.dto.EventCreateDTO;
import org.uma_gym.web_event_booker.model.Category;
import org.uma_gym.web_event_booker.model.Event;
import org.uma_gym.web_event_booker.model.Tag;
import org.uma_gym.web_event_booker.model.User;
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

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Event createEvent(EventCreateDTO dto) {
        // Pronalazimo povezane entitete
        User author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Autor sa ID " + dto.getAuthorId() + " nije pronađen."));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Kategorija sa ID " + dto.getCategoryId() + " nije pronađena."));

        List<Tag> tags = tagRepository.findByIds(dto.getTagIds());
        if (tags.size() != dto.getTagIds().size()) {
            throw new NotFoundException("Jedan ili više tagova nisu pronađeni.");
        }

        // Kreiramo novi Event objekat
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
}
package org.uma_gym.web_event_booker.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.uma_gym.web_event_booker.controller.dto.CommentCreateDTO;
import org.uma_gym.web_event_booker.model.Comment;
import org.uma_gym.web_event_booker.model.Event;
import org.uma_gym.web_event_booker.repository.CommentRepository;
import org.uma_gym.web_event_booker.repository.EventRepository;
import java.util.List;

@ApplicationScoped
public class CommentService {

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private EventRepository eventRepository;

    public List<Comment> getCommentsForEvent(Long eventId) {
        // Prvo proveravamo da li događaj uopšte postoji
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Događaj sa ID " + eventId + " nije pronađen."));

        return commentRepository.findByEventId(eventId);
    }

    public Comment createCommentForEvent(Long eventId, CommentCreateDTO dto) {
        // 1. Pronađi događaj za koji se vezuje komentar
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Događaj sa ID " + eventId + " nije pronađen."));

        // 2. Kreiraj novi objekat komentara
        Comment comment = new Comment();
        comment.setImeAutora(dto.getImeAutora());
        comment.setTekstKomentara(dto.getTekstKomentara());
        comment.setEvent(event); // 3. Poveži komentar sa događajem

        // 4. Sačuvaj komentar u bazi
        return commentRepository.save(comment);
    }
}
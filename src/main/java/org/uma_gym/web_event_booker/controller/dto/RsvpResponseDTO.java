package org.uma_gym.web_event_booker.controller.dto;

import org.uma_gym.web_event_booker.model.Rsvp;
import java.time.LocalDateTime;

public class RsvpResponseDTO {
    private Long id;
    private String userIdentifier;
    private LocalDateTime rsvpDate;

    public RsvpResponseDTO(Rsvp rsvp) {
        this.id = rsvp.getId();
        this.userIdentifier = rsvp.getUserIdentifier();
        this.rsvpDate = rsvp.getRsvpDate();
    }

    public Long getId() { return id; }
    public String getUserIdentifier() { return userIdentifier; }
    public LocalDateTime getRsvpDate() { return rsvpDate; }

}

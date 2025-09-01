// src/main/java/org/uma_gym/web_event_booker/model/Rsvp.java
package org.uma_gym.web_event_booker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Table(name = "rsvps")
public class Rsvp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Korisnički identifikator je obavezan.")
    @Column(nullable = false)
    private String userIdentifier; // Email ili neko drugo ime koje korisnik unese

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private LocalDateTime rsvpDate = LocalDateTime.now();

    // Konstruktori
    public Rsvp() {}

    // Getteri i Setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserIdentifier() { return userIdentifier; }
    public void setUserIdentifier(String userIdentifier) { this.userIdentifier = userIdentifier; }
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
    public LocalDateTime getRsvpDate() { return rsvpDate; }
    public void setRsvpDate(LocalDateTime rsvpDate) { this.rsvpDate = rsvpDate; }
}
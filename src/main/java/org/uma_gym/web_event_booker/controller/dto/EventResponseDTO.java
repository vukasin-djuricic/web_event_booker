package org.uma_gym.web_event_booker.controller.dto;

import org.uma_gym.web_event_booker.model.Event;
import org.uma_gym.web_event_booker.model.Tag;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class EventResponseDTO {
    private Long id;
    private String naslov;
    private String opis;
    private LocalDateTime datumOdrzavanja;
    private String lokacija;
    private UserResponseDTO author; // Koristimo DTO da ne prikažemo lozinku
    private String categoryName;
    private List<String> tags;

    public EventResponseDTO(Event event) {
        this.id = event.getId();
        this.naslov = event.getNaslov();
        this.opis = event.getOpis();
        this.datumOdrzavanja = event.getDatumOdrzavanja();
        this.lokacija = event.getLokacija();
        this.author = new UserResponseDTO(event.getAuthor());
        this.categoryName = event.getCategory().getName();
        this.tags = event.getTags().stream().map(Tag::getNaziv).collect(Collectors.toList());
    }

    // Generišite Gettere
    public Long getId() { return id; }
    public String getNaslov() { return naslov; }
    public String getOpis() { return opis; }
    public LocalDateTime getDatumOdrzavanja() { return datumOdrzavanja; }
    public String getLokacija() { return lokacija; }
    public UserResponseDTO getAuthor() { return author; }
    public String getCategoryName() { return categoryName; }
    public List<String> getTags() { return tags; }
}
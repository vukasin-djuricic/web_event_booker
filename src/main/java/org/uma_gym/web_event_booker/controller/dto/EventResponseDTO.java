package org.uma_gym.web_event_booker.controller.dto;

import org.uma_gym.web_event_booker.model.Event;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EventResponseDTO {
    private Long id;
    private String naslov;
    private String opis;
    private LocalDateTime datumOdrzavanja;
    private String lokacija;
    private UserResponseDTO author;
    private String categoryName;
    private Long categoryId; // Dodajemo ID kategorije za lakše editovanje
    private List<TagDTO> tags; // Koristimo TagDTO

    public EventResponseDTO(Event event) {
        this.id = event.getId();
        this.naslov = event.getNaslov();
        this.opis = event.getOpis();
        this.datumOdrzavanja = event.getDatumOdrzavanja();
        this.lokacija = event.getLokacija();

        if (event.getAuthor() != null) {
            this.author = new UserResponseDTO(event.getAuthor());
        }
        if (event.getCategory() != null) {
            this.categoryName = event.getCategory().getName();
            this.categoryId = event.getCategory().getId(); // Dodajemo ID
        }
        if (event.getTags() != null) {
            this.tags = event.getTags().stream().map(TagDTO::new).collect(Collectors.toList());
        } else {
            this.tags = Collections.emptyList();
        }
    }

    // Getteri
    public Long getId() { return id; }
    public String getNaslov() { return naslov; }
    public String getOpis() { return opis; }
    public LocalDateTime getDatumOdrzavanja() { return datumOdrzavanja; }
    public String getLokacija() { return lokacija; }
    public UserResponseDTO getAuthor() { return author; }
    public String getCategoryName() { return categoryName; }
    public Long getCategoryId() { return categoryId; }
    public List<TagDTO> getTags() { return tags; }
}
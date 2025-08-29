package org.uma_gym.web_event_booker.controller.dto;

import org.uma_gym.web_event_booker.model.Tag;

public class TagDTO {
    private Long id;
    private String naziv;

    public TagDTO(Tag tag) {
        this.id = tag.getId();
        this.naziv = tag.getNaziv();
    }

    // Getteri
    public Long getId() { return id; }
    public String getNaziv() { return naziv; }
}
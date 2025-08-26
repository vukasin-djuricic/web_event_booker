package org.uma_gym.web_event_booker.controller.dto;

import org.uma_gym.web_event_booker.model.User;
import org.uma_gym.web_event_booker.model.UserType;

// Ova klasa služi da pošaljemo podatke o korisniku bez lozinke
public class UserResponseDTO {
    private Long id;
    private String email;
    private String ime;
    private String prezime;
    private UserType userType;
    private boolean active;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.ime = user.getIme();
        this.prezime = user.getPrezime();
        this.userType = user.getUserType();
        this.active = user.isActive();
    }

    // Generišite gettere
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getIme() { return ime; }
    public String getPrezime() { return prezime; }
    public UserType getUserType() { return userType; }
    public boolean isActive() { return active; }
}
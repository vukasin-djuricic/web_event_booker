// src/main/java/org/uma_gym/web_event_booker/controller/dto/RsvpCreateDTO.java
package org.uma_gym.web_event_booker.controller.dto;

import jakarta.validation.constraints.NotEmpty;

public class RsvpCreateDTO {
    @NotEmpty(message = "Identifikator korisnika je obavezan.")
    private String userIdentifier;

    public String getUserIdentifier() { return userIdentifier; }
    public void setUserIdentifier(String userIdentifier) { this.userIdentifier = userIdentifier; }
}
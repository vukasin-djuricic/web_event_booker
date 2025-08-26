package org.uma_gym.web_event_booker.controller.dto;

import jakarta.validation.constraints.NotEmpty;

public class CommentCreateDTO {

    @NotEmpty(message = "Ime autora je obavezno")
    private String imeAutora;

    @NotEmpty(message = "Tekst komentara je obavezan")
    private String tekstKomentara;

    // Getteri i Setteri
    public String getImeAutora() { return imeAutora; }
    public void setImeAutora(String imeAutora) { this.imeAutora = imeAutora; }
    public String getTekstKomentara() { return tekstKomentara; }
    public void setTekstKomentara(String tekstKomentara) { this.tekstKomentara = tekstKomentara; }
}
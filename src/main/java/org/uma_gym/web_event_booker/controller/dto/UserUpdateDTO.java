package org.uma_gym.web_event_booker.controller.dto;

import org.uma_gym.web_event_booker.model.UserType;

// DTO za ažuriranje postojećeg korisnika (bez lozinke)
public class UserUpdateDTO {
    private String email;
    private String ime;
    private String prezime;
    private UserType userType;

    // Getteri i Setteri
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }
    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }
    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }
}
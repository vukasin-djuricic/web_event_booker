package org.uma_gym.web_event_booker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "users") //mapira na ime entiteta users u bazi
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Email adresa mora biti validna")
    @NotEmpty(message = "Email je obavezan")
    @Column(unique = true, nullable = false)
    private String email;

    @NotEmpty(message = "Ime je obavezno")
    @Column(nullable = false)
    private String ime;

    @NotEmpty(message = "Prezime je obavezno")
    @Column(nullable = false)
    private String prezime;

    @Enumerated(EnumType.STRING) // Čuva enum kao string u bazi (npr. "ADMIN")
    @Column(nullable = false)
    private UserType userType;

    @Column(nullable = false)
    private boolean active = true; // Podrazumevano je aktivan

    @NotEmpty(message = "Lozinka je obavezna")
    @Column(nullable = false)
    private String lozinka;

    // Konstruktori
    public User() {
    }

    // Getteri i Setteri
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
}
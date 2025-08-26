package org.uma_gym.web_event_booker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Ime autora je obavezno")
    @Column(nullable = false)
    private String imeAutora;

    @NotEmpty(message = "Tekst komentara je obavezan")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String tekstKomentara;

    @Column(nullable = false)
    private LocalDateTime datumKreiranja = LocalDateTime.now();

    private int likeCount = 0;
    private int dislikeCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // Konstruktori
    public Comment() {
    }

    // Getteri i Setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getImeAutora() { return imeAutora; }
    public void setImeAutora(String imeAutora) { this.imeAutora = imeAutora; }
    public String getTekstKomentara() { return tekstKomentara; }
    public void setTekstKomentara(String tekstKomentara) { this.tekstKomentara = tekstKomentara; }
    public LocalDateTime getDatumKreiranja() { return datumKreiranja; }
    public void setDatumKreiranja(LocalDateTime datumKreiranja) { this.datumKreiranja = datumKreiranja; }
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    public int getDislikeCount() { return dislikeCount; }
    public void setDislikeCount(int dislikeCount) { this.dislikeCount = dislikeCount; }
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
}
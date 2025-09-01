package org.uma_gym.web_event_booker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Naslov je obavezan")
    @Column(nullable = false)
    private String naslov;

    @NotEmpty(message = "Opis je obavezan")
    @Lob // Koristi se za duži tekst
    @Column(nullable = false, columnDefinition = "TEXT")
    private String opis;

    @Column(nullable = false)
    private LocalDateTime vremeKreiranja = LocalDateTime.now();

    @NotNull(message = "Datum održavanja je obavezan")
    @Column(nullable = false)
    private LocalDateTime datumOdrzavanja;

    @NotEmpty(message = "Lokacija je obavezna")
    @Column(nullable = false)
    private String lokacija;

    private int brojPoseta = 0;
    private int likeCount = 0;
    private int dislikeCount = 0;

    private Integer maxKapacitet; // Opcionalno

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "event_tags",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    // Konstruktori
    public Event() {
    }

    // Getteri i Setteri...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNaslov() { return naslov; }
    public void setNaslov(String naslov) { this.naslov = naslov; }
    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }
    public LocalDateTime getVremeKreiranja() { return vremeKreiranja; }
    public void setVremeKreiranja(LocalDateTime vremeKreiranja) { this.vremeKreiranja = vremeKreiranja; }
    public LocalDateTime getDatumOdrzavanja() { return datumOdrzavanja; }
    public void setDatumOdrzavanja(LocalDateTime datumOdrzavanja) { this.datumOdrzavanja = datumOdrzavanja; }
    public String getLokacija() { return lokacija; }
    public void setLokacija(String lokacija) { this.lokacija = lokacija; }
    public int getBrojPoseta() { return brojPoseta; }
    public void setBrojPoseta(int brojPoseta) { this.brojPoseta = brojPoseta; }
    public Integer getMaxKapacitet() { return maxKapacitet; }
    public void setMaxKapacitet(Integer maxKapacitet) { this.maxKapacitet = maxKapacitet; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public List<Tag> getTags() { return tags; }
    public void setTags(List<Tag> tags) { this.tags = tags; }
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    public int getDislikeCount() { return dislikeCount; }
    public void setDislikeCount(int dislikeCount) { this.dislikeCount = dislikeCount; }
}
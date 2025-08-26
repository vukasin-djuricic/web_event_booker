package org.uma_gym.web_event_booker.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

public class EventCreateDTO {
    private String naslov;
    private String opis;
    private LocalDateTime datumOdrzavanja;
    private String lokacija;
    private Integer maxKapacitet;
    private Long authorId;
    private Long categoryId;
    private List<Long> tagIds;

    // Generišite Gettere i Settere
    public String getNaslov() { return naslov; }
    public void setNaslov(String naslov) { this.naslov = naslov; }
    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }
    public LocalDateTime getDatumOdrzavanja() { return datumOdrzavanja; }
    public void setDatumOdrzavanja(LocalDateTime datumOdrzavanja) { this.datumOdrzavanja = datumOdrzavanja; }
    public String getLokacija() { return lokacija; }
    public void setLokacija(String lokacija) { this.lokacija = lokacija; }
    public Integer getMaxKapacitet() { return maxKapacitet; }
    public void setMaxKapacitet(Integer maxKapacitet) { this.maxKapacitet = maxKapacitet; }
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public List<Long> getTagIds() { return tagIds; }
    public void setTagIds(List<Long> tagIds) { this.tagIds = tagIds; }
}
package org.uma_gym.web_event_booker.controller.dto;

import org.uma_gym.web_event_booker.model.Comment;
import java.time.LocalDateTime;

public class CommentResponseDTO {

    private Long id;
    private String imeAutora;
    private String tekstKomentara;
    private LocalDateTime datumKreiranja;
    private int likeCount;
    private int dislikeCount;

    public CommentResponseDTO(Comment comment) {
        this.id = comment.getId();
        this.imeAutora = comment.getImeAutora();
        this.tekstKomentara = comment.getTekstKomentara();
        this.datumKreiranja = comment.getDatumKreiranja();
        this.likeCount = comment.getLikeCount();
        this.dislikeCount = comment.getDislikeCount();
    }

    // Getteri
    public Long getId() { return id; }
    public String getImeAutora() { return imeAutora; }
    public String getTekstKomentara() { return tekstKomentara; }
    public LocalDateTime getDatumKreiranja() { return datumKreiranja; }
    public int getLikeCount() { return likeCount; }
    public int getDislikeCount() { return dislikeCount; }
}
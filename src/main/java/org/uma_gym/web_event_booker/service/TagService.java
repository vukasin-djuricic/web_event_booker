package org.uma_gym.web_event_booker.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.uma_gym.web_event_booker.model.Tag;
import org.uma_gym.web_event_booker.repository.TagRepository;

import java.util.List;

@ApplicationScoped
public class TagService {

    @Inject
    private TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag createTag(Tag tag) {
        // Ovde bi išla provera da li tag sa istim nazivom već postoji
        return tagRepository.save(tag);
    }
}
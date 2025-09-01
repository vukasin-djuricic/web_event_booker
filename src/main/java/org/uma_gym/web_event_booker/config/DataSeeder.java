package org.uma_gym.web_event_booker.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;
import org.uma_gym.web_event_booker.model.*;
import org.uma_gym.web_event_booker.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DataSeeder {

    @Inject private UserRepository userRepository;
    @Inject private CategoryRepository categoryRepository;
    @Inject private TagRepository tagRepository;
    @Inject private EventRepository eventRepository;
    @Inject private CommentRepository commentRepository;

    public void seedData(@Observes @Initialized(ApplicationScoped.class) Object init) {

        // Proveravamo da li već postoje događaji da ne bismo duplirali podatke
        if (eventRepository.countAll() > 0) {
            System.out.println(">>> Baza već sadrži podatke, preskačem popunjavanje.");
            return;
        }

        System.out.println(">>> Baza je prazna, popunjavam sa test podacima...");

        // --- 1. Kreiranje Korisnika ---
        User admin = new User();
        admin.setEmail("admin@raf.rs");
        admin.setIme("Admin");
        admin.setPrezime("Adminic");
        admin.setUserType(UserType.ADMIN);
        admin.setLozinka(BCrypt.hashpw("admin123", BCrypt.gensalt()));
        admin.setActive(true);
        userRepository.save(admin);

        User eventCreator = new User();
        eventCreator.setEmail("creator@raf.rs");
        eventCreator.setIme("Kreator");
        eventCreator.setPrezime("Kreatoric");
        eventCreator.setUserType(UserType.EVENT_CREATOR);
        eventCreator.setLozinka(BCrypt.hashpw("creator123", BCrypt.gensalt()));
        eventCreator.setActive(true);
        userRepository.save(eventCreator);
        System.out.println(">>> Kreirano 2 korisnika.");

        // --- 2. Kreiranje Kategorija ---
        List<Category> categories = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            Category cat = new Category();
            cat.setName("Test Kategorija " + i);
            cat.setDescription("Opis za kategoriju broj " + i + ".");
            categoryRepository.save(cat);
            categories.add(cat);
        }
        System.out.println(">>> Kreirano 25 kategorija.");

        // --- 3. Kreiranje Tagova ---
        Tag tagIT = new Tag();
        tagIT.setNaziv("IT");
        tagRepository.save(tagIT);

        Tag tagMuzika = new Tag();
        tagMuzika.setNaziv("Muzika");
        tagRepository.save(tagMuzika);

        Tag tagBiznis = new Tag();
        tagBiznis.setNaziv("Biznis");
        tagRepository.save(tagBiznis);
        System.out.println(">>> Kreirano 3 taga.");

        // --- 4. Kreiranje Događaja ---
        List<Event> createdEvents = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            Event event = new Event();
            event.setNaslov("Test Događaj Broj " + i);
            event.setOpis("Ovo je detaljan opis za testni događaj broj " + i + ". Razgovaraćemo o najnovijim trendovima.");
            event.setLokacija("Lokacija " + ((i % 5) + 1)); // Vrti 5 lokacija
            event.setDatumOdrzavanja(LocalDateTime.now().plusDays(i));
            event.setAuthor((i % 2 == 0) ? admin : eventCreator); // Menjaj autore
            event.setCategory(categories.get((i - 1) % categories.size())); // Vrti kategorije

            List<Tag> eventTags = new ArrayList<>();
            if (i % 2 == 0) eventTags.add(tagIT);
            if (i % 3 == 0) eventTags.add(tagMuzika);
            if (i % 5 == 0) eventTags.add(tagBiznis);
            if (eventTags.isEmpty()) eventTags.add(tagIT); // Svaki događaj ima bar jedan tag
            event.setTags(eventTags);

            eventRepository.save(event);
            createdEvents.add(event);
        }
        System.out.println(">>> Kreirano 25 događaja.");

        // --- 5. Kreiranje Komentara za prva dva događaja ---
        if (!createdEvents.isEmpty()) {
            Comment comment1 = new Comment();
            comment1.setImeAutora("Petar Petrović");
            comment1.setTekstKomentara("Jedva čekam ovaj događaj!");
            comment1.setEvent(createdEvents.get(0));
            commentRepository.save(comment1);

            Comment comment2 = new Comment();
            comment2.setImeAutora("Milica Marković");
            comment2.setTekstKomentara("Da li će biti snimak dostupan online?");
            comment2.setEvent(createdEvents.get(1));
            commentRepository.save(comment2);
            System.out.println(">>> Kreirano 2 komentara.");
        }

        System.out.println(">>> Popunjavanje baze završeno.");
    }
}
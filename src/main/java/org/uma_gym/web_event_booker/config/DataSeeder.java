package org.uma_gym.web_event_booker.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;
import org.uma_gym.web_event_booker.model.*;
import org.uma_gym.web_event_booker.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class DataSeeder {

    @Inject private UserRepository userRepository;
    @Inject private CategoryRepository categoryRepository;
    @Inject private TagRepository tagRepository;
    @Inject private EventRepository eventRepository;
    @Inject private CommentRepository commentRepository;

    /**
     * Ova metoda se automatski poziva kada se CDI Application Scope inicijalizuje.
     * Koristimo je da popunimo bazu sa početnim podacima ako je prazna.
     * @param event Objekat koji nosi informaciju o događaju.
     */
    public void seedData(@Observes @Initialized(ApplicationScoped.class) Object event) {

        // Proveravamo da li već postoje korisnici da ne bismo duplirali podatke
        if (userRepository.findAll().isEmpty()) {
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
            Category catKoncerti = new Category();
            catKoncerti.setName("Koncerti");
            catKoncerti.setDescription("Svi muzički događaji, od roka do klasike.");
            categoryRepository.save(catKoncerti);

            Category catKonferencije = new Category();
            catKonferencije.setName("Konferencije");
            catKonferencije.setDescription("Stručni skupovi, predavanja i paneli.");
            categoryRepository.save(catKonferencije);

            System.out.println(">>> Kreirano 2 kategorije.");

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
            Event event1 = new Event();
            event1.setNaslov("Velika IT Konferencija 2025");
            event1.setOpis("Godišnji skup vodećih IT stručnjaka iz regiona. Teme uključuju veštačku inteligenciju, cloud tehnologije i cybersecurity.");
            event1.setLokacija("Belexpocentar, Beograd");
            event1.setDatumOdrzavanja(LocalDateTime.of(2025, 10, 15, 9, 0));
            event1.setAuthor(admin); // Admin je autor
            event1.setCategory(catKonferencije);
            event1.setTags(List.of(tagIT, tagBiznis)); // Povezujemo sa tagovima
            eventRepository.save(event1);

            Event event2 = new Event();
            event2.setNaslov("Letnji Rock Festival");
            event2.setOpis("Trodnevni festival rock muzike na otvorenom. Nastupaju domaći i strani bendovi.");
            event2.setLokacija("Petrovaradinska tvrđava, Novi Sad");
            event2.setDatumOdrzavanja(LocalDateTime.of(2025, 8, 8, 18, 0));
            event2.setAuthor(eventCreator); // Event Creator je autor
            event2.setCategory(catKoncerti);
            event2.setTags(List.of(tagMuzika)); // Povezujemo sa tagom
            eventRepository.save(event2);

            System.out.println(">>> Kreirano 2 događaja.");

            // --- 5. Kreiranje Komentara ---
            Comment comment1 = new Comment();
            comment1.setImeAutora("Petar Petrović");
            comment1.setTekstKomentara("Odličan line-up na festivalu ove godine!");
            comment1.setEvent(event2); // Komentar za drugi događaj (festival)
            commentRepository.save(comment1);

            Comment comment2 = new Comment();
            comment2.setImeAutora("Jovana Jovanović");
            comment2.setTekstKomentara("Da li će biti live stream konferencije?");
            comment2.setEvent(event1); // Komentar za prvi događaj (konferencija)
            commentRepository.save(comment2);

            System.out.println(">>> Kreirano 2 komentara.");
            System.out.println(">>> Popunjavanje baze završeno.");
        } else {
            System.out.println(">>> Baza već sadrži podatke, preskačem popunjavanje.");
        }
    }
}
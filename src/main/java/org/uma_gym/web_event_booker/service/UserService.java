package org.uma_gym.web_event_booker.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;
import org.uma_gym.web_event_booker.model.User;
import org.uma_gym.web_event_booker.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private JwtService jwtService;

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return this.userRepository.findById(id);
    }

    public User createUser(User user) {
        // 1. Proveri da li korisnik sa datim email-om već postoji
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Korisnik sa email adresom '" + user.getEmail() + "' već postoji.");
        }

        // 2. Heširaj lozinku pre snimanja
        String hashedPassword = BCrypt.hashpw(user.getLozinka(), BCrypt.gensalt());
        user.setLozinka(hashedPassword);

        return this.userRepository.save(user);
    }

    public Optional<String> login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.isActive() && BCrypt.checkpw(password, user.getLozinka())) {
                // Ako je login uspešan, generiši pravi token
                return Optional.of(jwtService.generateToken(user));
            }
        }
        return Optional.empty();
    }
}
package org.uma_gym.web_event_booker.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import org.mindrot.jbcrypt.BCrypt;
import org.uma_gym.web_event_booker.controller.dto.UserUpdateDTO;
import org.uma_gym.web_event_booker.model.User;
import org.uma_gym.web_event_booker.model.UserType;
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

    public User updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Korisnik sa ID " + id + " nije pronađen."));

        // Provera da li novi email već postoji (a ne pripada trenutnom korisniku)
        userRepository.findByEmail(userUpdateDTO.getEmail()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new IllegalArgumentException("Email adresa je već zauzeta.");
            }
        });

        userToUpdate.setIme(userUpdateDTO.getIme());
        userToUpdate.setPrezime(userUpdateDTO.getPrezime());
        userToUpdate.setEmail(userUpdateDTO.getEmail());
        userToUpdate.setUserType(userUpdateDTO.getUserType());

        return userRepository.save(userToUpdate);
    }

    public User toggleUserStatus(Long id) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Korisnik sa ID " + id + " nije pronađen."));

        if (userToUpdate.getUserType() == UserType.ADMIN) {
            throw new ForbiddenException("Admin nalog ne može biti deaktiviran.");
        }

        userToUpdate.setActive(!userToUpdate.isActive());
        return userRepository.save(userToUpdate);
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
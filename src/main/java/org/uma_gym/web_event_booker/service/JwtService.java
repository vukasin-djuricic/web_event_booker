package org.uma_gym.web_event_booker.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.enterprise.context.ApplicationScoped;
import org.uma_gym.web_event_booker.model.User;
import org.uma_gym.web_event_booker.model.UserType; // Dodaj import

import java.util.Date;
import java.util.Optional; // Dodaj import

@ApplicationScoped
public class JwtService {

    private final String JWT_SECRET = "raf_event_booker_super_tajna_rec_za_jwt_koja_mora_biti_duga";
    private final Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
    private final JWTVerifier verifier = JWT.require(algorithm).build();

    public String generateToken(User user) {
        return JWT.create()
                .withClaim("userId", user.getId())
                .withClaim("email", user.getEmail())
                .withClaim("role", user.getUserType().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)))
                .sign(algorithm);
    }

    public DecodedJWT validateToken(String token) {
        return verifier.verify(token);
    }

    // --- NOVE POMOĆNE METODE ---

    /**
     * Izvlači ID korisnika iz dekodiranog tokena.
     */
    public Long getUserId(DecodedJWT decodedJWT) {
        return decodedJWT.getClaim("userId").asLong();
    }

    /**
     * Izvlači ulogu (role) korisnika iz dekodiranog tokena.
     */
    public Optional<UserType> getUserRole(DecodedJWT decodedJWT) {
        try {
            return Optional.of(UserType.valueOf(decodedJWT.getClaim("role").asString()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
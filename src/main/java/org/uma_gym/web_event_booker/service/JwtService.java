package org.uma_gym.web_event_booker.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.enterprise.context.ApplicationScoped;
import org.uma_gym.web_event_booker.model.User;

import java.util.Date;

@ApplicationScoped
public class JwtService {

    // U realnoj aplikaciji, ovaj "secret" bi trebalo čuvati na sigurnom mestu
    // (npr. environment variable), a ne hardkodirati u kodu.
    private final String JWT_SECRET = "raf_event_booker_super_tajna_rec_za_jwt_koja_mora_biti_duga";

    private final Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
    private final JWTVerifier verifier = JWT.require(algorithm).build();

    /**
     * Generiše JWT token za datog korisnika.
     */
    public String generateToken(User user) {
        return JWT.create()
                // "Claims" su podaci koje čuvamo unutar tokena
                .withClaim("userId", user.getId())
                .withClaim("email", user.getEmail())
                .withClaim("role", user.getUserType().toString())
                // Vreme isteka tokena (npr. 24 sata od sada)
                .withExpiresAt(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)))
                .sign(algorithm);
    }

    /**
     * Validira token i vraća dekodirane podatke ako je ispravan.
     * Baca izuzetak ako token nije validan.
     */
    public DecodedJWT validateToken(String token) {
        return verifier.verify(token);
    }
}
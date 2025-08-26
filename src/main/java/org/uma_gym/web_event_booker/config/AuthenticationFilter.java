package org.uma_gym.web_event_booker.config;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.uma_gym.web_event_booker.controller.Secured;
import org.uma_gym.web_event_booker.service.JwtService;

import java.io.IOException;

@Secured // Kaže filteru da se primenjuje samo na endpoint-e sa @Secured anotacijom
@Provider // Registruje filter u JAX-RS aplikaciji
@Priority(Priorities.AUTHENTICATION) // Postavlja visok prioritet izvršavanja
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private JwtService jwtService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // 1. Preuzmi 'Authorization' header iz zahteva
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // 2. Proveri da li header postoji i da li je u ispravnom formatu "Bearer <token>"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            abortWithUnauthorized(requestContext, "Authorization header must be provided.");
            return;
        }

        // 3. Izdvoj token iz headera
        String token = authHeader.substring("Bearer ".length()).trim();

        try {
            // 4. Validiraj token
            jwtService.validateToken(token);
            // U naprednijoj verziji, ovde biste mogli da izvučete korisnika iz tokena
            // i postavite ga u SecurityContext da bude dostupan kontrolerima.
        } catch (Exception e) {
            // 5. Ako validacija ne uspe, odbij zahtev
            abortWithUnauthorized(requestContext, "Invalid token.");
        }
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext, String message) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"" + message + "\"}")
                        .type("application/json")
                        .build()
        );
    }
}
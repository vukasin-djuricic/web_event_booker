package org.uma_gym.web_event_booker.config;

import com.auth0.jwt.interfaces.DecodedJWT; // Dodaj import
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext; // Dodaj import
import jakarta.ws.rs.ext.Provider;
import org.uma_gym.web_event_booker.controller.Secured;
import org.uma_gym.web_event_booker.service.JwtService;

import java.io.IOException;
import java.security.Principal; // Dodaj import

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private JwtService jwtService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            abortWithUnauthorized(requestContext, "Authorization header must be provided.");
            return;
        }

        String token = authHeader.substring("Bearer ".length()).trim();

        try {
            DecodedJWT decodedJWT = jwtService.validateToken(token); // Sačuvaj dekodirani token

            // IZMENA POČINJE OVDE: Postavljanje SecurityContext-a

            // Preuzmi podatke iz tokena
            String userId = decodedJWT.getClaim("userId").asLong().toString();
            String role = decodedJWT.getClaim("role").asString();

            // Kreiraj novi SecurityContext
            SecurityContext originalContext = requestContext.getSecurityContext();
            SecurityContext newContext = new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    // Principal je standardni Java objekat koji predstavlja identitet korisnika
                    return () -> userId;
                }

                @Override
                public boolean isUserInRole(String requiredRole) {
                    // Ova metoda proverava da li korisnik ima traženu ulogu
                    return role != null && role.equals(requiredRole);
                }

                @Override
                public boolean isSecure() {
                    return originalContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Bearer";
                }
            };

            // Postavi novi SecurityContext za ovaj zahtev
            requestContext.setSecurityContext(newContext);

        } catch (Exception e) {
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
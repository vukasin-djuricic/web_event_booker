package org.uma_gym.web_event_booker.config;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import org.uma_gym.web_event_booker.controller.RolesAllowed;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Provider
@Priority(Priorities.AUTHORIZATION) // Izvršava se nakon AUTHENTICATION
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo; // Daje nam informacije o metodi koja se poziva

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // 1. Dobij informaciju o metodi koja je pozvana
        Method resourceMethod = resourceInfo.getResourceMethod();

        // 2. Proveri da li metoda ima @RolesAllowed anotaciju
        if (resourceMethod.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAnnotation = resourceMethod.getAnnotation(RolesAllowed.class);
            List<String> allowedRoles = Arrays.asList(rolesAnnotation.value());

            // 3. Dobij SecurityContext koji je postavio AuthenticationFilter
            SecurityContext securityContext = requestContext.getSecurityContext();

            // 4. Proveri da li korisnik ima bar jednu od dozvoljenih uloga
            boolean authorized = allowedRoles.stream()
                    .anyMatch(securityContext::isUserInRole);

            // 5. Ako nema, odbij zahtev sa 403 Forbidden
            if (!authorized) {
                requestContext.abortWith(
                        Response.status(Response.Status.FORBIDDEN)
                                .entity("{\"error\": \"You do not have permission to access this resource.\"}")
                                .type("application/json")
                                .build()
                );
            }
        }
    }
}
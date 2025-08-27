package org.uma_gym.web_event_booker.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider // Ova anotacija je ključna!
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        // Dozvoljava zahteve sa BILO KOJE adrese. Za razvoj je ovo u redu.
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");

        // Dozvoljava metode koje koristimo (GET, POST, itd.)
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        // Dozvoljava zaglavlja koja šaljemo, uključujući 'Authorization' za token
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
    }
}
package org.lection.initial;


import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

//https://www.w3schools.com/tags/ref_urlencode.ASP Hello+Günter
//https://docs.mapp.com/docs/url-encoding-and-what-characters-are-valid-in-a-uri
@Slf4j
public class URLExample {
    public static void main(String[] args) {
        try {
            //https://example.com/page
            //mailto:someone@example.com 
            //tel:+1-800-555-1212
            //ftp://ftp.example.com/path/to/file
            //urn:isbn:0451450523
            //https://example. com

            URI uriInfo = getUriInfo("");

            // Validating the authority component (optional)

            // Converting URI to URL


        } catch (Exception e) {
            log.error("An error occurred:" + e.getMessage());
        }
    }

    private static URI getUriInfo(String uriString) throws URISyntaxException {
        //scheme:[//authority]path[?query][#fragment]
        URI myUri = new URI(uriString);


        return myUri;
    }
}

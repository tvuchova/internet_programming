package org.lecture.http.clients;


import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


//https://www.w3schools.com/tags/ref_urlencode.ASP
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


            URI uriInfo = getUriInfo("urn:isbn:0451450523");

            // Validating the authority component (optional)
            uriInfo.parseServerAuthority();

            // Converting URI to URL
            URL url = uriInfo.toURL();
            log.info("URL is {}", url);

        } catch (Exception e) {
            log.error("An error occurred:" + e.getMessage());
        }
    }

    private static URI getUriInfo(String uriString) throws URISyntaxException {
        URI myUri = new URI(uriString);
        log.info("Schema is {}", myUri.getScheme());
        log.info("Specific part is {}", myUri.getSchemeSpecificPart());
        log.info("Authority is {}", myUri.getAuthority());
        log.info("Host is {}", myUri.getHost());
        log.info("Port is {}", myUri.getPort());
        log.info("Path is {}", myUri.getPath());
        log.info("Query part {}", myUri.getQuery());
        log.info("Fragment {} ", myUri.getFragment());


        return myUri;
    }
}

package org.lecture.servlet;

import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import java.io.File;

@Slf4j
public class Main {
    public static void main(String[] args) throws LifecycleException {

        log.info("Starting embedded Tomcat server...");

        // Initialize Tomcat instance and set port
        Tomcat tomcat = new Tomcat();
        tomcat.setHostname("localhost");
        tomcat.setPort(8083);
        tomcat.getConnector();

        // Set up the directory for static resources (webAppDir)
        String webAppDir = new File("httpExamples/src/main/webapp").getAbsolutePath();
        Context context = tomcat.addWebapp("/", webAppDir);
        addFilter(context, "myFilter", new MyFilter(), "/*");

        // Register UserServlet programmatically and map it to /UserServlet
        Tomcat.addServlet(context, "userServlet", new UserServlet());
        context.addServletMappingDecoded("/UserServlet", "userServlet");

        // Start the Tomcat server
        try {
            log.info("Attempting to start Tomcat...");
            tomcat.start();
            log.info("Tomcat started successfully!");
            // Keep the server running to handle requests
            tomcat.getServer().await();
        } catch (Exception e) {
            log.error("Error starting Tomcat: " + e.getMessage(), e);
        }
    }

    private static void addFilter(Context context, String filterName, Filter filter, String urlPattern) {
        // Define the filter
        FilterDef filterDef = new FilterDef();
        filterDef.setFilterName(filterName);
        filterDef.setFilter(filter);
        filterDef.setFilterClass(filter.getClass().getName());
        context.addFilterDef(filterDef);

        // Map the filter to a URL pattern
        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName(filterName);
        filterMap.addURLPattern(urlPattern);
        context.addFilterMap(filterMap);
    }
}
package com.pravin;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080); // set server port
        tomcat.getConnector();
        tomcat.setBaseDir("temp"); // temp directory

        // Add context ("" = root context)
        Context context = tomcat.addContext("", null);

        // Register and map servlet
        Tomcat.addServlet(context, "HelloServlet", new HelloServlet());
        context.addServletMappingDecoded("/hello", "HelloServlet");

        // Start Tomcat
        tomcat.start();
        System.out.println("Tomcat started. Access your servlet at: http://localhost:8080/hello");

        tomcat.getServer().await();
    }
}

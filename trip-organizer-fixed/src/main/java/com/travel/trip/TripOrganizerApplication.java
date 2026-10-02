package com.travel.trip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TripOrganizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TripOrganizerApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("  Trip Organizer Application Started!");
        System.out.println("  Access at:     http://localhost:4040");
        System.out.println("  H2 Console:    http://localhost:4040/h2-console");
        System.out.println("  JDBC URL:      jdbc:h2:mem:tripdb");
        System.out.println("  Username:      sa  |  Password: (empty)");
        System.out.println("  Admin Login:   admin@travelease.com / admin123");
        System.out.println("  Demo Login:    test@email.com / password");
        System.out.println("========================================\n");
    }
}

package org.example.rowingcoaching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class RowingCoachingApplication {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(RowingCoachingApplication.class, args);
    }

    @PostConstruct
    public void logConfiguration() {
        System.out.println("=== DEBUGGING CONFIGURATION ===");
        System.out.println("Active Profiles: " + String.join(", ", env.getActiveProfiles()));
        System.out.println("Database URL: " + env.getProperty("spring.datasource.url"));
        System.out.println("Database User: " + env.getProperty("spring.datasource.username"));
        System.out.println("DATABASE_URL: " + env.getProperty("DATABASE_URL"));
        System.out.println("DATABASE_USERNAME: " + env.getProperty("DATABASE_USERNAME"));
        System.out.println("=== END DEBUGGING ===");
    }
}

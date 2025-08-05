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
        System.out.println("SUPABASE_URL: " + env.getProperty("SUPABASE_URL"));
        System.out.println("SUPABASE_DB_HOST: " + env.getProperty("SUPABASE_DB_HOST"));
        System.out.println("SPRING_PROFILES_ACTIVE: " + env.getProperty("SPRING_PROFILES_ACTIVE"));
        System.out.println("=== END DEBUGGING ===");
    }
}

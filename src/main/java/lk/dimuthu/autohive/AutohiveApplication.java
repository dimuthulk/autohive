package lk.dimuthu.autohive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class AutohiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutohiveApplication.class, args);
        System.out.println("Autohive Application Started");
        System.out.println("http://localhost:8080/");
    }

}

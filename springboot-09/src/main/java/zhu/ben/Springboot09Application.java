package zhu.ben;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Springboot09Application {

    public static void main(String[] args) {
        SpringApplication.run(Springboot09Application.class, args);
    }

}

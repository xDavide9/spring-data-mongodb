package com.xdavide9.springdatamongodb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@Slf4j
public class SpringDataMongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataMongodbApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository, MongoTemplate mongoTemplate) {
        return args -> {
            Address address = new Address(
                    "Italy",
                    "Rome",
                    "00100"
            );
            String email = "john@gmail.com";
            Student student = new Student(
                    "John",
                    "Doe",
                    email,
                    Gender.MALE,
                    address,
                    List.of("Math", "Computer Science"),
                    new BigDecimal("200.00"),
                    LocalDateTime.now()
            );

            Query query = new Query();
            query.addCriteria(Criteria.where("email").is(email));
            List<Student> students = mongoTemplate.find(query, Student.class);
            if (students.isEmpty()) {
                log.info("Inserting student {}", student);
                repository.insert(student);
            } else {
                log.error("Student {} already exists", student);
                throw new IllegalStateException("Student already exists");
            }
        };
    }
}

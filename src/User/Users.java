package com.JPA.JPA.User;

import jakarta.persistence.*;
import lombok.*;
import net.datafaker.Faker;

@Entity
@Table(name = "users")
@Data
//@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String email;
    private static final Faker faker = new Faker();
    public Users(){
        this.name = faker.name().firstName();
        this.email = faker.internet().emailAddress();
    }
}

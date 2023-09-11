package com.example.demo.api;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Documented;
import java.math.BigDecimal;
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "users")
@EqualsAndHashCode(of = {"id","name","department"})
public class User {
    @Id
    private String id;
    private String name;
    private String age;
    private BigDecimal salary;
    private String department;
}

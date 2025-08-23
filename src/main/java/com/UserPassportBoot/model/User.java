package com.UserPassportBoot.model;

import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



@Entity
@Table(name = "java_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    @NotBlank(message = "Name can't be empty")
    private String name;

    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Email should be valid")
    @Column(name = "email")
    private String email;

    @Column(name = "created_at", updatable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime createdAt;

@Column(name="date_of_birth")
@Past(message = "Date should be in past")
@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

@Column(name = "gender")
@NotEmpty(message = "Gender can't be empty")
private String gender;

@Column(name = "password")
@NotEmpty(message = "Password can't be empty")
private String password;


    @OneToOne(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Passport passport;


    public User() {
        this.createdAt = LocalDateTime.now();
    }


    public User(String name, String email, LocalDate date_of_birth, String gender, String password) {
        this();
        this.name = name;
        this.email = email;
        this.dateOfBirth = date_of_birth;
        this.gender = gender;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
        passport.setOwner(this);
    }


    public String getFormattedCreatedAt() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        return createdAt.format(dateTimeFormatter);
    }

    @Override
    public String toString() {
        return "User  {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", createdAt='" + getFormattedCreatedAt() + '\'' +
                ", Date of Birth='" + getFormattedDateOfBirth() + '\'' +
                ", Gender='" + gender + '\'' +
                ", Password='" + password +
                '}';
    }


    public String getFormattedDateOfBirth() {
        if(dateOfBirth == null)
            return null;
        return dateOfBirth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfBirth(){
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

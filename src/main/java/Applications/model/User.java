package Applications.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
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

    @OneToOne(mappedBy = "owner")
    private Passport passport;

    public User() {
        this.createdAt = LocalDateTime.now();
    }


    public User(String name, String email, LocalDate date_of_birth, String gender) {
        this();
        this.name = name;
        this.email = email;
        this.dateOfBirth = date_of_birth;
        this.gender = gender;
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
                ", createdAt='" + getFormattedDateOfBirth() + '\'' +
                ", createdAt='" + gender +
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

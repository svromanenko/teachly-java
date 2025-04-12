package org.teachly.models;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank
    @Size(max = 50)
    private String username;

    @Column(nullable = false)
    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role = Role.STUDENT;

    @ElementCollection
    @CollectionTable(name = "user_subjects", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "subject")
    private Set<String> subjects = new HashSet<>();

    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String about;

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateProfile(String name, String about, Role role, Set<String> subjects) {
        this.name = name;
        this.about = about;
        this.role = role;
        this.subjects = subjects;
    }

    // Неизменяемый Set
    public Set<String> getSubjects() {
        return Collections.unmodifiableSet(subjects);
    }
}

package com.library.library_management.entity;

import com.library.library_management.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;  // Either "STUDENT" or "ADMIN"

    // Additional fields for academic year and other user-specific info
    private String academicYear;
    private String classAssigned;

    // Reference to the student
    @OneToOne(mappedBy = "user")
    private Student student;
}



package com.library.library_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.library.library_management.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String nationality;
    private String category;

    private String lastSchoolAttended;
    private String lastClassPassed;
    private String yearOfPassing;
    private String board;
    private String marksObtained;
    private String classApplyingFor;
    private String secondLanguageChoice;

    private String fatherName;
    private String motherName;
    private String guardianName;
    private String occupation;
    private String primaryMobile;
    private String alternateMobile;
    private String emailAddress;
    private String residentialAddress;
    private String emergencyContactName;
    private String emergencyContactRelation;
    private String emergencyContactPhone;

    // Uploaded Document Paths
    private String birthCertificatePath;
    private String reportCardPath;
    private String transferCertificatePath;
    private String photoPath;
    private String aadharCardPath;

    // Reference to the User table
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    private UserStatus status; // e.g., "Pending", "Approved", "Rejected"
}

package com.library.library_management.service;

import com.library.library_management.entity.Student;
import com.library.library_management.entity.User;
import com.library.library_management.enums.Role;
import com.library.library_management.enums.UserStatus;
import com.library.library_management.repository.StudentRepository;
import com.library.library_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final String UPLOAD_DIR = "uploads/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB limit

    private final StudentRepository studentRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void saveStudentWithDocuments(Student student, MultipartFile birthCertificate,
                                         MultipartFile reportCard, MultipartFile transferCertificate,
                                         MultipartFile photo, MultipartFile aadharCard) throws Exception {

        Files.createDirectories(Paths.get(UPLOAD_DIR));

        // Validate files
        validateFile(birthCertificate);
        validateFile(reportCard);
        validateFile(transferCertificate);
        validateFile(photo);
        validateFile(aadharCard);

        // Save files and set paths
        saveFile(birthCertificate, student::setBirthCertificatePath);
        saveFile(reportCard, student::setReportCardPath);
        saveFile(transferCertificate, student::setTransferCertificatePath);
        saveFile(photo, student::setPhotoPath);
        saveFile(aadharCard, student::setAadharCardPath);

        // Create User and assign to Student
        User user = new User();
        String username = generateUsername(student.getDateOfBirth(), student.getYearOfPassing(), student.getClassApplyingFor());
        String password = generatePassword();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.STUDENT);
        user.setAcademicYear(student.getYearOfPassing());
        user.setClassAssigned(student.getClassApplyingFor());

        userRepository.save(user);
        student.setStatus(UserStatus.PENDING);
        student.setUser(user);
        studentRepository.save(student);
    }

    // Validate file size and type
    private void validateFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File cannot be empty!");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("File is too large! Maximum allowed size is 5MB.");
        }

        String[] allowedTypes = {"image/jpeg", "image/png", "application/pdf"};
        boolean validType = false;
        for (String type : allowedTypes) {
            if (Objects.equals(file.getContentType(), type)) {
                validType = true;
                break;
            }
        }

        if (!validType) {
            throw new IOException("Invalid file type! Allowed types: JPG, PNG, PDF.");
        }
    }

    // Save file and set the corresponding path
    private void saveFile(MultipartFile file, java.util.function.Consumer<String> setPath) throws IOException {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            file.transferTo(path);
            setPath.accept(path.toString());
        }
    }

    // Generate a unique username for the student
    private String generateUsername(String dob, String academicYear, String classAppliedFor) {
        return academicYear + "-" + classAppliedFor + "-" + dob.replaceAll("-", "");
    }

    // Generate a random password for the student
    private String generatePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }

    public Student getStudentById(Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        return student.orElse(null);
    }

    public String getApplicationStatus(Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        return student.map(s -> s.getStatus().name()).orElse("Student not found!");
    }
}

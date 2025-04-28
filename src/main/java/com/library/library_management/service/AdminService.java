package com.library.library_management.service;

import com.library.library_management.entity.Student;
import com.library.library_management.enums.UserStatus;
import com.library.library_management.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final StudentRepository studentRepository;
    // Get all unverified students
    public List<Student> getAllUnverifiedStudents() {
        return studentRepository.findAllByStatus(UserStatus.PENDING);  // Implement this in the repository
    }

    // Verify a student
    public void verifyStudent(Long studentId) throws Exception {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            Student existingStudent = student.get();
            existingStudent.setStatus(UserStatus.APPROVED);
            studentRepository.save(existingStudent);
        } else {
            throw new Exception("Student not found.");
        }
    }

    public Student getStudentById(Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        // or throw an exception
        return student.orElse(null);
    }
}

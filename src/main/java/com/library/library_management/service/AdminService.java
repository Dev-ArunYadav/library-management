package com.library.library_management.service;

import com.library.library_management.entity.Student;
import com.library.library_management.enums.UserStatus;
import com.library.library_management.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final Logger logger = LoggerFactory.getLogger(AdminService.class);
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

//    public void updateStudentStatus(Long studentId, UserStatus status) {
//        Optional<Student> student = studentRepository.findById(studentId);
//        logger.info("Updating status for student ID: {} and Status : {}", studentId, status);
//        if (student.isPresent()) {
//            student.get().setStatus(status);
//            studentRepository.save(student.get());
//        }
//    }

    @Transactional
    public void updateStudentStatus(Long studentId, UserStatus status) {
        logger.info("Updating status for student ID: {} and Status : {}", studentId, status);

        // Direct update via custom query, without loading the full entity
        int rowsAffected = studentRepository.updateStatus(studentId, status);

        if (rowsAffected > 0) {
            logger.info("Status updated successfully for student ID: {} and Status : {}", studentId, status);
        } else {
            logger.warn("No student found with ID: {}", studentId);
        }
    }


    public List<Student> updateListOfStudentStatus(List<Long> studentIds, UserStatus status) {
        List<Student> students = studentRepository.findAllById(studentIds);
        for (Student student : students) {
            student.setStatus(status);
        }
        return studentRepository.saveAll(students);
    }
}

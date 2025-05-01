package com.library.library_management.controller;

import com.library.library_management.entity.Student;
import com.library.library_management.enums.UserStatus;
import com.library.library_management.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService studentService;

    // API to list all unverified students
    @GetMapping("/students/unverified")
    public ResponseEntity<List<Student>> getUnverifiedStudents() {
        List<Student> unverifiedStudents = studentService.getAllUnverifiedStudents();
        return ResponseEntity.ok(unverifiedStudents);
    }

    // API for admin to verify a student's admission
    @PostMapping("/students/verify/{studentId}")
    public ResponseEntity<String> verifyStudent(@PathVariable Long studentId) {
        try {
            studentService.verifyStudent(studentId);
            return ResponseEntity.ok("Student Verified Successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    // API to get the details of a specific student (for admin use)
    @GetMapping("/students/details/{studentId}")
    public ResponseEntity<Student> getStudentDetails(@PathVariable Long studentId) {
        Student student = studentService.getStudentById(studentId);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    // API to update the status of a student (for admin use)
    @PutMapping("/students/updateStatus/{studentId}")
    public ResponseEntity<String> updateStudentStatus(@PathVariable Long studentId, @RequestParam(value = "status") String status) {
        try {
            studentService.updateStudentStatus(studentId, UserStatus.valueOf(status));
            return ResponseEntity.ok("Student Status Updated Successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }
}
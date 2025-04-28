package com.library.library_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.library_management.entity.Student;
import com.library.library_management.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/student")
public class StudentRestController {

    @Autowired
    private StudentService studentService;

    @PostMapping(
            value = "/submit",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> submitAdmissionForm(
            @RequestPart("student") String studentJson,
            @RequestParam("birthCertificatePath") MultipartFile birthCertificate,
            @RequestParam("reportCardPath") MultipartFile reportCard,
            @RequestParam("transferCertificatePath") MultipartFile transferCertificate,
            @RequestParam("photoPath") MultipartFile photo,
            @RequestParam("aadharCardPath") MultipartFile aadharCard
    ) {
        try {
            Student student = new ObjectMapper().readValue(studentJson, Student.class);
            studentService.saveStudentWithDocuments(student, birthCertificate, reportCard, transferCertificate, photo, aadharCard);
            return ResponseEntity.ok("Admission Form Submitted Successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }


    // API to get the details of a specific student
    @GetMapping("/details/{studentId}")
    public ResponseEntity<Student> getStudentDetails(@PathVariable Long studentId) {
        Student student = studentService.getStudentById(studentId);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    // API for the student to check the status of their application
    @GetMapping("/status/{studentId}")
    public ResponseEntity<String> getApplicationStatus(@PathVariable Long studentId) {
        String status = studentService.getApplicationStatus(studentId);
        return ResponseEntity.ok(status);
    }
}


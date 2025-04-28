package com.library.library_management.repository;

import com.library.library_management.entity.*;
import com.library.library_management.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByStatus(UserStatus status);
}


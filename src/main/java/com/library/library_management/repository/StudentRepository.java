package com.library.library_management.repository;

import com.library.library_management.entity.*;
import com.library.library_management.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByStatus(UserStatus status);

    @Modifying
    @Query("UPDATE Student s SET s.status = :status WHERE s.id = :studentId")
    int updateStatus(@Param("studentId") Long studentId,@Param("status") UserStatus status);
}


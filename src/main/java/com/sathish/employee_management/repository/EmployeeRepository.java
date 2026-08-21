package com.sathish.employee_management.repository;

import com.sathish.employee_management.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<Employee> findByEmail(String email);

    @Query("""
        SELECT e FROM Employee e
        WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(e.department) LIKE LOWER(CONCAT('%', :search, '%'))
        """)
    Page<Employee> searchEmployees(
            @Param("search") String search,
            Pageable pageable);
}

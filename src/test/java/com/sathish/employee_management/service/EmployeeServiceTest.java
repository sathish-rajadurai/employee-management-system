package com.sathish.employee_management.service;

import com.sathish.employee_management.dto.EmployeeRequest;
import com.sathish.employee_management.dto.EmployeeResponse;
import com.sathish.employee_management.entity.Employee;
import com.sathish.employee_management.exception.DuplicateEmailException;
import com.sathish.employee_management.exception.EmployeeNotFoundException;
import com.sathish.employee_management.mapper.EmployeeMapper;
import com.sathish.employee_management.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void createEmployee_shouldSaveEmployee() {
        EmployeeRequest request = new EmployeeRequest();

        request.setFirstName("Sathish");
        request.setLastName("Raj");
        request.setEmail("sathish@test.com");
        request.setDepartment("IT");
        request.setSalary(85000.0);

        Employee savedEmployee = new Employee();

        savedEmployee.setId(1L);
        savedEmployee.setFirstName("Sathish");
        savedEmployee.setLastName("Raj");
        savedEmployee.setEmail("sathish@test.com");
        savedEmployee.setDepartment("IT");
        savedEmployee.setSalary(85000.0);

        EmployeeResponse employeeResponse = new EmployeeResponse(
                1L,
                "Sathish",
                "Raj",
                "sathish@test.com",
                "IT",
                85000.0
        );

        when(employeeRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);
        when(employeeMapper.toResponse(savedEmployee)).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.createEmployee(request);

        assertEquals(1L, result.getId());
        assertEquals("sathish@test.com", result.getEmail());

        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void createEmployee_shouldThrowExceptionForDuplicateEmail() {

        EmployeeRequest request = new EmployeeRequest();

        request.setFirstName("Sathish");
        request.setLastName("Raj");
        request.setEmail("existing@test.com");
        request.setDepartment("IT");
        request.setSalary(85000.0);

        when(employeeRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        DuplicateEmailException exception =
                assertThrows(
                        DuplicateEmailException.class,
                        () -> employeeService.createEmployee(request)
                );

        assertEquals(
                "Employee already exists with email: existing@test.com",
                exception.getMessage()
        );

        verify(employeeRepository, never())
                .save(any(Employee.class));
    }

    @Test
    void getEmployeeById_shouldThrowExceptionWhenNotFound() {

        Long employeeId = 999L;

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.empty());

        EmployeeNotFoundException exception =
                assertThrows(
                        EmployeeNotFoundException.class,
                        () -> employeeService.getEmployeeById(employeeId)
                );

        assertEquals(
                "Employee not found with id: 999",
                exception.getMessage()
        );
    }
}

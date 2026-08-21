package com.sathish.employee_management.service;

import com.sathish.employee_management.dto.EmployeeRequest;
import com.sathish.employee_management.dto.EmployeeResponse;
import com.sathish.employee_management.dto.EmployeeUpdateRequest;
import com.sathish.employee_management.dto.PageResponse;
import com.sathish.employee_management.entity.Employee;
import com.sathish.employee_management.exception.DuplicateEmailException;
import com.sathish.employee_management.exception.EmployeeNotFoundException;
import com.sathish.employee_management.mapper.EmployeeMapper;
import com.sathish.employee_management.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public EmployeeResponse createEmployee(EmployeeRequest request) {

        if(employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Employee already exists with email: " + request.getEmail());
        }

        Employee employee = new Employee();

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setSalary(request.getSalary());

        Employee createdEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponse(createdEmployee);
    }

    public EmployeeResponse getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        return employeeMapper.toResponse(employee);
    }

    public EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        if(employeeRepository.existsByEmailAndIdNot(request.getEmail(), id)){
            throw new DuplicateEmailException("Employee already exists with email: " + request.getEmail());
        }

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setSalary(request.getSalary());

        Employee updatedEmployee = employeeRepository.save(employee);

        return employeeMapper.toResponse(updatedEmployee);
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        employeeRepository.delete(employee);
    }

    public PageResponse<EmployeeResponse> getEmployees(String search, Pageable pageable) {
        Page<Employee> employeePage;
        if (search == null || search.isBlank()){
            employeePage = employeeRepository.findAll(pageable);
        } else {
            employeePage = employeeRepository.searchEmployees(search.trim(), pageable);
        }

        List<EmployeeResponse> employees =
                employeePage.getContent()
                        .stream()
                        .map(employeeMapper::toResponse)
                        .toList();

        return new PageResponse<>(
                employees,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages()
        );
    }
}
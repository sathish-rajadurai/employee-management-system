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
import com.sathish.employee_management.specification.EmployeeSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public EmployeeResponse createEmployee(EmployeeRequest request) {

        log.info("Creating employee with email: {}", request.getEmail());
        if(employeeRepository.existsByEmail(request.getEmail())) {
            log.warn("Employee creation failed. Email already exists: {}", request.getEmail());
            throw new DuplicateEmailException("Employee already exists with email: " + request.getEmail());
        }

        Employee employee = new Employee();

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setSalary(request.getSalary());

        Employee createdEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with ID: {}", createdEmployee.getId());
        return employeeMapper.toResponse(createdEmployee);
    }

    public EmployeeResponse getEmployeeById(Long id) {

        log.info("Fetching employee with ID: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Employee not found with ID: {}", id);
                    return new EmployeeNotFoundException("Employee not found with id: " + id);
                });
        return employeeMapper.toResponse(employee);
    }

    public EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request) {

        log.info("Updating employee with ID: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Employee not found for update. ID: {}", id);
                    return new EmployeeNotFoundException("Employee not found with id: " + id);
                });

        if(employeeRepository.existsByEmailAndIdNot(request.getEmail(), id)){
            log.warn("Employee already exists with email: {}", request.getEmail());
            throw new DuplicateEmailException("Employee already exists with email: " + request.getEmail());
        }

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setSalary(request.getSalary());

        Employee updatedEmployee = employeeRepository.save(employee);
        log.info("Employee updated successfully. ID: {}", updatedEmployee.getId());
        return employeeMapper.toResponse(updatedEmployee);
    }

    public void deleteEmployee(Long id) {

        log.info("Deleting employee with ID: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Employee not found for deletion. ID: {}", id);
                    return new EmployeeNotFoundException("Employee not found with id: " + id);
                });
        employeeRepository.delete(employee);
        log.info("Employee deleted successfully. ID: {}", id);
    }

    public PageResponse<EmployeeResponse> getEmployees(String search, String department,
           Double minSalary, Double maxSalary, Pageable pageable) {

        log.info("Fetching employees. Search: '{}', department: {}, minSalary: {}, maxSalary:{}, Page: {}, Size: {}",
                search, department, minSalary, maxSalary,
                pageable.getPageNumber(),
                pageable.getPageSize());

        Specification<Employee> specification = Specification.where(EmployeeSpecification.hasSearch(search))
                .and(EmployeeSpecification.hasDepartment(department))
                .and(EmployeeSpecification.salaryGreaterThanOrEqual(minSalary))
                .and(EmployeeSpecification.salaryLessThanOrEqual(maxSalary));

        Page<Employee> employeePage = employeeRepository.findAll(specification, pageable);

        List<EmployeeResponse> employees =
                employeePage.getContent()
                        .stream()
                        .map(employeeMapper::toResponse)
                        .toList();
        log.info("Employee search completed. Found {} employees", employeePage.getTotalElements());

        return new PageResponse<>(
                employees,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages()
        );
    }
}
package com.sathish.employee_management.controller;

import com.sathish.employee_management.dto.EmployeeRequest;
import com.sathish.employee_management.dto.EmployeeResponse;
import com.sathish.employee_management.dto.EmployeeUpdateRequest;
import com.sathish.employee_management.dto.PageResponse;
import com.sathish.employee_management.exception.InvalidPaginationException;
import com.sathish.employee_management.exception.InvalidSortFieldException;
import com.sathish.employee_management.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of(
                    "id",
                    "firstName",
                    "lastName",
                    "email",
                    "department",
                    "salary"
            );

    @Operation(
            summary = "Create employee",
            description = "Creates a new employee"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
        return employeeService.createEmployee(employeeRequest);
    }

    @Operation(
            summary = "Get employee by ID",
            description = "Returns an employee using the employee ID"
    )
    @GetMapping("/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable Long id){
        return employeeService.getEmployeeById(id);
    }

    @Operation(
            summary = "Update employee by ID",
            description = "Update employee using the employee ID"
    )
    @PutMapping("/{id}")
    public EmployeeResponse updateEmployee(@PathVariable Long id,
                                   @Valid @RequestBody EmployeeUpdateRequest request){
        return employeeService.updateEmployee(id, request);
    }

    @Operation(
            summary = "Delete employee by ID",
            description = "Delete an employee using the employee ID"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
    }

    @Operation(
            summary = "Get employees",
            description = "Returns paginated, searchable and sortable employees"
    )
    @GetMapping
    public PageResponse<EmployeeResponse> getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary){

        if(!ALLOWED_SORT_FIELDS.contains(sortBy)){
            throw new InvalidSortFieldException("Invalid sort field: " + sortBy);
        }

        if(!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc")){
            throw new InvalidSortFieldException("Invalid sort direction: " + sortDir);
        }

        if (page < 0) {
            throw new InvalidPaginationException("Page must be greater than or equal to 0");
        }

        if (size < 1 || size > 100) {
            throw new InvalidPaginationException("Size must be between 1 and 100");
        }

        if (minSalary != null && minSalary < 0) {
            throw new InvalidPaginationException("Minimum salary cannot be negative");
        }

        if (maxSalary != null && maxSalary < 0) {
            throw new InvalidPaginationException("Maximum salary cannot be negative");
        }

        if (minSalary != null && maxSalary != null && minSalary > maxSalary) {
            throw new InvalidPaginationException("Minimum salary cannot be greater than maximum salary");
        }

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return employeeService.getEmployees(search, department, minSalary, maxSalary, pageable);
    }
}

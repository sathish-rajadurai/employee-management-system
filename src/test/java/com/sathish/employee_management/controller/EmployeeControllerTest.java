package com.sathish.employee_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sathish.employee_management.dto.EmployeeRequest;
import com.sathish.employee_management.dto.EmployeeResponse;
import com.sathish.employee_management.exception.EmployeeNotFoundException;
import com.sathish.employee_management.exception.GlobalExceptionHandler;
import com.sathish.employee_management.service.CustomUserDetailsService;
import com.sathish.employee_management.service.EmployeeService;
import com.sathish.employee_management.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {

        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void createEmployee_shouldReturn201() throws Exception {

        EmployeeRequest request = new EmployeeRequest();

        request.setFirstName("Sathish");
        request.setLastName("Raj");
        request.setEmail("sathish@test.com");
        request.setDepartment("IT");
        request.setSalary(85000.0);

        EmployeeResponse employee = new EmployeeResponse();

        employee.setId(1L);
        employee.setFirstName("Sathish");
        employee.setLastName("Raj");
        employee.setEmail("sathish@test.com");
        employee.setDepartment("IT");
        employee.setSalary(85000.0);

        when(employeeService.createEmployee(any(EmployeeRequest.class)))
                .thenReturn(employee);

        mockMvc.perform(
                post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Sathish"))
                .andExpect(jsonPath("$.email").value("sathish@test.com"));

    }

    @Test
    void createEmployee_shouldReturn400ForInvalidRequest()
            throws Exception {

        EmployeeRequest request = new EmployeeRequest();

        request.setFirstName("");
        request.setLastName("");
        request.setEmail("invalid-email");
        request.setDepartment("");
        request.setSalary(-100.0);

        mockMvc.perform(
                        post("/api/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void getEmployeeById_shouldReturn200() throws Exception {

        EmployeeResponse employee = new EmployeeResponse();

        employee.setId(1L);
        employee.setFirstName("Sathish");
        employee.setLastName("Raj");
        employee.setEmail("sathish@test.com");
        employee.setDepartment("IT");
        employee.setSalary(85000.0);

        when(employeeService.getEmployeeById(1L))
                .thenReturn(employee);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Sathish"))
                .andExpect(jsonPath("$.email").value("sathish@test.com"));
    }

    @Test
    void getEmployeeById_shouldReturn404WhenNotFound()
            throws Exception {

        when(employeeService.getEmployeeById(999L))
                .thenThrow(
                        new EmployeeNotFoundException(
                                "Employee not found with id: 999"
                        )
                );

        mockMvc.perform(
                        get("/api/employees/999")
                )
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.message")
                                .value("Employee not found with id: 999")
                );
    }

}

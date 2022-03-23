package com.aayush.employee.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.aayush.employee.exception.ValidationFailedException;
import com.aayush.employee.model.Employee;
import com.aayush.employee.services.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private EmployeeService employeeService;

  @Test
  void createEmployee() throws Exception {

    Employee employee = new Employee(1, "Aayush", "Agrawal", "agrawal.aayushn91@gmail.com");
    mockMvc
      .perform(
        post("/api/v1/employees")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(employee)))
      .andExpect(status().isCreated());
    verify(employeeService, times(1)).createEmployee(any(Employee.class));
  }

  @Test
  void createEmployee_FirstName_Is_Null() throws Exception {
    when(employeeService.createEmployee(any(Employee.class)))
      .thenThrow(new ValidationFailedException("First name cannot be null or empty"));

    Employee employee = new Employee(1, null, "Agrawal", "agrawal.aayushn91@gmail.com");
    mockMvc
      .perform(
        post("/api/v1/employees")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(employee)))
      .andExpect(status().isBadRequest());
    verify(employeeService, times(1)).createEmployee(any(Employee.class));
  }

  @Test
  void createEmployee_FirstName_Is_Empty() throws Exception {
    when(employeeService.createEmployee(any(Employee.class)))
      .thenThrow(new ValidationFailedException("First name cannot be null or empty"));

    Employee employee = new Employee(1, "", "Agrawal", "agrawal.aayushn91@gmail.com");
    mockMvc
      .perform(
        post("/api/v1/employees")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(employee)))
      .andExpect(status().isBadRequest());
    verify(employeeService, times(1)).createEmployee(any(Employee.class));
  }

  @Test
  void updateEmployee() throws Exception {
    Employee employee = new Employee(1, "Aayush", "Agrawal", "agrawal.aayushn91@gmail.com");

    when(employeeService.updateEmployee(anyLong(), any(Employee.class)))
      .thenReturn(employee);

    mockMvc
      .perform(
        put("/api/v1/employees/{id}", 1l)
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(employee)))
      .andExpect(status().isOk());

    verify(employeeService, times(1)).updateEmployee(anyLong(), any(Employee.class));
  }

  @Test
  void updateEmployee_Invalid_Id() throws Exception {

    Employee employee = new Employee(1, "Aayush", "Agrawal", "agrawal.aayushn91@gmail.com");
    mockMvc
      .perform(
        put("/api/v1/employees/null")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(employee)))
      .andExpect(status().isBadRequest());

    verify(employeeService, times(0)).updateEmployee(anyLong(), any(Employee.class));
  }

  @Test
  void updateEmployee_FirstName_Is_Null() throws Exception {
    when(employeeService.updateEmployee(anyLong(), any(Employee.class)))
      .thenThrow(new ValidationFailedException("First name cannot be null or empty"));

    Employee employee = new Employee(1, null, "Agrawal", "agrawal.aayushn91@gmail.com");
    mockMvc
      .perform(
        put("/api/v1/employees/null")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(employee)))
      .andExpect(status().isBadRequest());

    verify(employeeService, times(0)).updateEmployee(anyLong(), any(Employee.class));
  }

  @Test
  void updateEmployee_FirstName_Is_Empty() throws Exception {
    when(employeeService.updateEmployee(anyLong(), any(Employee.class)))
      .thenThrow(new ValidationFailedException("First name cannot be null or empty"));
    Employee employee = new Employee(1, "", "Agrawal", "agrawal.aayushn91@gmail.com");
    mockMvc
      .perform(
        put("/api/v1/employees/null")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(employee)))
      .andExpect(status().isBadRequest());

    verify(employeeService, times(0)).updateEmployee(anyLong(), any(Employee.class));
  }

  @Test
  void updateEmployee_NotFound() throws Exception {
    when(employeeService.updateEmployee(anyLong(), any(Employee.class)))
      .thenReturn(null);
    Employee employee = new Employee(1, "Aayush", "Agrawal", "agrawal.aayushn91@gmail.com");
    mockMvc
      .perform(
        put("/api/v1/employees/{id}", 1L)
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(employee)))
      .andExpect(status().isNotFound());

    verify(employeeService, times(1)).updateEmployee(anyLong(), any(Employee.class));
  }

  @Test
  void getEmployee() throws Exception {

    when(employeeService.getEmployeeById(1L))
      .thenReturn(new Employee(1, "Aayush", "Agrawal", "agrawal.aayushn91@gmail.com"));

    mockMvc
      .perform(
        get("/api/v1/employees/{id}", 1)
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk()).andReturn();

    verify(employeeService, times(1)).getEmployeeById(1L);
  }

  @Test
  void getEmployee_NotFound() throws Exception {
    mockMvc
      .perform(
        get("/api/v1/employees/{id}", 1)
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound()).andReturn();

    verify(employeeService, times(1)).getEmployeeById(1L);
  }

  @Test
  void getEmployee_Invalid_Id() throws Exception {
    mockMvc
      .perform(
        get("/api/v1/employees/null")
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest()).andReturn();

    verify(employeeService, times(0)).getEmployeeById(1L);
  }

  @Test
  void getAllEmployees() throws Exception {

    when(employeeService.getAllEmployees())
      .thenReturn(List.of(new Employee(1, "Aayush", "Agrawal", "agrawal.aayushn91@gmail.com")));

    String s ="";
    "*".repeat(2);
    mockMvc
      .perform(
        get("/api/v1/employees")
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk()).andReturn();

    verify(employeeService, times(1)).getAllEmployees();
  }

}

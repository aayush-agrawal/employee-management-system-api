package com.aayush.employee.services;

import com.aayush.employee.entity.EmployeeEntity;
import com.aayush.employee.exception.ValidationFailedException;
import com.aayush.employee.model.Employee;
import com.aayush.employee.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;

  @Override
  public Employee createEmployee(Employee employee) {
    if (employee.getFirstName() == null || employee.getFirstName().isEmpty())
      throw new ValidationFailedException("First name cannot be null or empty.");
    EmployeeEntity employeeEntity = new EmployeeEntity();
    BeanUtils.copyProperties(employee, employeeEntity);
    employeeEntity = employeeRepository.save(employeeEntity);
    BeanUtils.copyProperties(employeeEntity, employee);
    return employee;
  }

  @Override
  public List<Employee> getAllEmployees() {
    List<EmployeeEntity> employeeEntities = employeeRepository.findAll();
    return employeeEntities
      .stream()
      .map(emp -> new Employee(
        emp.getId(),
        emp.getFirstName(),
        emp.getLastName(),
        emp.getEmailId()))
      .collect(Collectors.toList());
  }

  @Override
  public boolean deleteEmployee(Long id) {
    employeeRepository.findById(id)
      .ifPresent(employeeRepository::delete);
    return true;
  }

  @Override
  public Employee getEmployeeById(Long id) {
    Optional<EmployeeEntity> entityOptional = employeeRepository.findById(id);
    return entityOptional
      .map(employeeEntity -> {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeEntity, employee);
        return employee;
      }).orElse(null);
  }

  @Override
  public Employee updateEmployee(Long id, Employee employee) {
    if (employee.getFirstName() == null || employee.getFirstName().isEmpty())
      throw new ValidationFailedException("Firstname cannot be null or empty");
    return employeeRepository.findById(id)
      .map(employeeEntity -> {
        employeeEntity.setEmailId(employee.getEmailId());
        employeeEntity.setFirstName(employee.getFirstName());
        employeeEntity.setLastName(employee.getLastName());

        EmployeeEntity entity = employeeRepository.save(employeeEntity);
        BeanUtils.copyProperties(entity, employee);
        return employee;
      })
      .orElse(null);
  }
}

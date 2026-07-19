package com.ems.service;

import com.ems.dto.EmployeeRequestDTO;
import com.ems.dto.EmployeeResponseDTO;
import com.ems.entity.Employee;
import com.ems.exception.EmployeeNotFoundException;
import com.ems.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Add Employee
    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO dto) {

        Employee employee = new Employee();

        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());

        Employee savedEmployee = employeeRepository.save(employee);

        return new EmployeeResponseDTO(
                savedEmployee.getId(),
                savedEmployee.getName(),
                savedEmployee.getEmail(),
                savedEmployee.getDepartment(),
                savedEmployee.getSalary()
        );
    }

    // Get Employees with Pagination & Sorting
    public Page<EmployeeResponseDTO> getEmployees(Pageable pageable) {

        return employeeRepository.findAll(pageable)
                .map(employee -> new EmployeeResponseDTO(
                        employee.getId(),
                        employee.getName(),
                        employee.getEmail(),
                        employee.getDepartment(),
                        employee.getSalary()
                ));
    }

    // Get Employee by ID
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with id: " + id));
    }

    // Delete Employee
    public void deleteEmployee(Long id) {

        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(
                    "Employee not found with id: " + id);
        }

        employeeRepository.deleteById(id);
    }
}
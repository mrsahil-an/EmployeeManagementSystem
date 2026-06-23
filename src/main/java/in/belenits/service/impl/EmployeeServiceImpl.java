package in.belenits.service.impl;

import in.belenits.dto.EmployeeUpdateDto;
import in.belenits.exception.EmployeeNotFoundException;
import in.belenits.model.Employee;
import in.belenits.repository.EmployeeRepository;
import in.belenits.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void saveEmployee(Employee employee) {
        LOGGER.info("Saving employee with name: {}", employee.getName());

        Optional.of(employee)
                .ifPresentOrElse(
                        employeeRepository::save,
                        () -> {
                            LOGGER.error("Employee object is null");
                            throw new IllegalArgumentException("Employee cannot be null");
                        }
                );

        LOGGER.info("Employee saved successfully");
    }

    @Override
    public Employee updateEmployee(Long id, EmployeeUpdateDto employeeUpdateDto) {

        LOGGER.info("Updating employee with id: {}", id);

        Employee employee = employeeRepository.findById(id)
                .map(existingEmployee -> {

                    Optional.ofNullable(employeeUpdateDto.getName())
                            .ifPresent(existingEmployee::setName);

                    Optional.ofNullable(employeeUpdateDto.getCompany())
                            .ifPresent(existingEmployee::setCompany);

                    Optional.ofNullable(employeeUpdateDto.getDesignation())
                            .ifPresent(existingEmployee::setDesignation);

                    Optional.ofNullable(employeeUpdateDto.getSalary())
                            .ifPresent(existingEmployee::setSalary);

                    employeeRepository.save(existingEmployee);

                    LOGGER.info("Employee updated successfully with id: {}", id);

                    return existingEmployee;
                })
                .orElseThrow(() -> {
                    LOGGER.error("Employee not found with id: {}", id);
                    return new EmployeeNotFoundException(id);
                });

        return employee;
    }

    @Override
    public void deleteEmployee(Long id) {

        LOGGER.info("Deleting employee with id: {}", id);

        employeeRepository.findById(id)
                .ifPresentOrElse(
                        employee -> {
                            employeeRepository.delete(employee);
                            LOGGER.info("Employee deleted successfully with id: {}", id);
                        },
                        () -> {
                            LOGGER.error("Employee not found with id: {}", id);
                            throw new RuntimeException(
                                    "Employee not found with id: " + id);
                        }
                );
    }

    @Override
    public List<Employee> getAllEmployee() {

        LOGGER.info("Fetching all employees");

        List<Employee> employees = new ArrayList<>(employeeRepository.findAll());

        LOGGER.info("Total employees found: {}", employees.size());

        return employees;
    }

    @Override
    public Employee getEmployee(Long id) {

        LOGGER.info("Fetching employee with id: {}", id);

        return employeeRepository.findById(id)
                .map(employee -> {
                    LOGGER.info("Employee found with id: {}", id);
                    return employee;
                })
                .orElseThrow(() -> {
                    LOGGER.error("Employee not found with id: {}", id);
                    return new RuntimeException(
                            "Employee not found with id: " + id);
                });
    }
}
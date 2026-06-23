package in.belenits.service;

import in.belenits.dto.EmployeeUpdateDto;
import in.belenits.model.Employee;

import java.util.List;

public interface EmployeeService {

    void saveEmployee(Employee employee);

    Employee updateEmployee(Long id, EmployeeUpdateDto employeeUpdateDto);

    void deleteEmployee(Long id);

    List<Employee> getAllEmployee();

    Employee getEmployee(Long id);
}

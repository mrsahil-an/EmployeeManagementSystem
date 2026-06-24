package in.belenits.service.impl;

import in.belenits.dto.EmployeeCreateRequest;
import in.belenits.dto.EmployeeResponse;
import in.belenits.dto.EmployeeUpdateRequest;
import in.belenits.exception.EmployeeNotFoundException;
import in.belenits.model.Employee;
import in.belenits.repository.EmployeeRepository;
import in.belenits.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    private final FileStorageService fileStorageService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, FileStorageService fileStorageService) {
        this.employeeRepository = employeeRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public EmployeeResponse saveEmployee(
            EmployeeCreateRequest employeeCreateRequest) {

        LOGGER.info(
                "Saving employee with name: {}",
                employeeCreateRequest.name());

        Employee employee = new Employee();

        employee.setName(employeeCreateRequest.name());
        employee.setCompany(employeeCreateRequest.company());
        employee.setDesignation(employeeCreateRequest.designation());
        employee.setSalary(employeeCreateRequest.salary());
        employee.setActive(true);

        Employee savedEmployee =
                employeeRepository.save(employee);

        LOGGER.info(
                "Employee saved successfully with id: {}",
                savedEmployee.getId());

        return mapToResponse(savedEmployee);
    }

    @Override
    public EmployeeResponse updateEmployee(
            Long id,
            EmployeeUpdateRequest employeeUpdateRequest) {

        LOGGER.info("Updating employee with id: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error(
                            "Employee not found with id: {}",
                            id);
                    return new EmployeeNotFoundException(id);
                });

        if (employeeUpdateRequest.name() != null) {
            employee.setName(employeeUpdateRequest.name());
        }

        if (employeeUpdateRequest.company() != null) {
            employee.setCompany(employeeUpdateRequest.company());
        }

        if (employeeUpdateRequest.designation() != null) {
            employee.setDesignation(
                    employeeUpdateRequest.designation());
        }

        if (employeeUpdateRequest.salary() != null) {
            employee.setSalary(employeeUpdateRequest.salary());
        }

        Employee updatedEmployee =
                employeeRepository.save(employee);

        LOGGER.info(
                "Employee updated successfully with id: {}",
                id);

        return mapToResponse(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {

        LOGGER.info(
                "Deleting employee with id: {}",
                id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error(
                            "Employee not found with id: {}",
                            id);
                    return new EmployeeNotFoundException(id);
                });

        employeeRepository.delete(employee);

        LOGGER.info(
                "Employee deleted successfully with id: {}",
                id);
    }

    @Override
    public List<EmployeeResponse> getAllEmployee() {

        LOGGER.info("Fetching all employees");

        List<EmployeeResponse> employees =
                employeeRepository.findAll()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        LOGGER.info(
                "Total employees found: {}",
                employees.size());

        return employees;
    }

    @Override
    public EmployeeResponse getEmployee(Long id) {

        LOGGER.info(
                "Fetching employee with id: {}",
                id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error(
                            "Employee not found with id: {}",
                            id);
                    return new EmployeeNotFoundException(id);
                });

        LOGGER.info(
                "Employee found with id: {}",
                id);

        return mapToResponse(employee);
    }

    @Override
    public void uploadDocuments(
            Long employeeId,
            MultipartFile photo,
            MultipartFile resume) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new EmployeeNotFoundException(employeeId));

        try {

            if (photo != null && !photo.isEmpty()) {

                String photoName =
                        fileStorageService.saveFile(photo);

                employee.setPhotoFileName(photoName);
            }

            if (resume != null && !resume.isEmpty()) {

                String resumeName =
                        fileStorageService.saveFile(resume);

                employee.setResumeFileName(resumeName);
            }

            employeeRepository.save(employee);

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Failed to upload files",
                    ex
            );
        }
    }

    private EmployeeResponse mapToResponse(Employee employee) {

        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getCompany(),
                employee.getDesignation(),
                employee.getSalary(),
                employee.getActive()
        );
    }
}
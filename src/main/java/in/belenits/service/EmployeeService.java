package in.belenits.service;

import in.belenits.dto.EmployeeCreateRequest;
import in.belenits.dto.EmployeeResponse;
import in.belenits.dto.EmployeeUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse saveEmployee(EmployeeCreateRequest employeeCreateRequest);

    EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest employeeUpdateRequest);

    void deleteEmployee(Long id);

    List<EmployeeResponse> getAllEmployee();

    EmployeeResponse getEmployee(Long id);

    void uploadDocuments(Long id, MultipartFile photo, MultipartFile resume);
}

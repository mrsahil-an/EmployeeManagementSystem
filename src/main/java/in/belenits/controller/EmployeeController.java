package in.belenits.controller;

import in.belenits.dto.EmployeeCreateRequest;
import in.belenits.dto.EmployeeResponse;
import in.belenits.dto.EmployeeUpdateRequest;
import in.belenits.response.CustomApiResponse;
import in.belenits.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Validated
@RequestMapping("/employees")
@Tag(
        name = "Employee Management",
        description = "CRUD operations for employee management"
)
public class EmployeeController {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(
            summary = "Create Employee",
            description = "Creates a new employee record"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Employee created successfully",
                    content = @Content(
                            schema = @Schema(implementation = EmployeeResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request payload"
            )
    })

    @PostMapping
    public ResponseEntity<CustomApiResponse<EmployeeResponse>> createEmployee(
            @Valid @RequestBody EmployeeCreateRequest employeeCreateRequest) {

        LOGGER.info("REST request to create employee");

        EmployeeResponse employeeResponse =
                employeeService.saveEmployee(employeeCreateRequest);

        CustomApiResponse<EmployeeResponse> response =
                new CustomApiResponse<>();

        response.setStatus(HttpStatus.CREATED.value());
        response.setMessage("Employee created successfully");
        response.setData(employeeResponse);

        LOGGER.info("Employee created successfully");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Get Employee By Id",
            description = "Fetch employee details using employee id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee found",
                    content = @Content(
                            schema = @Schema(implementation = EmployeeResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )
    })

    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<EmployeeResponse>> getEmployee(
            @Parameter(description = "Employee Id", example = "1")
            @Positive(message = "Employee id must be positive")
            @PathVariable Long id) {

        LOGGER.info("REST request to fetch employee with id {}", id);

        EmployeeResponse employeeResponse =
                employeeService.getEmployee(id);

        CustomApiResponse<EmployeeResponse> response =
                new CustomApiResponse<>();

        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Employee fetched successfully");
        response.setData(employeeResponse);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get All Employees",
            description = "Fetch all employees from the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employees fetched successfully"
            )
    })
    @GetMapping
    public ResponseEntity<CustomApiResponse<List<EmployeeResponse>>> getAllEmployees() {

        LOGGER.info("REST request to fetch all employees");

        List<EmployeeResponse> employees =
                employeeService.getAllEmployee();

        CustomApiResponse<List<EmployeeResponse>> response =
                new CustomApiResponse<>();

        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Employees fetched successfully");
        response.setData(employees);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update Employee",
            description = "Updates employee details partially"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request payload"
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CustomApiResponse<EmployeeResponse>> updateEmployee(
            @Parameter(description = "Employee Id", example = "1")
            @Positive(message = "Employee id must be positive")
            @PathVariable Long id,
            @Valid @RequestBody EmployeeUpdateRequest employeeUpdateRequest) {

        LOGGER.info("REST request to update employee with id {}", id);

        EmployeeResponse employeeResponse =
                employeeService.updateEmployee(id, employeeUpdateRequest);

        CustomApiResponse<EmployeeResponse> response =
                new CustomApiResponse<>();

        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Employee updated successfully");
        response.setData(employeeResponse);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete Employee",
            description = "Deletes an employee by id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteEmployee(
            @Parameter(description = "Employee Id", example = "1")
            @Positive(message = "Employee id must be positive")
            @PathVariable Long id) {

        LOGGER.info("REST request to delete employee with id {}", id);

        employeeService.deleteEmployee(id);

        CustomApiResponse<Void> response =
                new CustomApiResponse<>();

        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Employee deleted successfully");

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Upload Employee Documents",
            description = """
                Upload employee photo and resume.
                
                - Photo is stored in local file system.
                - Resume is stored in local file system.
                - Only file names are stored in database.
                - Existing employee record must be present.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Documents uploaded successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid file upload request"
            )
    })
    @PostMapping(
            value = "/{id}/documents",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<CustomApiResponse<Void>> uploadDocuments(

            @Parameter(
                    description = "Employee Id",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,

            @Parameter(
                    description = "Employee photo file (jpg, jpeg, png)"
            )
            @RequestPart(required = false)
            MultipartFile photo,

            @Parameter(
                    description = "Employee resume file (pdf, doc, docx)"
            )
            @RequestPart(required = false)
            MultipartFile resume) {

        LOGGER.info(
                "REST request to upload documents for employee id {}",
                id);

        employeeService.uploadDocuments(
                id,
                photo,
                resume);

        CustomApiResponse<Void> response =
                new CustomApiResponse<>();

        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Documents uploaded successfully");

        return ResponseEntity.ok(response);
    }
}
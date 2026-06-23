package in.belenits.controller;

import in.belenits.dto.EmployeeUpdateDto;
import in.belenits.model.Employee;
import in.belenits.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @PostMapping
    public ResponseEntity<Void> createEmployee(
            @Valid @RequestBody Employee employee) {

        LOGGER.info("REST request to create employee");

        employeeService.saveEmployee(employee);

        LOGGER.info("Employee created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).build();
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
                            schema = @Schema(implementation = Employee.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(
            @Parameter(description = "Employee Id", example = "1")
            @PathVariable Long id) {

        LOGGER.info("REST request to fetch employee with id {}", id);

        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @Operation(
            summary = "Get All Employees",
            description = "Fetch all employees from the system"
    )
    @ApiResponse(responseCode = "200", description = "Employees fetched successfully")
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {

        LOGGER.info("REST request to fetch all employees");

        return ResponseEntity.ok(employeeService.getAllEmployee());
    }

    @Operation(
            summary = "Update Employee",
            description = "Updates employee details partially"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @Parameter(description = "Employee Id", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody EmployeeUpdateDto employeeUpdateDto) {

        LOGGER.info("REST request to update employee with id {}", id);

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, employeeUpdateDto)
        );
    }

    @Operation(
            summary = "Delete Employee",
            description = "Deletes an employee by id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "Employee Id", example = "1")
            @PathVariable Long id) {

        LOGGER.info("REST request to delete employee with id {}", id);

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }
}

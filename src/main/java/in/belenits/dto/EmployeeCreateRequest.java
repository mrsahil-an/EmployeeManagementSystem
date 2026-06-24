package in.belenits.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EmployeeCreateRequest(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Company is required")
        String company,

        @NotBlank(message = "Designation is required")
        String designation,

        @NotNull(message = "Salary is required")
        @Positive(message = "Salary must be greater than 0")
        Double salary

) {}
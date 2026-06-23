package in.belenits.dto;

import lombok.Data;

@Data
public class EmployeeUpdateDto {

    private String name;

    private String company;

    private String designation;

    private Double salary;
}

package in.belenits.dto;

public record EmployeeResponse(

        Long id,
        String name,
        String company,
        String designation,
        Double salary,
        Boolean active
) {}

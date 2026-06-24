package in.belenits.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private Double salary;

    @Column(nullable = false)
    private Boolean active = true;

    private String photoFileName;

    private String resumeFileName;
}
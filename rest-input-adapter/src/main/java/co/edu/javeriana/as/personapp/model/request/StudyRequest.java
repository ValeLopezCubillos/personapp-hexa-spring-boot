package co.edu.javeriana.as.personapp.model.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyRequest {
    private Integer person; // Cambiado de String a Integer
    private Integer profession; // Cambiado de String a Integer
    private LocalDate graduationDate;
    private String universityName;
    private String database;
}

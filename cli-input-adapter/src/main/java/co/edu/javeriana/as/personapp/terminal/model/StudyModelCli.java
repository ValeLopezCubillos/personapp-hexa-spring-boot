package co.edu.javeriana.as.personapp.terminal.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyModelCli {
    private PersonaModelCli persona;
    private ProfessionModelCli profesion;
    private LocalDate graduationDate;
    private String universityName;
}

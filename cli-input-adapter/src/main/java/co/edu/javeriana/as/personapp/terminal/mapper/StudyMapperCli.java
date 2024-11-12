package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfessionModelCli;
import co.edu.javeriana.as.personapp.terminal.model.StudyModelCli;

@Mapper
public class StudyMapperCli {

    public StudyModelCli fromDomainToAdapterCli(Study study) {
        PersonaModelCli personModel = null;
        ProfessionModelCli professionModel = null;

        if (study.getPerson() != null) {
            personModel = mapPersonToModel(study.getPerson());
        }
        if (study.getProfession() != null) {
            professionModel = mapProfessionToModel(study.getProfession());
        }

        StudyModelCli studyModelCli = new StudyModelCli();
        studyModelCli.setPersona(personModel);
        studyModelCli.setProfesion(professionModel);
        studyModelCli.setGraduationDate(study.getGraduationDate());
        studyModelCli.setUniversityName(study.getUniversityName());
        return studyModelCli;
    }

    public PersonaModelCli mapPersonToModel(Person person) {
        if (person == null) {
            return null;
        }

        PersonaModelCli personModel = new PersonaModelCli();
        if (person.getIdentification() != null) {
            personModel.setCc(person.getIdentification());
        }
        if (person.getFirstName() != null) {
            personModel.setNombre(person.getFirstName());
        }
        if (person.getLastName() != null) {
            personModel.setApellido(person.getLastName());
        }
        if (person.getGender() != null) {
            personModel.setGenero(person.getGender().toString());
        }
        if (person.getAge() != null) {
            personModel.setEdad(person.getAge());
        }
        return personModel;
    }

    public ProfessionModelCli mapProfessionToModel(Profession profession) {
        if (profession == null) {
            return null;
        }

        ProfessionModelCli professionModel = new ProfessionModelCli();
        if (profession.getIdentification() != null) {
            professionModel.setIdentificacion(profession.getIdentification());
        }
        if (profession.getName() != null) {
            professionModel.setNombre(profession.getName());
        }
        if (profession.getDescription() != null) {
            professionModel.setDescripcion(profession.getDescription());
        }
        return professionModel;
    }
}



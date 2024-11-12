package co.edu.javeriana.as.personapp.mongo.mapper;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import lombok.NonNull;

@Mapper
public class EstudiosMapperMongo {

    @Autowired
    private PersonaMapperMongo personaMapperMongo;

    @Autowired
    private ProfesionMapperMongo profesionMapperMongo;

    public EstudiosDocument fromDomainToAdapter(Study study) {
        if (study == null) {
            throw new IllegalArgumentException("El objeto 'Study' no puede ser nulo.");
        }
    
        EstudiosDocument estudio = new EstudiosDocument();
    
        if (study.getPerson() == null) {
            throw new IllegalArgumentException("El campo 'person' no puede ser nulo en el objeto Study.");
        }
    
        if (study.getProfession() == null) {
            throw new IllegalArgumentException("El campo 'profession' no puede ser nulo en el objeto Study.");
        }
    
        // Validar y asignar los campos necesarios
        estudio.setId(validateId(study.getPerson().getIdentification(), study.getProfession().getIdentification()));
        estudio.setFecha(study.getGraduationDate());
        estudio.setUniver(study.getUniversityName());
        estudio.setCc_per(validatePersona(study.getPerson())); // Asegurarse de asignar correctamente la persona
        estudio.setId_prof(validateProfesion(study.getProfession()));
        return estudio;
    }
    

    private PersonaDocument validatePersona(Person person) {
        if (person != null && person.getIdentification() != null) {
            return personaMapperMongo.fromDomainToAdapter(person);
        }
        return null;
    }

    
    private ProfesionDocument validateProfesion(Profession profesion) {
        if (profesion != null && profesion.getIdentification() != null) {
            return profesionMapperMongo.fromDomainToAdapter(profesion);
        }
        return null;
    }

    private String validateId(Integer identificationPerson, Integer identificationProfession) {
        if (identificationPerson == null || identificationProfession == null) {
            throw new IllegalArgumentException("Los identificadores de persona y profesión no pueden ser nulos.");
        }
        return identificationPerson + "-" + identificationProfession;
    }

   

    public Study fromAdapterToDomain(EstudiosDocument estudiosDocument) {
        if (estudiosDocument == null) {
            throw new IllegalArgumentException("El documento 'EstudiosDocument' no puede ser nulo.");
        }
    
        Study study = new Study();
    
        // Asignar persona si está presente y es válida
        if (estudiosDocument.getId_prof() == null) {
            throw new IllegalArgumentException("El campo 'persona' no puede ser nulo en EstudiosDocument.");
        }
        // Asignar la fecha de graduación y el nombre de la universidad
        study.setGraduationDate(estudiosDocument.getFecha());
        study.setUniversityName(estudiosDocument.getUniver());


        Person person = validatePersona(estudiosDocument.getCc_per());
        if (person != null) {
            study.setPerson(person);
        }

        Profession profession = validateProfesion(estudiosDocument.getId_prof());
        if (profession != null) {
            study.setProfession(profession);
        }
    
        
        return study;
    }
    
    
    private Person validatePersona(PersonaDocument person) {
        if (person == null || person.getId() == null) {
            return null;
        }
        return personaMapperMongo.fromAdapterToDomain(person);
    }

    private Profession validateProfesion(ProfesionDocument profesion) {
        if (profesion == null || profesion.getId() == null) {
            return null;
        }
        return profesionMapperMongo.fromAdapterToDomain(profesion);
    }
    
    
}
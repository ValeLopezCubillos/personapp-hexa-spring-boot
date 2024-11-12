package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import lombok.NonNull;

@Mapper
public class EstudiosMapperMaria {

	@Autowired
	private PersonaMapperMaria personaMapperMaria;

	@Autowired
	private ProfesionMapperMaria profesionMapperMaria;

	public EstudiosEntity fromDomainToAdapter(Study study) {
		if (study == null) {
			throw new IllegalArgumentException("El objeto 'Study' no puede ser nulo.");
		}
	
		// Crear el objeto de clave primaria
		EstudiosEntityPK estudioPK = new EstudiosEntityPK();
		if (study.getPerson() != null && study.getPerson().getIdentification() != null) {
			estudioPK.setCcPer(study.getPerson().getIdentification());
		} else {
			throw new IllegalArgumentException("La persona o su identificación no pueden ser nulas.");
		}
	
		if (study.getProfession() != null && study.getProfession().getIdentification() != null) {
			estudioPK.setIdProf(study.getProfession().getIdentification());
		} else {
			throw new IllegalArgumentException("La profesión o su identificación no pueden ser nulas.");
		}
	
		// Crear el objeto EstudiosEntity y asignar los valores correspondientes
		EstudiosEntity estudio = new EstudiosEntity();
		estudio.setEstudiosEntityPK(estudioPK);
		estudio.setFecha(validateFecha(study.getGraduationDate()));
		estudio.setUniver(validateUniver(study.getUniversityName()));
	
		// Asociar la persona y la profesión (validar y mapear adecuadamente)
		estudio.setPersona(validatepersona(study.getPerson()));
		estudio.setProfesion(validateProfesion(study.getProfession()));
	
		return estudio;
	}

	private PersonaEntity validatepersona(@NonNull Person persona) {
		return persona != null ? personaMapperMaria.fromDomainToAdapter(persona) : new PersonaEntity();
	}


	private ProfesionEntity validateProfesion(@NonNull Profession profesion) {
		return profesion != null ? profesionMapperMaria.fromDomainToAdapter(profesion) : new ProfesionEntity();
	}

	private LocalDate validateFecha(LocalDate graduationDate) {
        return graduationDate;
    }

	private String validateUniver(String universityName) {
		return universityName != null ? universityName : "";
	}

	public Study fromAdapterToDomain(EstudiosEntity estudiosEntity) {
		Study study = new Study();
		study.setPerson(personaMapperMaria.fromAdapterToDomain(estudiosEntity.getPersona()));
		study.setProfession(profesionMapperMaria.fromAdapterToDomain(estudiosEntity.getProfesion()));
		study.setGraduationDate(validateGraduationDate(estudiosEntity.getFecha()));
		study.setUniversityName(validateUniversityName(estudiosEntity.getUniver()));
		return study;
	}

	private LocalDate validateGraduationDate(LocalDate fecha) {
        return fecha;
    }

	private String validateUniversityName(String univer) {
		return univer != null ? univer : "";
	}
}

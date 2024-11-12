package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;

@Mapper
public class StudyMapperRest {

    public StudyResponse fromDomainToAdapterRestMaria(Study study) {
        return fromDomainToAdapterRest(study, "MariaDB");
    }

    public StudyResponse fromDomainToAdapterRestMongo(Study study) {
        return fromDomainToAdapterRest(study, "MongoDB");
    }

    public StudyResponse fromDomainToAdapterRest(Study study, String database) {
        Integer personId = (study.getPerson() != null && study.getPerson().getIdentification() != null) 
                            ? study.getPerson().getIdentification()
                            : null;;
    
        Integer professionId = (study.getProfession() != null && study.getProfession().getIdentification() != null) 
                              ? study.getProfession().getIdentification()
                              : null;;
    
        return new StudyResponse(
                personId,
                professionId,
                study.getGraduationDate(),
                study.getUniversityName(),
                database,
                "OK"
        );
    }

    public Study fromAdapterToDomain(StudyRequest request, Person person, Profession profession) {
        // Asignar las instancias de persona y profesión que ya fueron buscadas
        Study study = new Study();
        study.setPerson(person);
        study.setProfession(profession);
        study.setUniversityName(request.getUniversityName());
        study.setGraduationDate(request.getGraduationDate());
        return study;
    }
    
}

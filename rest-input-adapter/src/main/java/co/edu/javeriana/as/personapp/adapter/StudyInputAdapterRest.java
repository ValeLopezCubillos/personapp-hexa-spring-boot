package co.edu.javeriana.as.personapp.adapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.StudyMapperRest;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class StudyInputAdapterRest {

    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortMongo;

    @Autowired
    private StudyMapperRest studyMapperRest;

    StudyInputPort studyInputPort;
    PersonInputPort personInputPort;
    ProfessionInputPort professionInputPort;

    private String setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        log.info("Database option received: {}", dbOption);

        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<StudyResponse> historial(String database) {
        log.info("Into historial StudyEntity in Input Adapter");
        try {
            setStudyOutputPortInjection(database);
            
            // Usar el mapper adecuado según el tipo de base de datos
            return studyInputPort.findAll().stream()
                    .map(study -> {
                        // Dependiendo de la base de datos, llama al método correspondiente
                        if ("MARIA".equals(database)) {
                            return studyMapperRest.fromDomainToAdapterRestMaria(study);
                        } else if ("MONGO".equals(database)) {
                            return studyMapperRest.fromDomainToAdapterRestMongo(study);
                        } else {
                            return studyMapperRest.fromDomainToAdapterRest(study, "Unknown");
                        }
                    })
                    .collect(Collectors.toList());
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<>();
        }
    }    

    public void eliminarEstudio(String database, Integer personId, Integer professionId) {
        log.info("Into eliminarEstudio in Input Adapter");
        try {
            setStudyOutputPortInjection(database);
            Boolean deleted = studyInputPort.drop(personId, professionId);
            if (!deleted) {
                throw new RuntimeException("No se pudo eliminar el estudio");
            }
        } catch (NoExistException | InvalidOptionException e) {
            log.error("Error deleting study: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar el estudio", e);
        }
    }

    public StudyResponse crearEstudio(StudyRequest request) {
        try {
            // Establecer la salida de la persistencia
            String database = setStudyOutputPortInjection(request.getDatabase());
            log.info("Database option received: {}", database);
    
            // Buscar persona y profesión por sus identificadores usando los input ports
            Person person = personInputPort.findOne(request.getPerson());
            Profession profession = professionInputPort.findOne(request.getProfession());
    
            // Verificar si la persona o la profesión son nulas antes de continuar
            if (person == null) {
                log.error("Person not found for ID: {}", request.getPerson());
                throw new IllegalArgumentException("La persona no existe.");
            }
            if (profession == null) {
                log.error("Profession not found for ID: {}", request.getProfession());
                throw new IllegalArgumentException("La profesión no existe.");
            }
    
            // Crear el estudio asignando la persona y la profesión encontradas
            Study study = studyMapperRest.fromAdapterToDomain(request, person, profession);
    
            // Llamar al inputPort para crear el estudio
            study = studyInputPort.create(study);
    
            // Retornar la respuesta correspondiente según la base de datos
            if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return studyMapperRest.fromDomainToAdapterRestMaria(study);
            } else {
                return studyMapperRest.fromDomainToAdapterRestMongo(study);
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            throw new RuntimeException("Error creating study", e);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            throw new RuntimeException("Unexpected error while creating study", e);
        }
    }    
    
}

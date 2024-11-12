package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mapper.ProfessionMapperRest;
import co.edu.javeriana.as.personapp.model.request.ProfessionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfessionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfessionInputAdapterRest {

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private ProfessionMapperRest professionMapperRest;

    ProfessionInputPort professionInputPort;

    private String setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
        log.info("Database option received: {}", dbOption);

        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<ProfessionResponse> historial(String database) {
        log.info("Into historial ProfessionEntity in Input Adapter");
        try {
            if (setProfessionOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return professionInputPort.findAll().stream().map(professionMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else {
                return professionInputPort.findAll().stream().map(professionMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }

        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<ProfessionResponse>();
        }
    }

    public ProfessionResponse crearProfession(ProfessionRequest request) {
        try {
            String database = setProfessionOutputPortInjection(request.getDatabase());
            Profession profession = professionInputPort.create(professionMapperRest.fromAdapterToDomain(request));

            if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return professionMapperRest.fromDomainToAdapterRestMaria(profession);
            } else {
                return professionMapperRest.fromDomainToAdapterRestMongo(profession);
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    public void eliminarProfession(Integer id, String database) {
        log.info("Eliminando la profesión con ID: {} en la base de datos: {}", id, database);
        try {
            setProfessionOutputPortInjection(database);
            if (professionInputPort.drop(id)) {
                log.info("Profesión eliminada exitosamente.");
            } else {
                log.warn("No se pudo eliminar la profesión con ID: {}. Puede que no exista.", id);
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
        } catch (NoExistException e) {
            log.warn("La profesión con ID: {} no existe.", id);
        }
    }    

    public ProfessionResponse editarProfession(Integer id, ProfessionRequest request) {
        try {
            String database = setProfessionOutputPortInjection(request.getDatabase());
            Profession updatedProfession = professionInputPort.edit(id, professionMapperRest.fromAdapterToDomain(request));
    
            if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return professionMapperRest.fromDomainToAdapterRestMaria(updatedProfession);
            } else {
                return professionMapperRest.fromDomainToAdapterRestMongo(updatedProfession);
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
        } catch (NoExistException e) {
            log.warn("La profesión con ID: {} no existe.", id);
        }
        return null;
    }    

}

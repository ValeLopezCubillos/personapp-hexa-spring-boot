package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mapper.PhoneMapperRest;
import co.edu.javeriana.as.personapp.model.request.PhoneRequest;
import co.edu.javeriana.as.personapp.model.response.PhoneResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PhoneInputAdapterRest {

    @Autowired
    @Qualifier("phoneOutputAdapterMaria")
    private PhoneOutputPort phoneOutputPortMaria;

    @Autowired
    @Qualifier("phoneOutputAdapterMongo")
    private PhoneOutputPort phoneOutputPortMongo;

    @Autowired
    private PhoneMapperRest phoneMapperRest;

    @Autowired
    private PersonInputPort personInputPort; // Asegúrate de tener esta línea

    PhoneInputPort phoneInputPort;

    private String setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
        log.info("Database option received: {}", dbOption);

        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<PhoneResponse> historial(String database) {
        log.info("Into historial PhoneEntity in Input Adapter");
        try {
            if (setPhoneOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return phoneInputPort.findAll().stream().map(phoneMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else {
                return phoneInputPort.findAll().stream().map(phoneMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }

        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<PhoneResponse>();
        }
    }

    public PhoneResponse crearTelefono(PhoneRequest request) {
        log.info("Crear teléfono con datos: {}", request);
        try {
            // Busca al dueño usando la identificación proporcionada en el request
            Person owner = personInputPort.findOne(Integer.parseInt(request.getOwnerId())); // Asegúrate de que `getOwnerId()` exista en PhoneRequest

            if (owner == null) {
                throw new NoExistException("No se encontró una persona con la identificación proporcionada.");
            }

            // Configura la opción de base de datos
            String database = setPhoneOutputPortInjection(request.getDatabase());
            
            // Crea el teléfono y asocia al dueño
            Phone phone = phoneMapperRest.fromAdapterToDomain(request);
            phone.setOwner(owner); // Asocia el dueño al teléfono

            Phone createdPhone = phoneInputPort.create(phone);

            // Devuelve la respuesta correspondiente según la base de datos
            if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return phoneMapperRest.fromDomainToAdapterRestMaria(createdPhone);
            } else {
                return phoneMapperRest.fromDomainToAdapterRestMongo(createdPhone);
            }
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            throw new RuntimeException("Error al crear teléfono: " + e.getMessage(), e);
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            throw new RuntimeException("Error al crear teléfono: " + e.getMessage(), e);
        }
    }

    public void eliminarTelefono(String number, String database) {
        try {
            setPhoneOutputPortInjection(database); // Inyecta el puerto de salida correspondiente
            if (phoneInputPort.drop(number)) { // Llama al método drop para eliminar el teléfono
                log.info("Teléfono eliminado con éxito, Número: {}", number);
            } else {
                throw new NoExistException("Teléfono no encontrado con número: " + number);
            }
        } catch (InvalidOptionException e) {
            log.warn("Opción de base de datos inválida: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NoExistException e) {
            log.warn("Error al eliminar teléfono: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al eliminar teléfono: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        }
    }

    public PhoneResponse editarTelefono(String number, PhoneRequest request) {
        try {
            String database = setPhoneOutputPortInjection(request.getDatabase());
            Phone phone = phoneMapperRest.fromAdapterToDomain(request);
    
            // Buscar la persona usando el ownerId
            Person owner = personInputPort.findOne(Integer.parseInt(request.getOwnerId()));
            if (owner == null) {
                throw new NoExistException("No existe la persona con ID: " + request.getOwnerId());
            }
    
            // Asignar la persona encontrada al objeto Phone
            phone.setOwner(owner);
    
            // Editar el teléfono
            Phone updatedPhone = phoneInputPort.edit(number, phone);
    
            // Mapear la respuesta según la base de datos
            if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return phoneMapperRest.fromDomainToAdapterRestMaria(updatedPhone);
            } else {
                return phoneMapperRest.fromDomainToAdapterRestMongo(updatedPhone);
            }
        } catch (InvalidOptionException e) {
            log.warn("Opción de base de datos inválida: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NoExistException e) {
            log.warn("Error al editar teléfono: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }    
    
}

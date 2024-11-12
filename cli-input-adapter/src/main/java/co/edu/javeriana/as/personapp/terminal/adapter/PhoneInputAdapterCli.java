package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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
import co.edu.javeriana.as.personapp.terminal.mapper.PhoneMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PhoneModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PhoneInputAdapterCli {

    @Autowired
    @Qualifier("phoneOutputAdapterMaria")
    private PhoneOutputPort phoneOutputPortMaria;

    @Autowired
    @Qualifier("phoneOutputAdapterMongo")
    private PhoneOutputPort phoneOutputPortMongo;

    @Autowired
    private PhoneMapperCli phoneMapperCli;

    @Autowired
    private PersonInputPort personInputPort;

    PhoneInputPort phoneInputPort;

    public void setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void historial() {
        log.info("Into historial PhoneEntity in Input Adapter");
        List<PhoneModelCli> phones = phoneInputPort.findAll().stream()
                .map(phone -> {
                    PhoneModelCli phoneModel = phoneMapperCli.fromDomainToAdapterCli(phone);
                    phoneModel.setOwner(phoneMapperCli.mapOwnerToModel(phone.getOwner()));
                    return phoneModel;
                })
                .collect(Collectors.toList());
        phones.forEach(p -> System.out.println(p.toString()));
    }

    public void listarTodosLosTelefonos() {
        // Asegúrate de que el phoneInputPort esté configurado antes de llamar a este método
        List<Phone> phones = phoneInputPort.findAll(); // Usamos phoneInputPort que ha sido inyectado

        if (phones.isEmpty()) {
            System.out.println("No hay teléfonos registrados.");
        } else {
            phones.forEach(phone -> System.out.println(phone.toString()));
        }
    }

    public void crearTelefono(Scanner keyboard) {
        System.out.print("Ingrese el número de teléfono: ");
        String number = keyboard.next();

        System.out.print("Ingrese la compañía telefónica: ");
        String company = keyboard.next();

        System.out.print("Ingrese la identificación del dueño: ");
        String ownerId = keyboard.next();

        try {
            System.out.println("Buscando persona con identificación: " + ownerId);
            // Buscar al dueño del teléfono en la base de datos usando la identificación
            Person owner = personInputPort.findOne(Integer.parseInt(ownerId));

            if (owner != null) {
                // Crear el teléfono y asociarlo al dueño encontrado
                Phone phone = new Phone();
                phone.setNumber(number);
                phone.setCompany(company);
                phone.setOwner(owner);

                // Guardar el teléfono usando el caso de uso
                Phone createdPhone = phoneInputPort.create(phone);
                System.out.println("Teléfono creado con éxito: " + createdPhone);
            } else {
                System.out.println("No se encontró una persona con la identificación proporcionada.");
            }
        } catch (NoExistException e) {
            System.out.println("Error: No se encontró una persona con la identificación proporcionada.");
        }
    }

    public void eliminarTelefono(Scanner keyboard) {
        System.out.print("Ingrese el número de teléfono a eliminar: ");
        String number = keyboard.next();
    
        try {
            boolean deleted = phoneInputPort.drop(number);
            if (deleted) {
                System.out.println("Teléfono eliminado con éxito.");
            } else {
                System.out.println("No se pudo eliminar el teléfono.");
            }
        } catch (NoExistException e) {
            System.out.println("Error: No se encontró un teléfono con el número proporcionado.");
        }
    }
    
    public void editarTelefono(Scanner keyboard) {
        System.out.print("Ingrese el número de teléfono a editar: ");
        String number = keyboard.next();
    
        try {
            // Buscar el teléfono existente en la base de datos
            Phone existingPhone = phoneInputPort.findOne(number);
    
            if (existingPhone != null) {
                System.out.print("Ingrese el nuevo número de teléfono (deje en blanco para mantener el mismo): ");
                String newNumber = keyboard.next();
                if (newNumber.isEmpty()) {
                    newNumber = existingPhone.getNumber(); // Mantener el número actual
                }
    
                System.out.print("Ingrese la nueva compañía telefónica (deje en blanco para mantener la misma): ");
                String newCompany = keyboard.next();
                if (newCompany.isEmpty()) {
                    newCompany = existingPhone.getCompany(); // Mantener la compañía actual
                }
    
                Phone updatedPhone = new Phone();
                updatedPhone.setNumber(newNumber);
                updatedPhone.setCompany(newCompany);
                updatedPhone.setOwner(existingPhone.getOwner()); // Mantener el mismo dueño
    
                // Llamar al caso de uso para editar el teléfono
                Phone phoneEdited = phoneInputPort.edit(existingPhone.getNumber(), updatedPhone);
                System.out.println("Teléfono editado con éxito: " + phoneEdited);
            } else {
                System.out.println("No se encontró un teléfono con el número proporcionado.");
            }
        } catch (NoExistException e) {
            System.out.println("Error: No se encontró un teléfono con el número proporcionado.");
        }
    }
    

} 

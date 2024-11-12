package co.edu.javeriana.as.personapp.terminal.adapter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
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
import co.edu.javeriana.as.personapp.terminal.mapper.StudyMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.StudyModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class StudyInputAdapterCli {

    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortMongo;

    @Autowired
    private StudyMapperCli studyMapperCli;
    @Autowired
    private PersonInputPort personInputPort;

    @Autowired
    private ProfessionInputPort professionInputPort;

    private StudyInputPort studyInputPort;

    public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void historial() {
        log.info("Into historial StudyEntity in Input Adapter");
        List<StudyModelCli> studies = studyInputPort.findAll().stream()
            .map(study -> {
                StudyModelCli studyModel = studyMapperCli.fromDomainToAdapterCli(study);
                studyModel.setPersona(studyMapperCli.mapPersonToModel(study.getPerson()));
                studyModel.setProfesion(studyMapperCli.mapProfessionToModel(study.getProfession()));
                return studyModel;
            })
            .collect(Collectors.toList());
        studies.forEach(p -> System.out.println(p.toString()));
    }
    
    public void listarTodosLosEstudios() {
        List<Study> studies = studyInputPort.findAll();

        if (studies.isEmpty()) {
            System.out.println("No hay estudios registrados.");
        } else {
            studies.forEach(study -> System.out.println(study.toString()));
        }
    }

    public void crearEstudio(Scanner keyboard) {
        System.out.print("Ingrese el DNI de la persona: ");
        String personIdInput = keyboard.next();
    
        System.out.print("Ingrese el ID de la profesión: ");
        String professionIdInput = keyboard.next();
    
        System.out.print("Ingrese la fecha de graduación (YYYY-MM-DD): ");
        String graduationDateInput = keyboard.next();
    
        System.out.print("Ingrese el nombre de la universidad: ");
        String universityName = keyboard.next();
    
        try {
            // Parsear los IDs a enteros
            int personId = Integer.parseInt(personIdInput);
            int professionId = Integer.parseInt(professionIdInput);
    
            // Parsear la fecha de graduación
            LocalDate graduationDate = LocalDate.parse(graduationDateInput);
    
            System.out.println("Buscando persona con DNI: " + personId);
            // Buscar a la persona en la base de datos usando el DNI
            Person persona = personInputPort.findOne(personId);
    
            // Imprimir los datos de la persona para verificar
            if (persona != null) {
                System.out.println("Persona encontrada: " + persona);
            } else {
                System.out.println("No se encontró una persona con el DNI proporcionado.");
            }
    
            System.out.println("Buscando profesión con ID: " + professionId);
            // Buscar la profesión en la base de datos usando el ID
            Profession profesion = professionInputPort.findOne(professionId);
    
            // Imprimir los datos de la profesión para verificar
            if (profesion != null) {
                System.out.println("Profesión encontrada: " + profesion);
            } else {
                System.out.println("No se encontró una profesión con el ID proporcionado.");
            }
    
            // Verificar que tanto persona como profesion fueron encontradas
            if (persona != null && profesion != null) {
                // Crear el objeto Study y asociarlo a la persona y la profesión encontradas
                Study study = new Study();
                study.setPerson(persona);
                study.setProfession(profesion);
                study.setGraduationDate(graduationDate);
                study.setUniversityName(universityName);
                
                System.out.println("Estudio a ser creado: " + study);
                
                // Guardar el estudio usando el caso de uso
                Study createdStudy = studyInputPort.create(study);
                System.out.println("Estudio creado con éxito: " + createdStudy);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: El DNI y el ID de la profesión deben ser números enteros.");
        } catch (DateTimeParseException e) {
            System.out.println("Error: La fecha de graduación no tiene un formato válido (YYYY-MM-DD).");
        } catch (Exception e) {
            System.out.println("Error al crear el estudio: " + e.getMessage());
        }
    }
    
    public void eliminarEstudio(Scanner keyboard) {
        System.out.print("Ingrese el DNI de la persona para eliminar el estudio: ");
        String personIdInput = keyboard.next();
    
        System.out.print("Ingrese el ID de la profesión asociada al estudio: ");
        String professionIdInput = keyboard.next();
    
        try {
            // Parsear los IDs a enteros
            int personId = Integer.parseInt(personIdInput);
            int professionId = Integer.parseInt(professionIdInput);
    
            boolean deleted = studyInputPort.drop(personId, professionId);
            if (deleted) {
                System.out.println("Estudio eliminado con éxito.");
            } else {
                System.out.println("No se pudo eliminar el estudio. Asegúrese de que los IDs proporcionados son correctos.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: El DNI de la persona y el ID de la profesión deben ser números enteros.");
        } catch (NoExistException e) {
            System.out.println("Error: No se encontró un estudio con los IDs proporcionados.");
        }
    }
    
    public void editarEstudio(Scanner keyboard) {
        System.out.print("Ingrese el DNI de la persona para editar el estudio: ");
        String personIdInput = keyboard.next();
    
        System.out.print("Ingrese el ID de la profesión asociada al estudio: ");
        String professionIdInput = keyboard.next();
    
        try {
            // Parsear los IDs a enteros
            int personId = Integer.parseInt(personIdInput);
            int professionId = Integer.parseInt(professionIdInput);
    
            // Buscar el estudio existente en la base de datos
            Study existingStudy = studyInputPort.findOne(personId, professionId);
    
            if (existingStudy != null) {
                System.out.print("Ingrese la nueva fecha de graduación (YYYY-MM-DD) (deje en blanco para mantener la misma): ");
                String newGraduationDateInput = keyboard.next();
                LocalDate newGraduationDate = newGraduationDateInput.isEmpty()
                        ? existingStudy.getGraduationDate()
                        : LocalDate.parse(newGraduationDateInput);
    
                keyboard.nextLine(); // Limpiar el buffer
    
                System.out.print("Ingrese el nuevo nombre de la universidad (deje en blanco para mantener el mismo): ");
                String newUniversityName = keyboard.nextLine();
                if (newUniversityName.isEmpty()) {
                    newUniversityName = existingStudy.getUniversityName();
                }
    
                // Llamar al caso de uso para editar solo los campos editables
                Study studyEdited = studyInputPort.edit(personId, professionId, newGraduationDate, newUniversityName);
                System.out.println("Estudio editado con éxito: " + studyEdited);
            } else {
                System.out.println("No se encontró un estudio con los IDs proporcionados.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: El DNI de la persona y el ID de la profesión deben ser números enteros.");
        } catch (DateTimeParseException e) {
            System.out.println("Error: La nueva fecha de graduación no tiene un formato válido (YYYY-MM-DD).");
        } catch (NoExistException e) {
            System.out.println("Error: No se encontró un estudio con los IDs proporcionados.");
        }
    }
    
    
    
}
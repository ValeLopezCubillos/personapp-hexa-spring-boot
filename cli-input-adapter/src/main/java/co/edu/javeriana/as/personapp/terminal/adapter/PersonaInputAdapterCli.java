package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterCli {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperCli personaMapperCli;

	PersonInputPort personInputPort;

	public void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial1() {
		log.info("Into historial PersonaEntity in Input Adapter");
		List<PersonaModelCli> persona = personInputPort.findAll().stream().map(personaMapperCli::fromDomainToAdapterCli)
					.collect(Collectors.toList());
		persona.forEach(p -> System.out.println(p.toString()));
	}
	public void historial() {
	    log.info("Into historial PersonaEntity in Input Adapter");
	    personInputPort.findAll().stream()
	        .map(personaMapperCli::fromDomainToAdapterCli)
	        .forEach(System.out::println);
	}

	public void eliminarPersona(Integer dni) {
		log.info("Eliminando persona con DNI: {}", dni);
		try {
			boolean eliminado = personInputPort.drop(dni); // Llama al método drop de PersonInputPort
			if (eliminado) {
				log.info("Persona con DNI {} eliminada exitosamente.", dni);
			} else {
				log.warn("No se encontró una persona con DNI {} para eliminar.", dni);
			}
		} catch (NoExistException e) {
			log.warn("Error al eliminar la persona: {}", e.getMessage());
		}
	}

	public void crearPersona() {
		log.info("Creando una nueva persona.");
		Scanner scanner = new Scanner(System.in);

		try {
			System.out.print("Ingrese el DNI: ");
			Integer dni = scanner.nextInt();
			scanner.nextLine(); // Limpiar el buffer

			System.out.print("Ingrese el nombre: ");
			String nombre = scanner.nextLine();

			System.out.print("Ingrese el apellido: ");
			String apellido = scanner.nextLine();

			System.out.print("Ingrese la edad: ");
			Integer edad = scanner.nextInt();
			scanner.nextLine(); // Limpiar el buffer

			System.out.print("Ingrese el género (MALE/FEMALE): ");
			String generoInput = scanner.nextLine();
			Gender genero = Gender.valueOf(generoInput.toUpperCase());

			// Crear la persona con los datos ingresados
			Person persona = new Person(dni, nombre, apellido, genero); // Constructor que no incluye edad
			persona.setAge(edad); // Establecer la edad después

			Person personaCreada = personInputPort.create(persona); // Llamar al método create

			log.info("Persona creada: {}", personaCreada);
		} catch (InputMismatchException e) {
			log.warn("Entrada no válida. Intente nuevamente.");
		} catch (Exception e) {
			log.warn("Error al crear la persona: {}", e.getMessage());
		}
	}

	public void editarPersona() {
		log.info("Editando una persona existente.");
		Scanner scanner = new Scanner(System.in);
	
		try {
			System.out.print("Ingrese el DNI de la persona a editar: ");
			Integer dni = scanner.nextInt();
			scanner.nextLine(); // Limpiar el buffer
	
			// Verificar si la persona existe
			Person personaExistente = personInputPort.findOne(dni);
			if (personaExistente == null) {
				log.warn("No se encontró la persona con DNI: {}", dni);
				return;
			}
	
			// Mostrar datos actuales
			log.info("Datos actuales: {}", personaExistente);
	
			// Solicitar nuevos datos
			System.out.print("Ingrese el nuevo nombre (actual: " + personaExistente.getFirstName() + "): ");
			String nombre = scanner.nextLine();
	
			System.out.print("Ingrese el nuevo apellido (actual: " + personaExistente.getLastName() + "): ");
			String apellido = scanner.nextLine();
	
			System.out.print("Ingrese la nueva edad (actual: " + personaExistente.getAge() + "): ");
			Integer edad = scanner.nextInt();
			scanner.nextLine(); // Limpiar el buffer
	
			System.out.print("Ingrese el nuevo género (actual: " + personaExistente.getGender() + ", MALE/FEMALE): ");
			String generoInput = scanner.nextLine();
			Gender genero = Gender.valueOf(generoInput.toUpperCase());
	
			// Crear un nuevo objeto Person con los datos actualizados
			Person personaActualizada = new Person(dni, nombre, apellido, genero);
			personaActualizada.setAge(edad);
	
			// Llamar al método de edición en el puerto de entrada
			Person personaEditada = personInputPort.edit(dni, personaActualizada);
	
			log.info("Persona editada: {}", personaEditada);
		} catch (InputMismatchException e) {
			log.warn("Entrada no válida. Intente nuevamente.");
		} catch (NoExistException e) {
			log.warn("No se encontró la persona: {}", e.getMessage());
		} catch (Exception e) {
			log.warn("Error al editar la persona: {}", e.getMessage());
		}
	}	


}

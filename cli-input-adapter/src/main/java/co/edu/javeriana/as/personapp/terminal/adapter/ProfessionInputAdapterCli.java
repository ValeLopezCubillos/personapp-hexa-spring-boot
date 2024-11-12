package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
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
import co.edu.javeriana.as.personapp.terminal.mapper.ProfessionMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfessionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfessionInputAdapterCli {

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private ProfessionMapperCli professionMapperCli;

    ProfessionInputPort professionInputPort;

    public void setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void historial() {
        log.info("Into historial ProfessionEntity in Input Adapter");
        List<ProfessionModelCli> professions = professionInputPort.findAll().stream()
                .map(professionMapperCli::fromDomainToAdapterCli)
                .collect(Collectors.toList());
        professions.forEach(p -> System.out.println(p.toString()));
    }

    public void crearProfesion() {
        log.info("Creando una nueva profesión.");
        Scanner scanner = new Scanner(System.in);

        try {
            // Solicitar datos de la profesión
            System.out.print("Ingrese el ID de la profesión: ");
            Integer id = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            System.out.print("Ingrese el nombre de la profesión: ");
            String nombre = scanner.nextLine();

            System.out.print("Ingrese la descripción de la profesión: ");
            String descripcion = scanner.nextLine();

            // Crear un nuevo objeto Profession
            Profession nuevaProfesion = new Profession(id, nombre, descripcion, null); // Lista vacía de estudios por ahora

            // Llamar al método de creación en el puerto de entrada
            Profession profesionCreada = professionInputPort.create(nuevaProfesion);

            log.info("Profesión creada: {}", profesionCreada);
        } catch (InputMismatchException e) {
            log.warn("Entrada no válida. Intente nuevamente.");
        } catch (Exception e) {
            log.warn("Error al crear la profesión: {}", e.getMessage());
        }
    }

    public void eliminarProfesion() {
        log.info("Eliminando una profesión.");
        Scanner scanner = new Scanner(System.in);

        try {
            // Solicitar el ID de la profesión a eliminar
            System.out.print("Ingrese el ID de la profesión a eliminar: ");
            Integer id = scanner.nextInt();

            // Llamar al método de eliminación en el puerto de entrada
            Boolean eliminado = professionInputPort.drop(id);

            if (eliminado) {
                log.info("Profesión con ID {} eliminada exitosamente.", id);
            } else {
                log.warn("No se pudo eliminar la profesión con ID {}.", id);
            }
        } catch (InputMismatchException e) {
            log.warn("Entrada no válida. Intente nuevamente.");
        } catch (NoExistException e) {
            log.warn("Error: {}", e.getMessage());
        } catch (Exception e) {
            log.warn("Error al eliminar la profesión: {}", e.getMessage());
        }
    }

    public void editarProfesion() {
        log.info("Editando una profesión.");
        Scanner scanner = new Scanner(System.in);
    
        try {
            // Solicitar el ID de la profesión a editar
            System.out.print("Ingrese el ID de la profesión a editar: ");
            Integer id = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer
    
            // Solicitar los nuevos datos de la profesión
            System.out.print("Ingrese el nuevo nombre de la profesión: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Ingrese la nueva descripción de la profesión: ");
            String descripcion = scanner.nextLine();
    
            // Crear una nueva instancia de Profession con los datos ingresados
            Profession profession = new Profession(id, nombre, descripcion, null); // Lista vacía de estudios por ahora
    
            // Llamar al método de edición en el puerto de entrada
            Profession editedProfession = professionInputPort.edit(id, profession);
            
            log.info("Profesión editada exitosamente: {}", editedProfession);
        } catch (InputMismatchException e) {
            log.warn("Entrada no válida. Intente nuevamente.");
        } catch (NoExistException e) {
            log.warn("Error: {}", e.getMessage());
        } catch (Exception e) {
            log.warn("Error al editar la profesión: {}", e.getMessage());
        }
    }    


}

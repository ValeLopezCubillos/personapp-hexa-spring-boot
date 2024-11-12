package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.PhoneInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PhoneMenu {

    private static final int OPCION_REGRESAR_MODULOS = 0;
    private static final int PERSISTENCIA_MARIADB = 1;
    private static final int PERSISTENCIA_MONGODB = 2;

    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_VER_TODO = 1;
    private static final int OPCION_CREAR_TELEFONO = 2; 
    private static final int OPCION_ELIMINAR_TELEFONO = 3;
    private static final int OPCION_EDITAR_TELEFONO = 4;
    // mas opciones

    public void iniciarMenu(PhoneInputAdapterCli phoneInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                mostrarMenuMotorPersistencia();
                int opcion = leerOpcion(keyboard);
                switch (opcion) {
                case OPCION_REGRESAR_MODULOS:
                    isValid = true;
                    break;
                case PERSISTENCIA_MARIADB:
                    phoneInputAdapterCli.setPhoneOutputPortInjection("MARIA");
                    menuOpciones(phoneInputAdapterCli, keyboard);
                    break;
                case PERSISTENCIA_MONGODB:
                    phoneInputAdapterCli.setPhoneOutputPortInjection("MONGO");
                    menuOpciones(phoneInputAdapterCli, keyboard);
                    break;
                default:
                    log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    private void menuOpciones(PhoneInputAdapterCli phoneInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                mostrarMenuOpciones();
                int opcion = leerOpcion(keyboard);
                switch (opcion) {
                case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
                    isValid = true;
                    break;
                case OPCION_VER_TODO:
                    phoneInputAdapterCli.listarTodosLosTelefonos();
                    break;
                case OPCION_CREAR_TELEFONO:  
                    phoneInputAdapterCli.crearTelefono(keyboard);
                    break;
                case OPCION_ELIMINAR_TELEFONO:  
                    phoneInputAdapterCli.eliminarTelefono(keyboard);
                    break;
                case OPCION_EDITAR_TELEFONO: 
                    phoneInputAdapterCli.editarTelefono(keyboard);
                    break;
                // mas opciones
                default:
                    log.warn("La opción elegida no es válida.");
                }
            } catch (InputMismatchException e) {
                log.warn("Solo se permiten números.");
                keyboard.next(); // Limpiar entrada incorrecta
            }
        } while (!isValid);
    }

    private void mostrarMenuOpciones() {
        System.out.println("----------------------");
        System.out.println(OPCION_VER_TODO + " para ver todos los teléfonos");
        System.out.println(OPCION_CREAR_TELEFONO + " para crear un teléfono");
        System.out.println(OPCION_ELIMINAR_TELEFONO + " para eliminar un teléfono");
        System.out.println(OPCION_EDITAR_TELEFONO + " para editar un teléfono");
        // implementar otras opciones
        System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
    }

    private void mostrarMenuMotorPersistencia() {
        System.out.println("----------------------");
        System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
        System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
        System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
    }

    private int leerOpcion(Scanner keyboard) {
        try {
            System.out.print("Ingrese una opción: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten números.");
            keyboard.next(); // Limpiar entrada incorrecta
            return leerOpcion(keyboard);
        }
    }
}

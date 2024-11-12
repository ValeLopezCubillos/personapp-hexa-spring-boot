package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.javeriana.as.personapp.adapter.PhoneInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.request.PhoneRequest;
import co.edu.javeriana.as.personapp.model.response.PhoneResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/phone")
public class PhoneControllerV1 {

    @Autowired
    private PhoneInputAdapterRest phoneInputAdapterRest;

    @Operation(summary = "Obtener lista de teléfonos desde una base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron teléfonos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PhoneResponse> phones(@PathVariable String database) {
        log.info("Into phones REST API");
        return phoneInputAdapterRest.historial(database.toUpperCase());
    }

    @Operation(summary = "Crear un nuevo teléfono")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Teléfono creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhoneResponse> crearTelefono(@RequestBody PhoneRequest request) {
        log.info("Crear teléfono desde API REST");
        PhoneResponse createdPhoneResponse = phoneInputAdapterRest.crearTelefono(request);
        return new ResponseEntity<>(createdPhoneResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar un teléfono")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Teléfono eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Teléfono no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping(path = "/{number}/{database}")
    public ResponseEntity<Void> eliminarTelefono(@PathVariable String number, @PathVariable String database) {
        log.info("Método eliminarTelefono en el controller del API, Número: {}", number);
        phoneInputAdapterRest.eliminarTelefono(number, database.toUpperCase());
        return ResponseEntity.noContent().build(); // Devuelve un estado 204 No Content
    }

    @Operation(summary = "Editar un teléfono existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Teléfono editado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "404", description = "Teléfono no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping(path = "/{number}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhoneResponse> editarTelefono(
            @PathVariable String number,
            @RequestBody PhoneRequest request) {

        log.info("Método editarTelefono en el controller del API, número: {}", number);
        PhoneResponse response = phoneInputAdapterRest.editarTelefono(number, request);

        if (response != null) {
            return ResponseEntity.ok(response); // Devuelve el teléfono editado con estado 200 (OK)
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // En caso de error
        }
    }


}

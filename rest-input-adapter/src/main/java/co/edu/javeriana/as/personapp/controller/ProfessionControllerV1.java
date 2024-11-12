package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.javeriana.as.personapp.adapter.ProfessionInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.ProfessionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfessionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/profession")
public class ProfessionControllerV1 {

    @Autowired
    private ProfessionInputAdapterRest professionInputAdapterRest;

    @Operation(summary = "Obtener lista de profesiones desde una base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron profesiones"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProfessionResponse> professions(@PathVariable String database) {
        log.info("Into professions REST API");
        return professionInputAdapterRest.historial(database.toUpperCase());
    }

    @Operation(summary = "Crear una nueva profesión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profesión creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfessionResponse> crearProfession(@RequestBody ProfessionRequest request) {
        log.info("Método crearProfession en el controller del API.");
        ProfessionResponse response = professionInputAdapterRest.crearProfession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Eliminar una profesión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profesión eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Profesión no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping(path = "/{id}/{database}")
    public ResponseEntity<Void> eliminarProfession(@PathVariable Integer id, @PathVariable String database) {
        log.info("Método eliminarProfession en el controller del API.");
        professionInputAdapterRest.eliminarProfession(id, database.toUpperCase());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Editar una profesión existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profesión editada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Profesión no encontrada"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfessionResponse> editarProfession(@PathVariable Integer id, @RequestBody ProfessionRequest request) {
        log.info("Método editarProfession en el controller del API para ID: {}", id);
        ProfessionResponse response = professionInputAdapterRest.editarProfession(id, request);
        
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}

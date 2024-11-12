package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import co.edu.javeriana.as.personapp.adapter.StudyInputAdapterRest;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.StudyMapperRest;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/study")
public class StudyControllerV1 {

    @Autowired
    private StudyInputAdapterRest studyInputAdapterRest;

    @Operation(summary = "Obtener lista de estudios desde una base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron estudios"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StudyResponse> estudios(@PathVariable String database) {
        log.info("Into estudios REST API");
        return studyInputAdapterRest.historial(database.toUpperCase());
    }
    
    @Operation(summary = "Eliminar un estudio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Estudio eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Estudio no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping(path = "/{database}/{personId}/{professionId}")
    public ResponseEntity<Void> eliminarEstudio(
            @PathVariable String database,
            @PathVariable Integer personId,
            @PathVariable Integer professionId) {
        log.info("Into eliminarEstudio REST API");
        studyInputAdapterRest.eliminarEstudio(database.toUpperCase(), personId, professionId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Crear un nuevo estudio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estudio creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudyResponse> crearEstudio(@RequestBody StudyRequest request) {
        log.info("Método crearEstudio en el controller del API.");
        StudyResponse response = studyInputAdapterRest.crearEstudio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}

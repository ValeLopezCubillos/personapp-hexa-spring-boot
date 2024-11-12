package co.edu.javeriana.as.personapp.application.usecase;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class StudyUseCase implements StudyInputPort {

    private StudyOutputPort studyPersistence;

    public StudyUseCase(@Qualifier("studyOutputAdapterMaria") StudyOutputPort studyPersistence) {
        this.studyPersistence = studyPersistence;
    }

    @Override
    public void setPersistence(StudyOutputPort studyPersistence) {
        this.studyPersistence = studyPersistence;
    }

    @Override
    public Study create(Study study) {
        log.debug("Into create on Application Domain");
        return studyPersistence.save(study);
    }

    @Override
    public Study edit(int personId, int professionId, LocalDate graduationDate, String universityName) throws NoExistException {
    // Verificar si el estudio existe en la base de datos usando personId y professionId
    Optional<Study> existingStudyOpt = studyPersistence.findById(personId, professionId);

    if (existingStudyOpt.isPresent()) {
        // Obtener el estudio existente
        Study existingStudy = existingStudyOpt.get();

        // Actualizar los campos permitidos
        existingStudy.setGraduationDate(graduationDate);
        existingStudy.setUniversityName(universityName);

        // Guardar el estudio actualizado en la base de datos
        return studyPersistence.save(existingStudy);
    }

    // Lanzar excepción si el estudio no existe
    throw new NoExistException(
            "El estudio con personId " + personId + " y professionId " + professionId + " no existe en la base de datos, no se puede editar.");
}


    @Override
    public Boolean drop(Integer personId, Integer professionId) throws NoExistException {
        if (studyPersistence.findById(personId, professionId).isPresent()) {
            return studyPersistence.delete(personId, professionId);
        }
        throw new NoExistException(
                "The study with personId " + personId + " and professionId " + professionId + " does not exist in the database, cannot be dropped");
    }

    @Override
    public List<Study> findAll() {
        log.info("Output: " + studyPersistence.getClass());
        return studyPersistence.find();
    }

    @Override
    public Study findOne(Integer personId, Integer professionId) throws NoExistException {
        return studyPersistence.findById(personId, professionId)
                .orElseThrow(() -> new NoExistException("The study with personId " + personId + " and professionId " + professionId + " does not exist in the database"));
    }

    @Override
    public Integer count() {
        return findAll().size();
    }
}
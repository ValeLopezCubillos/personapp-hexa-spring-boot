package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.StudyRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMaria")
@Transactional
public class StudyOutputAdapterMaria implements StudyOutputPort {

    @Autowired
    private StudyRepositoryMaria estudiosRepositoryMaria;

    @Autowired
    private EstudiosMapperMaria estudiosMapperMaria;

    @Override
    public Study save(Study study) {
        log.debug("Into save on Adapter MariaDB");
        EstudiosEntity persistedEstudios = estudiosRepositoryMaria.save(estudiosMapperMaria.fromDomainToAdapter(study));
        return estudiosMapperMaria.fromAdapterToDomain(persistedEstudios);
    }

    @Override
    public Boolean delete(Integer personId, Integer professionId) {
        log.debug("Into delete on Adapter MariaDB");
        estudiosRepositoryMaria.deleteById(new EstudiosEntityPK(professionId, personId));
        return estudiosRepositoryMaria.findById(new EstudiosEntityPK(professionId, personId)).isEmpty();
    }

    @Override
    public List<Study> find() {
        log.debug("Into find on Adapter MariaDB");
        return estudiosRepositoryMaria.findAll().stream()
                .map(estudiosMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Study> findById(Integer personId, Integer professionId) {
        log.debug("Into findById on Adapter MariaDB");
        return estudiosRepositoryMaria.findById(new EstudiosEntityPK(professionId, personId))
                .map(estudiosMapperMaria::fromAdapterToDomain);
    }
}
package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.StudyRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMongo")
public class StudyOutputAdapterMongo implements StudyOutputPort {

    @Autowired
    private StudyRepositoryMongo estudiosRepositoryMongo;

    @Autowired
    private EstudiosMapperMongo estudiosMapperMongo;

    @Override
    public Study save(Study study) {
        log.debug("Into save on Adapter MongoDB");
        EstudiosDocument persistedEstudios = estudiosRepositoryMongo.save(estudiosMapperMongo.fromDomainToAdapter(study));
        return estudiosMapperMongo.fromAdapterToDomain(persistedEstudios);
    }

    @Override
    public Boolean delete(Integer personId, Integer professionId) {
        log.debug("Into delete on Adapter MongoDB");
        String compositeId = personId + "-" + professionId;
        estudiosRepositoryMongo.deleteById(compositeId);
        return estudiosRepositoryMongo.findById(compositeId).isEmpty();
    }

    @Override
    public List<Study> find() {
        log.debug("Into find on Adapter MongoDB");
        return estudiosRepositoryMongo.findAll().stream()
                .map(estudiosMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Study> findById(Integer personId, Integer professionId) {
        log.debug("Into findById on Adapter MongoDB");
        String compositeId = personId + "-" + professionId;
        return estudiosRepositoryMongo.findById(compositeId)
                .map(estudiosMapperMongo::fromAdapterToDomain);
    }
    
}

package co.edu.javeriana.as.personapp.application.port.out;

import java.util.List;
import java.util.Optional;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.domain.Study;

@Port
public interface StudyOutputPort {
    
    public Study save(Study study);

    public Boolean delete(Integer personId, Integer professionId);

    public List<Study> find();

    public Optional<Study> findById(Integer personId, Integer professionId);
}


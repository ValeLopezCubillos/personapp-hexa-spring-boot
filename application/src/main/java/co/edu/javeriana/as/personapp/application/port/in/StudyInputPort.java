package co.edu.javeriana.as.personapp.application.port.in;

import java.time.LocalDate;
import java.util.List;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;

@Port
public interface StudyInputPort {
    
    public void setPersistence(StudyOutputPort studyPersistence);

    public Study create(Study study);

    public Study edit(int personId, int professionId, LocalDate graduationDate,  String universityName) throws NoExistException;

    public Boolean drop(Integer personId, Integer professionId) throws NoExistException;

    public List<Study> findAll();

    public Study findOne(Integer personId, Integer professionId) throws NoExistException;

    public Integer count();
}
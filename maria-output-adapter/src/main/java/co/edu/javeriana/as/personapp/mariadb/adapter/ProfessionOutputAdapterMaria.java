package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.ProfesionMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfessionRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("professionOutputAdapterMaria")
@Transactional
public class ProfessionOutputAdapterMaria implements ProfessionOutputPort {

    @Autowired
    private ProfessionRepositoryMaria profesionRepositoryMaria;

    @Autowired
    private ProfesionMapperMaria profesionMapperMaria;

    @Override
    public Profession save(Profession profession) {
        log.debug("Into save on Adapter MariaDB");
        ProfesionEntity persistedProfession = profesionRepositoryMaria.save(profesionMapperMaria.fromDomainToAdapter(profession));
        return profesionMapperMaria.fromAdapterToDomain(persistedProfession);
    }

    @Override
    public Boolean delete(Integer id) {
        log.debug("Into delete on Adapter MariaDB");
        profesionRepositoryMaria.deleteById(id);
        return profesionRepositoryMaria.findById(id).isEmpty();
    }

    @Override
    public List<Profession> find() {
        log.debug("Into find on Adapter MariaDB");
        return profesionRepositoryMaria.findAll().stream()
                .map(profesionMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Profession findById(Integer id) {
        log.debug("Into findById on Adapter MariaDB");
        return profesionRepositoryMaria.findById(id)
                .map(profesionMapperMaria::fromAdapterToDomain)
                .orElse(null);
    }
}

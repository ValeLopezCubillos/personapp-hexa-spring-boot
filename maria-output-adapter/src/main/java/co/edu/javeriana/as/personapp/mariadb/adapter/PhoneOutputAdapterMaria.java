package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.PhoneRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("phoneOutputAdapterMaria")
public class PhoneOutputAdapterMaria implements PhoneOutputPort {

    @Autowired
    private PhoneRepositoryMaria telefonoRepositoryMaria;

    @Autowired
    private TelefonoMapperMaria telefonoMapperMaria;

    @Override
    public Phone save(Phone phone) {
        log.debug("Saving phone in MariaDB");
        TelefonoEntity telefonoEntity = telefonoMapperMaria.fromDomainToAdapter(phone);
        TelefonoEntity savedEntity = telefonoRepositoryMaria.save(telefonoEntity);
        return telefonoMapperMaria.fromAdapterToDomain(savedEntity);
    }

    @Override
    public Boolean delete(String number) {
        log.debug("Deleting phone with number: {} from MariaDB", number);
        if (telefonoRepositoryMaria.existsById(number)) {
            telefonoRepositoryMaria.deleteById(number);
            return true;
        }
        return false;
    }

    @Override
    public List<Phone> find() {
        log.debug("Finding all phones in MariaDB");
        return telefonoRepositoryMaria.findAll().stream()
                .map(telefonoMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Phone findByNumber(String number) {
        log.debug("Finding phone with number: {} in MariaDB", number);
        return telefonoRepositoryMaria.findById(number)
                .map(telefonoMapperMaria::fromAdapterToDomain)
                .orElse(null);
    }
}

package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.TelefonoMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.PhoneRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("phoneOutputAdapterMongo")
public class PhoneOutputAdapterMongo implements PhoneOutputPort {

    @Autowired
    private PhoneRepositoryMongo telefonoRepositoryMongo;

    @Autowired
    private TelefonoMapperMongo telefonoMapperMongo;

    @Override
    public Phone save(Phone phone) {
        log.debug("Saving phone in MongoDB");
        TelefonoDocument telefonoDocument = telefonoMapperMongo.fromDomainToAdapter(phone);
        TelefonoDocument savedDocument = telefonoRepositoryMongo.save(telefonoDocument);
        return telefonoMapperMongo.fromAdapterToDomain(savedDocument);
    }

    @Override
    public Boolean delete(String number) {
        log.debug("Deleting phone with number: {} from MongoDB", number);
        if (telefonoRepositoryMongo.existsById(number)) {
            telefonoRepositoryMongo.deleteById(number);
            return true;
        }
        return false;
    }

    @Override
    public List<Phone> find() {
        log.debug("Finding all phones in MongoDB");
        return telefonoRepositoryMongo.findAll().stream()
                .map(telefonoMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Phone findByNumber(String number) {
        log.debug("Finding phone with number: {} in MongoDB", number);
        return telefonoRepositoryMongo.findById(number)
                .map(telefonoMapperMongo::fromAdapterToDomain)
                .orElse(null);
    }
}

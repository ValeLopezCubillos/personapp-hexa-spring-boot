package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.model.request.PhoneRequest;
import co.edu.javeriana.as.personapp.model.response.PhoneResponse;

@Mapper
public class PhoneMapperRest {

    public PhoneResponse fromDomainToAdapterRestMaria(Phone phone) {
        return fromDomainToAdapterRest(phone, "MariaDB");
    }

    public PhoneResponse fromDomainToAdapterRestMongo(Phone phone) {
        return fromDomainToAdapterRest(phone, "MongoDB");
    }

    public PhoneResponse fromDomainToAdapterRest(Phone phone, String database) {
        return new PhoneResponse(
                phone.getNumber(),
                phone.getCompany(),
                phone.getOwner() != null ? phone.getOwner().getIdentification().toString() : null,
                database,
                "OK"
        );
    }

    public Phone fromAdapterToDomain(PhoneRequest request) {
        Person owner = null;
        if (request.getOwnerId() != null) {
            try {
                owner = new Person();
                owner.setIdentification(Integer.parseInt(request.getOwnerId()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Owner ID debe ser un número válido.", e);
            }
        }

        return new Phone(
                request.getNumber(),
                request.getCompany(),
                owner
        );
    }
}
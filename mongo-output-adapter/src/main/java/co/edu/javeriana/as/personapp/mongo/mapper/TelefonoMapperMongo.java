package co.edu.javeriana.as.personapp.mongo.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;
import lombok.NonNull;

@Mapper
public class TelefonoMapperMongo {

    @Autowired
    private PersonaMapperMongo personaMapperMongo;

    public TelefonoDocument fromDomainToAdapter(Phone phone) {
        if (phone == null) {
            throw new IllegalArgumentException("El objeto 'Phone' no puede ser nulo.");
        }

        TelefonoDocument telefonoDocument = new TelefonoDocument();
        if (phone.getNumber() == null || phone.getNumber().isEmpty()) {
            throw new IllegalArgumentException("El campo 'number' no puede ser nulo o vacío en el objeto Phone.");
        }
        telefonoDocument.setNum(phone.getNumber()); // Usar num para el número de teléfono
        telefonoDocument.setOper(phone.getCompany());
        telefonoDocument.setDuenio(validateDuenio(phone.getOwner())); // Relacionar correctamente con el dueño

        return telefonoDocument;
    }

    private PersonaDocument validateDuenio(Person owner) {
        if (owner != null && owner.getIdentification() != null) {
            return personaMapperMongo.fromDomainToAdapter(owner);
        }
        return null;
    }

    public Phone fromAdapterToDomain(TelefonoDocument telefonoDocument) {
        if (telefonoDocument == null) {
            throw new IllegalArgumentException("El documento 'TelefonoDocument' no puede ser nulo.");
        }

        Phone phone = new Phone();
        if (telefonoDocument.getNum() == null) {
            throw new IllegalArgumentException("El campo 'num' no puede ser nulo en TelefonoDocument.");
        }
        phone.setNumber(telefonoDocument.getNum()); // Usar num para el número de teléfono
        phone.setCompany(telefonoDocument.getOper());

        Person owner = validateOwner(telefonoDocument.getDuenio());
        if (owner != null) {
            phone.setOwner(owner); // Solo asignar si owner no es null
        }

        return phone;
    }

    private Person validateOwner(PersonaDocument duenio) {
        if (duenio == null || duenio.getId() == null) {
            return null; // Devolver null si el dueño no está presente o no tiene identificación
        }
        return personaMapperMongo.fromAdapterToDomain(duenio);
    }
}
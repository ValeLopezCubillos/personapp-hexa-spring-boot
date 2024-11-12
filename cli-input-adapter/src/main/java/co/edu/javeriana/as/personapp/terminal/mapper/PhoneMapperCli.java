package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.model.PhoneModelCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;

@Mapper
public class PhoneMapperCli {

    public PhoneModelCli fromDomainToAdapterCli(Phone phone) {
        PersonaModelCli ownerModel = null;
        if (phone.getOwner() != null) {
            ownerModel = mapOwnerToModel(phone.getOwner());
        }
        PhoneModelCli phoneModel = new PhoneModelCli();
        phoneModel.setNumber(phone.getNumber());
        phoneModel.setCompany(phone.getCompany());
        phoneModel.setOwner(ownerModel); // Ahora establecerá el dueño solo si no es null
        return phoneModel;
    }

    public PersonaModelCli mapOwnerToModel(Person owner) {
        if (owner == null) {
            return null;
        }

        PersonaModelCli ownerModel = new PersonaModelCli();
        if (owner.getIdentification() != null) {
            ownerModel.setCc(owner.getIdentification());
        }
        if (owner.getFirstName() != null) {
            ownerModel.setNombre(owner.getFirstName());
        }
        if (owner.getLastName() != null) {
            ownerModel.setApellido(owner.getLastName());
        }
        if (owner.getGender() != null) {
            ownerModel.setGenero(owner.getGender().toString());
        }
        if (owner.getAge() != null) {
            ownerModel.setEdad(owner.getAge());
        }
        return ownerModel;
    }
}

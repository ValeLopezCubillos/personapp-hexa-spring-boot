package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import java.util.Collections;
import java.util.List;

@Mapper
public class PersonaMapperRest {

	public PersonaResponse fromDomainToAdapterRestMaria(Person person) {
		return fromDomainToAdapterRest(person, "MariaDB");
	}

	public PersonaResponse fromDomainToAdapterRestMongo(Person person) {
		return fromDomainToAdapterRest(person, "MongoDB");
	}

	public PersonaResponse fromDomainToAdapterRest(Person person, String database) {
		return new PersonaResponse(
				person.getIdentification() + "",
				person.getFirstName(),
				person.getLastName(),
				person.getAge() + "",
				person.getGender().toString(),
				database,
				"OK");
	}

	public Person fromAdapterToDomain(PersonaRequest request) {
		Gender gender;

		// Mapeo del campo sex a Gender
		if ("M".equalsIgnoreCase(request.getSex())) {
			gender = Gender.MALE;
		} else if ("F".equalsIgnoreCase(request.getSex())) {
			gender = Gender.FEMALE;
		} else {
			gender = Gender.OTHER;
		}

		// Conversión de DNI (String) a Integer para el constructor de Person
		Integer identification;
		try {
			identification = Integer.parseInt(request.getDni());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("DNI debe ser un número válido.", e);
		}

		// Crear las listas explícitamente con el tipo correcto
		List<Phone> phoneNumbers = Collections.emptyList();
		List<Study> studies = Collections.emptyList();

		return new Person(
				identification,
				request.getFirstName(),
				request.getLastName(),
				gender, // Género mapeado
				request.getAge() != null ? Integer.parseInt(request.getAge()) : null, // Convertimos edad a Integer si
																						// no es nulo
				phoneNumbers, // Lista vacía de teléfonos
				studies // Lista vacía de estudios
		);
	}

}

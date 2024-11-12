package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.model.request.ProfessionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfessionResponse;

@Mapper
public class ProfessionMapperRest {

    public ProfessionResponse fromDomainToAdapterRestMaria(Profession profession) {
        return fromDomainToAdapterRest(profession, "MariaDB");
    }

    public ProfessionResponse fromDomainToAdapterRestMongo(Profession profession) {
        return fromDomainToAdapterRest(profession, "MongoDB");
    }

    public ProfessionResponse fromDomainToAdapterRest(Profession profession, String database) {
        return new ProfessionResponse(
                profession.getIdentification() + "",
                profession.getName(),
                profession.getDescription(),
                database,
                "OK"
        );
    }

    public Profession fromAdapterToDomain(ProfessionRequest request) {
        Integer identification;
        try {
            identification = Integer.parseInt(request.getIdentification());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID debe ser un número válido.", e);
        }

        return new Profession(
                identification,
                request.getName(),
                request.getDescription(),
                null // Lista vacía de estudios por ahora
        );
    }
}

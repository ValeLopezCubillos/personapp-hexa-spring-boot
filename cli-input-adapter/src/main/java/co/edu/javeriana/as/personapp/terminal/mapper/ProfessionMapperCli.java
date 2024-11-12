package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.model.ProfessionModelCli;

@Mapper
public class ProfessionMapperCli {

    public ProfessionModelCli fromDomainToAdapterCli(Profession profession) {
        ProfessionModelCli professionModelCli = new ProfessionModelCli();
        professionModelCli.setIdentificacion(profession.getIdentification());
        professionModelCli.setNombre(profession.getName());
        professionModelCli.setDescripcion(profession.getDescription());
        return professionModelCli;
    }

    public Profession fromAdapterToDomain(ProfessionModelCli modelCli) {
        return new Profession(
                modelCli.getIdentificacion(),
                modelCli.getNombre(),
                modelCli.getDescripcion(),
                null // Lista vacía de estudios por ahora
        );
    }
}

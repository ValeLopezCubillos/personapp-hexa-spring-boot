package co.edu.javeriana.as.personapp.mariadb.entity;

import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.mapper.PersonaMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.mapper.ProfesionMapperMaria;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author aasanchez
 */
@Entity
@Table(name = "estudios", catalog = "persona_db", schema = "")
@NamedQueries({
        @NamedQuery(name = "EstudiosEntity.findAll", query = "SELECT e FROM EstudiosEntity e"),
        @NamedQuery(name = "EstudiosEntity.findByIdProf", query = "SELECT e FROM EstudiosEntity e WHERE e.estudiosEntityPK.idProf = :idProf"),
        @NamedQuery(name = "EstudiosEntity.findByCcPer", query = "SELECT e FROM EstudiosEntity e WHERE e.estudiosEntityPK.ccPer = :ccPer"),
        @NamedQuery(name = "EstudiosEntity.findByFecha", query = "SELECT e FROM EstudiosEntity e WHERE e.fecha = :fecha"),
        @NamedQuery(name = "EstudiosEntity.findByUniver", query = "SELECT e FROM EstudiosEntity e WHERE e.univer = :univer")
})
@Mapper
public class EstudiosEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EstudiosEntityPK estudiosEntityPK;
    @Basic(optional = false)
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
    @Basic(optional = false)
    @Column(name = "univer", nullable = false, length = 50)
    private String univer;
    @JoinColumn(name = "cc_per", referencedColumnName = "cc", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PersonaEntity persona;
    @JoinColumn(name = "id_prof", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProfesionEntity profesion;

    public EstudiosEntity() {
    }

    public EstudiosEntity(EstudiosEntityPK estudiosEntityPK) {
        this.estudiosEntityPK = estudiosEntityPK;
    }

    public EstudiosEntity(EstudiosEntityPK estudiosEntityPK, LocalDate fecha, String univer) {
        this.estudiosEntityPK = estudiosEntityPK;
        this.fecha = fecha;
        this.univer = univer;
    }

    public EstudiosEntityPK getEstudiosEntityPK() {
        return estudiosEntityPK;
    }

    public void setEstudiosEntityPK(EstudiosEntityPK estudiosEntityPK) {
        this.estudiosEntityPK = estudiosEntityPK;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getUniver() {
        return univer;
    }

    public void setUniver(String univer) {
        this.univer = univer;
    }

    public PersonaEntity getPersona() {
        return persona;
    }

    public void setPersona(PersonaEntity persona) {
        this.persona = persona;
    }

    public ProfesionEntity getProfesion() {
        return profesion;
    }

    public void setProfesion(ProfesionEntity profesion) {
        this.profesion = profesion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estudiosEntityPK != null ? estudiosEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstudiosEntity)) {
            return false;
        }
        EstudiosEntity other = (EstudiosEntity) object;
        if ((this.estudiosEntityPK == null && other.estudiosEntityPK != null)
                || (this.estudiosEntityPK != null && !this.estudiosEntityPK.equals(other.estudiosEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EstudiosEntity [estudiosEntityPK=" + estudiosEntityPK + ", fecha=" + fecha + ", univer=" + univer + "]";
    }
}

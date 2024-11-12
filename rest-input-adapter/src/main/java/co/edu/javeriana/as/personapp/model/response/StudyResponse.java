package co.edu.javeriana.as.personapp.model.response;

import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import java.time.LocalDate;

public class StudyResponse extends StudyRequest {

    private String status;

    public StudyResponse(Integer person, Integer profession, LocalDate graduationDate, String universityName, String database, String status) {
        super(person, profession, graduationDate, universityName, database);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

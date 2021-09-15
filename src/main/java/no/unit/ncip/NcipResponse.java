package no.unit.ncip;

import com.fasterxml.jackson.annotation.JsonProperty;
import nva.commons.core.JsonSerializable;

public class NcipResponse implements JsonSerializable {

    @JsonProperty("message")
    public transient String message;

    @JsonProperty("status")
    public transient int status;

    @JsonProperty("problemdetail")
    public transient String problemdetail;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProblemdetail() {
        return problemdetail;
    }

    public void setProblemdetail(String problemdetail) {
        this.problemdetail = problemdetail;
    }

    @Override
    public String toString() {
        return "NcipResponse{"
            + "message='"
            + message + '\''
            + ", status="
            + status
            + ", problemdetail='"
            + problemdetail
            + '\''
            + '}';
    }
}

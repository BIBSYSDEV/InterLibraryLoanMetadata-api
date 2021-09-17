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

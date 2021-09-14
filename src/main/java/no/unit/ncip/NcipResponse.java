package no.unit.ncip;

public class NcipResponse {

    public transient String message;
    public transient int status;
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

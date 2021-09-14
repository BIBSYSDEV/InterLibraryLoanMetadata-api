package no.unit.libcheck;

import com.fasterxml.jackson.annotation.JsonProperty;
import nva.commons.core.JsonSerializable;

public class LibcheckResponse implements JsonSerializable {

    @JsonProperty("isAlmaLibrary")
    private boolean isAlmaLibrary;

    @JsonProperty("isNcipLibrary")
    private boolean isNcipLibrary;

    public boolean isAlmaLibrary() {
        return isAlmaLibrary;
    }

    public void setAlmaLibrary(boolean almaLibrary) {
        isAlmaLibrary = almaLibrary;
    }

    public boolean isNcipLibrary() {
        return isNcipLibrary;
    }

    public void setNcipLibrary(boolean ncipLibrary) {
        isNcipLibrary = ncipLibrary;
    }

}

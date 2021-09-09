package no.unit.ncip;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import nva.commons.core.JacocoGenerated;

public class NcipRequest {

    private final NcipMessage message;

    @JsonCreator
    public NcipRequest(@JsonProperty("message") NcipMessage message) {
        this.message = message;
    }

    protected NcipRequest(Builder builder) {
        this.message = builder.message;
    }

    public NcipMessage getMessage() {
        return message;
    }

    @JacocoGenerated
    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof NcipRequest)) {
            return false;
        }
        NcipRequest that = (NcipRequest) o;
        return Objects.equals(message, that.message);
    }

    @JacocoGenerated
    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    @JacocoGenerated
    public static final class Builder {

        private transient NcipMessage message;

        public NcipRequest.Builder withContents(NcipMessage message) {
            this.message = message;
            return this;
        }

        public NcipRequest build() {
            return new NcipRequest(this);
        }

    }

}

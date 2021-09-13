package no.unit.ncip;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import nva.commons.core.JacocoGenerated;

public class NcipRequest {

    private final transient NcipTransferMessage ncipTransferMessage;

    @JsonCreator
    public NcipRequest(@JsonProperty("transferMessage") NcipTransferMessage transferMessage) {
        this.ncipTransferMessage = transferMessage;
    }

    protected NcipRequest(Builder builder) {
        this.ncipTransferMessage = builder.transferMessage;
    }

    public NcipTransferMessage getTransferMessage() {
        return ncipTransferMessage;
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
        return Objects.equals(ncipTransferMessage, that.ncipTransferMessage);
    }

    @JacocoGenerated
    @Override
    public int hashCode() {
        return Objects.hash(ncipTransferMessage);
    }

    @JacocoGenerated
    public static final class Builder {

        private transient NcipTransferMessage transferMessage;

        public NcipRequest.Builder withContents(NcipTransferMessage ncipTransferMessage) {
            this.transferMessage = ncipTransferMessage;
            return this;
        }

        public NcipRequest build() {
            return new NcipRequest(this);
        }

    }

}

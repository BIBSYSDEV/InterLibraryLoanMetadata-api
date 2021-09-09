package no.unit;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Map;
import nva.commons.core.Environment;

public class MetadataHandler implements RequestHandler<Map<String, Object>, GatewayResponse> {


    private final transient Environment environment;

    public MetadataHandler() {
        this.environment = new Environment();
    }

    public MetadataHandler(Environment environment) {
        this.environment = environment;
    }

    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        return new GatewayResponse(environment, "Test", 200);
    }
}

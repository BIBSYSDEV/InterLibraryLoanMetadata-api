package no.unit.xservice;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


import java.util.Map;
import no.unit.GatewayResponse;
import nva.commons.core.Environment;

public class GetXServiceHandler implements RequestHandler<Map<String, Object>, GatewayResponse> {

    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        return new GatewayResponse(new Environment(), "PCB", 200);
    }
}

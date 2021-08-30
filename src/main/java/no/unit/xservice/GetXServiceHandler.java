package no.unit.xservice;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


import java.util.Map;

public class GetXServiceHandler implements RequestHandler<Map<String, Object>, GatewayResponse> {

    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        return new GatewayResponse(200, "");
    }
}

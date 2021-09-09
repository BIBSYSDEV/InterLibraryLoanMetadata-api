package no.unit.ncip;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Map;
import no.unit.GatewayResponse;
import no.unit.ill.services.InstitutionService;

public class NcipHandler implements RequestHandler<Map<String, Object>, GatewayResponse> {

    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        InstitutionService institutionService = new InstitutionService();
        final String libraryCode = institutionService.get("oriaCode", "NTNU_UB", "oriaDefaultNCIPserver");
        return new GatewayResponse("libaryCode=" + libraryCode, 200);
    }
}

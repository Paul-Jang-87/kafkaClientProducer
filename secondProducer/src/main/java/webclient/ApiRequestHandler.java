package webclient;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Arrays;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class ApiRequestHandler {

	private static final String BASE_URL = "https://api.apne2.pure.cloud";

	public UriComponents buildApiRequest(String path, Object... pathVariables) {

		UriComponents uriComponents = null;

		// Append path variables if any
		if (pathVariables.length > 0) {
			uriComponents = UriComponentsBuilder.fromUriString(BASE_URL).path(path).buildAndExpand(pathVariables);

		} else {
			uriComponents = UriComponentsBuilder.fromUriString(BASE_URL).path(path).buildAndExpand();

		}

		return uriComponents;
	}

	public UriComponents buildApiRequestaaa(String path, Object... pathVariables) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(BASE_URL).path(path);

        
        
        Object[] pathVars;
        Object[] queryParams;
        if (pathVariables.length > 0) {
            pathVars = Arrays.copyOfRange(pathVariables, 0, 1);
            queryParams = Arrays.copyOfRange(pathVariables, 1, pathVariables.length);

            // Expand path variables if any
            UriComponents uriComponents = uriComponentsBuilder.buildAndExpand(pathVars);
            
            // Create a new UriComponentsBuilder based on the result of buildAndExpand
            uriComponentsBuilder = UriComponentsBuilder.fromUri(uriComponents.toUri());

            // Append query parameters if any
            if (queryParams.length > 0) {
                for (int i = 0; i < queryParams.length; i += 2) {
                    // Accumulate query parameters
                    uriComponentsBuilder.queryParam(String.valueOf(queryParams[i]), queryParams[i + 1]);
                }
            }
        }

        // Build the final UriComponents
        return uriComponentsBuilder.build();
    }

}

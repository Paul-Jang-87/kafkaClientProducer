package webclient;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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
	
	public UriComponentsBuilder buildApiRequestWithQueryParamsAndValues(String path, Object... queryParamsAndValues) {
	    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(BASE_URL).path(path);

	    // Append query parameters if any
	    if (queryParamsAndValues.length % 2 == 0) {
	        for (int i = 0; i < queryParamsAndValues.length; i += 2) {
	            uriComponentsBuilder.queryParam(String.valueOf(queryParamsAndValues[i]), queryParamsAndValues[i + 1]);
	        }
	    }

	    return uriComponentsBuilder;
	}


}

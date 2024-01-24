package webclient;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Arrays;

public class ApiRequestHandler {//모든 api를 핸들링하는 클래스

	private static final String BASE_URL = WebClientConfig.getBaseUrl();

	public UriComponents buildApiRequest(String path, Object... pathVariables) {//uri를 api에 맞게 커스터 마이징.
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(BASE_URL).path(path);

        //path parameter가 몇 개 필요한지 파악
        int cnt = 0;
        
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '{') {
                cnt++;
            }
        }
        
        
        Object[] pathVars;  //path parameter를 복사하여 저장하는 변수
        Object[] queryParams; //query parameter를 복사하여 저장하는 변수 
        if (pathVariables.length > 0) {
        	
        	//buildApiRequest 함수를 사용할 때, 2번째 인자부터는 path parameter나 query parameter를 쓰면 된다. 
        	//하지만 순서는 path parameter부터 쓴다. 
            pathVars = Arrays.copyOfRange(pathVariables, 0, cnt);//index 0번째부터 index cnt까지 복사.
            queryParams = Arrays.copyOfRange(pathVariables, cnt, pathVariables.length); //index cnt번째부터 index 끝까지 복사.

            UriComponents uriComponents = uriComponentsBuilder.buildAndExpand(pathVars);//먼저path paramter를 expand해준다. 
            
            uriComponentsBuilder = UriComponentsBuilder.fromUri(uriComponents.toUri());

            if (queryParams.length > 0) {
                for (int i = 0; i < queryParams.length; i += 2) {
                    // Accumulate query parameters
                    uriComponentsBuilder.queryParam(String.valueOf(queryParams[i]), queryParams[i + 1]);//그 다음 query parameter. 
                    																					//query parameter는 속성,값 한 쌍으로 이루어 졌기 때문에 
                    																					//queryParams[i]는 속성, queryParams[i + 1]는 값이다.
                }
            }
            
        }else { //path parameter나 query parameter가 아예 없이 endpoint만 있는 api
            UriComponents uriComponents = uriComponentsBuilder.buildAndExpand(); 
            
            uriComponentsBuilder = UriComponentsBuilder.fromUri(uriComponents.toUri());
        }

        return uriComponentsBuilder.build();
    }

}

package webclient;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import com.mypurecloud.sdk.v2.ApiClient;
import com.mypurecloud.sdk.v2.ApiResponse;
import com.mypurecloud.sdk.v2.PureCloudRegionHosts;
import com.mypurecloud.sdk.v2.extensions.AuthResponse;


public class WebClientApp {

	private static String CLIENT_ID = "";
	private static String CLIENT_SECRET = "";
	private static String API_BASE_URL = "";
	private static String API_END_POINT = "";
	private static String HTTP_METHOD = "";
	private static String API_END_POINTINFO = "";

	private WebClient webClient;

	public WebClientApp(String apiName) {// WebClinet 생성자, 기본적인 초기 설정들.
										 // WebClient를 사용하기 위한 기본 설정들과 매개변수로 온 api를 사용하기 위한 기본 설정들.

		CLIENT_ID = WebClientConfig.getClientId();
		CLIENT_SECRET = WebClientConfig.getClientSecret();
		API_BASE_URL = WebClientConfig.getBaseUrl();
		API_END_POINTINFO = WebClientConfig.getApiEndpointInfo(apiName);
		String[] parts = API_END_POINTINFO.split(":"); //api의 endpoint와 어떤 http method를 사용하는지에 대한 정보를 가지고 온다. 
		HTTP_METHOD = parts[0];
		API_END_POINT = parts[1];

		this.webClient = WebClient.builder().baseUrl(API_BASE_URL)
				.defaultHeader("Authorization", "Bearer " + getAccessToken()).build();
	}

	private String getAccessToken() {
		// Replace the region with your desired one
		String region = "ap_northeast_2";
		String accessToken = "";

		ApiClient apiClient = ApiClient.Builder.standard().withBasePath(PureCloudRegionHosts.valueOf(region)).build();

		try {
			ApiResponse<AuthResponse> authResponse = apiClient.authorizeClientCredentials(CLIENT_ID, CLIENT_SECRET);
			accessToken = authResponse.getBody().getAccess_token();
		} catch (Exception e) {
			// Handle the exception more gracefully, e.g., log it
			e.printStackTrace();
		}

		System.out.println("Access Token: " + accessToken);
		return accessToken;
	}

	public Mono<String> makeApiRequestAsync() {
		
		RequestHeadersUriSpec<?> requestSpec = null;
		
		if(HTTP_METHOD.equals("GET")) {//http method설정. 
			requestSpec = webClient.get();
		}else {
			requestSpec = webClient.post();
		}
		
		ApiRequestHandler apiRequestHandler = new ApiRequestHandler();
		UriComponents api1 = apiRequestHandler.buildApiRequest(API_END_POINT,"87dde849-5710-4470-8a00-5e94c679e703");//첫번째 인자는 api endpoint이기 때문데 무조건 필요.
																													 //두번째 인자부터는 path parameter or query parameter. 
																													 //두번째 인자는 있어도 되고 없어도 됨. 

	    return requestSpec
				.uri(api1.toUriString())
				.retrieve()
				.bodyToMono(String.class)
				.onErrorResume(error -> {
			System.err.println("Error making API request: " + error.getMessage());
			return Mono.empty();
		});
	}
	

}

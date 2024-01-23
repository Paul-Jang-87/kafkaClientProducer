package webclient;

import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
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

	private WebClient webClient;

	public WebClientApp(String apiName) {

		CLIENT_ID = WebClientConfig.getClientId();
		CLIENT_SECRET = WebClientConfig.getClientSecret();
		API_BASE_URL = WebClientConfig.getBaseUrl();
		API_END_POINT = WebClientConfig.getApiEndpoint(apiName);

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

	public Mono<String> getApiRequestAsync() {
		
		UriComponents uriComponents = UriComponentsBuilder
				.fromUriString(API_BASE_URL)
				.path(API_END_POINT)
				.buildAndExpand("97e6b32d-c266-4d33-92b4-01ddf33898cd");
		
		RequestHeadersUriSpec<?> requestSpec = webClient.get();

		return requestSpec
				.uri(uriComponents.toUri())
				.retrieve()
				.bodyToMono(String.class)
				.onErrorResume(error -> {
			System.err.println("Error making API request: " + error.getMessage());
			return Mono.empty();
		});
	}

	
	
	public Mono<String> makeApiRequestAsync() {
		
		ApiRequestHandler apiRequestHandler = new ApiRequestHandler();
		UriComponents api1 = apiRequestHandler.buildApiRequestaaa(API_END_POINT,"87dde849-5710-4470-8a00-5e94c679e703","pageSize","5","pageNumber","3");

	    return webClient.get()
				.uri(api1.toUriString())
				.retrieve()
				.bodyToMono(String.class)
				.onErrorResume(error -> {
			System.err.println("Error making API request: " + error.getMessage());
			return Mono.empty();
		});
	}


	

	public Mono<String> postApiRequestAsync() {

		UriComponents uriComponents = UriComponentsBuilder.fromUriString(API_BASE_URL).path(API_END_POINT)
				.buildAndExpand();

		return webClient.post()
				.uri(uriComponents.toUri())
				.retrieve()
				.bodyToMono(String.class)
				.onErrorResume(error -> {
			System.err.println("Error making API request: " + error.getMessage());
			return Mono.empty();
		});
	}
	
}

package webclient;

import org.springframework.web.reactive.function.client.WebClient;

import com.mypurecloud.sdk.v2.ApiClient;
import com.mypurecloud.sdk.v2.ApiResponse;
import com.mypurecloud.sdk.v2.PureCloudRegionHosts;
import com.mypurecloud.sdk.v2.extensions.AuthResponse;

import reactor.core.publisher.Mono;

public class WebClientApp {

	private static String CLIENT_ID = ""; 
	private static String CLIENT_SECRET ="";
	private static String API_BASE_URL ="";
	
    private WebClient webClient;

    public WebClientApp() {
    	
    	CLIENT_ID = WebClientConfig.getClientId(); 
    	CLIENT_SECRET = WebClientConfig.getClientSecret();
    	API_BASE_URL = WebClientConfig.getBaseUrl();
    	
        this.webClient = WebClient.builder()
                .baseUrl(API_BASE_URL)
                .defaultHeader("Authorization", "Bearer " + getAccessToken())
                .build();
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
        String path = WebClientConfig.getCampaignEndpoint(); // Replace with your API endpoint

        return webClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(error -> {
                    System.err.println("Error making API request: " + error.getMessage());
                    return Mono.empty();
                });
    }

}

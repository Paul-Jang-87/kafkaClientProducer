package genesysapi;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.mypurecloud.sdk.v2.ApiClient;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.ApiResponse;
import com.mypurecloud.sdk.v2.Configuration;
import com.mypurecloud.sdk.v2.PureCloudRegionHosts;
import com.mypurecloud.sdk.v2.extensions.AuthResponse;

import httpaction.HttpAction;

public class ApiConfigure {
	
	ApiClient apiClient = null;
	ApiResponse<AuthResponse> authResponse = null;
	String accessToken = "";
	
	public ApiConfigure() throws IOException, ApiException{
		credentialsAuth();
		this.accessToken = authResponse.getBody().getAccess_token();
	}
	
	public void credentialsAuth() throws IOException, ApiException{
		String clientId = "8ed02ed8-2e38-41ee-b70d-ab09e43b3ff1";
		String clientSecret = "0xgqeo_xNbAUAy1JvXyGCrF5jr8yPOAg_TbDDbOOrB4";

		//Set Region
		PureCloudRegionHosts region = PureCloudRegionHosts.ap_northeast_2;
		
		apiClient = ApiClient.Builder.standard().withBasePath(region).build();
		authResponse = apiClient.authorizeClientCredentials(clientId, clientSecret);

		// Don't actually do this, this logs your auth token to the console!
		System.out.println(authResponse.getBody().toString());
		
		// Use the ApiClient instance
		Configuration.setDefaultApiClient(apiClient);
	}
	
	public JSONObject callApiRestTemplate_GET(String url) {
		UriComponents uriBuilder = UriComponentsBuilder.fromUriString(url)
									.build(true);

		System.out.println("## uriBuilder :: " + uriBuilder.toString());
		
		HttpAction httpAction = HttpAction.getInstance(); 
		String res = httpAction.restTemplateService(uriBuilder, accessToken);
		System.out.println(res);
		
		return new JSONObject(res);
	}

}

package com.example.secondProducer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import com.mypurecloud.sdk.v2.ApiClient;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.ApiResponse;
import com.mypurecloud.sdk.v2.Configuration;
import com.mypurecloud.sdk.v2.PureCloudRegionHosts;
import com.mypurecloud.sdk.v2.api.UsersApi;
import com.mypurecloud.sdk.v2.extensions.AuthResponse;
import com.mypurecloud.sdk.v2.model.UserEntityListing;

@SpringBootApplication
@EnableAsync
public class Mainrunningapp {

	public static void main(String[] args) {

//		SpringApplication.run(Mainrunningapp.class, args);

		String clientId = "058cae41-8fc1-463b-8b3e-d3d2cc5f410f";
		String clientSecret = "7LwBCYq__fAaoawUiXSh-uoM-kSWvComHt_xtbd0dOs";

		//Set Region
		PureCloudRegionHosts region = PureCloudRegionHosts.ap_northeast_2;

		ApiClient apiClient = ApiClient.Builder.standard().withBasePath(region).build();
		ApiResponse<AuthResponse> authResponse;
		try {
			authResponse = apiClient.authorizeClientCredentials(clientId, clientSecret);
			System.out.println(authResponse.getBody().toString());
			System.out.println(authResponse.getBody().getAccess_token().toString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Don't actually do this, this logs your auth token to the console!

		// Use the ApiClient instance
		Configuration.setDefaultApiClient(apiClient);

		// Create API instances and make authenticated API requests
		UsersApi apiInstance = new UsersApi();
		//UserEntityListing response = apiInstance.getUsers(null, null, null, null, null, null, null);
	}
}


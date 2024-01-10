package com.example.secondProducer.genesys.users;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.mypurecloud.sdk.v2.ApiClient;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.Configuration;
import com.mypurecloud.sdk.v2.api.UsersApi;
import com.mypurecloud.sdk.v2.model.UserEntityListing;

public class UserApiClient {

    private final ApiClient apiClient;

    public UserApiClient(String accessToken) {
        // Create ApiClient instance
        this.apiClient = ApiClient.Builder.standard()
                .withAccessToken(accessToken)
                .withBasePath("https://api.mypurecloud.com")
                .build();

        // Use the ApiClient instance
        Configuration.setDefaultApiClient(apiClient);
    }

    public UserEntityListing getUsers(Integer pageSize, Integer pageNumber, List<String> id, List<String> jabberId,
            String sortOrder, List<String> expand, String integrationPresenceSource, String state) throws IOException, ApiException {
        UsersApi apiInstance = new UsersApi();
        return apiInstance.getUsers(pageSize, pageNumber, id, jabberId, sortOrder, expand, integrationPresenceSource, state);
    }
}

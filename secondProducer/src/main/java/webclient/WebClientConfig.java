package webclient;

public class WebClientConfig {

	private static final String API_BASE_URL = "https://api.apne2.pure.cloud";
	private static final String CLIENT_ID = "8ed02ed8-2e38-41ee-b70d-ab09e43b3ff1";
	private static final String CLIENT_SECRET = "0xgqeo_xNbAUAy1JvXyGCrF5jr8yPOAg_TbDDbOOrB4";

	public static String getBaseUrl() {
		return API_BASE_URL;
	}

	public static String getApiEndpoint(String apiName) {

		String API_END_POINT = "";

		switch (apiName) { 
		case "campaigns":
			API_END_POINT = "/api/v2/outbound/campaigns";
			break;
		case "campaigns_interactions":
			API_END_POINT = "/api/v2/outbound/campaigns/{campaignId}/interactions";
			break;
		case "campaigns_agents":
			API_END_POINT = "/api/v2/outbound/campaigns/{campaignId}/agents/{userId}";
			break;
		default:
			API_END_POINT = "Invalid api";
			break;

		}
		return API_END_POINT;
	}

	public static String getClientId() {
		return CLIENT_ID;
	}

	public static String getClientSecret() {
		return CLIENT_SECRET;
	}

}

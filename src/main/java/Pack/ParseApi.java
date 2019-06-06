package Pack;

import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;


import static io.restassured.RestAssured.given;

public class ParseApi {

	static Response res;
	static JSONObject jsonobj;
	static JSONObject jsonobjMasterPass;
	static String masterpass;
	static String responseBody;
	static JSONArray jsonArray;
	static String testName;

	public static String getAccesstoken(String urlToken) throws Exception {
		String token = "";
		res = given().when().get(urlToken);
		responseBody = res.getBody().asString();
		token = responseBody.split("<access_token>")[1].split("</access_token>")[0];
		return token;
	}



}

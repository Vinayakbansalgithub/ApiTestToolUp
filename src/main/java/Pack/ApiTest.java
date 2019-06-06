package Pack;

import org.json.JSONArray;
import org.json.JSONObject;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Properties;
// api imports

public class ApiTest {
	// api

	static Response res;
	static JSONObject jsonobj;
	static JSONObject jsonobjMasterPass;
	static String masterpass;
	static String responseBody;
	static JSONArray jsonArray;
	static String testName;

	static int totalSteps = 0;
	static String BaseSiteurl = null;
	static String tcid = null;
	static String path = null;
	// parameter hashmap
	public static LinkedHashMap<Integer, String> StepSelector = new LinkedHashMap<Integer, String>();
	public static LinkedHashMap<Integer, String> StepAction = new LinkedHashMap<Integer, String>();
	public static LinkedHashMap<Integer, Boolean> StepIgnore = new LinkedHashMap<Integer, Boolean>();
	public static LinkedHashMap<Integer, String> StepActionId = new LinkedHashMap<Integer, String>();

	public static void main(String[] args) throws Exception {




	path = System.getProperty("user.dir");



		FileReader reader = new FileReader(path + "/TestCase.properties");

		Properties p = new Properties();
		p.load(reader);

		System.out.println(p.getProperty("test_id"));

		String token=ParseApi.getAccesstoken("https://app.functionize.com/api/oapi?method=generateToken&apikey=0bbb37bac7d8a20a2597abf72cd8bdde&secret=635026cd73f298bbc8238f1e018c161c");
		System.out.println(token);

		String Keyurl = "https://release.functionize.com/api/oapi/getTestDetails?accesstoken="
				+ token + "&testid=" + p.getProperty("test_id")
				+ "&response_type=json";

		res = given().when().get(
				"https://app.functionize.com/api/oapi?method=generateToken&apikey=0bbb37bac7d8a20a2597abf72cd8bdde&secret=635026cd73f298bbc8238f1e018c161c&response_type=json");

		responseBody = res.getBody().asString();

		// System.out.println(responseBody);

		if (responseBody.startsWith("[")) {
			jsonArray = new JSONArray(responseBody);
			// System.out.println("Object length:" + jsonArray.length());
			JSONObject obj = null;
			if (jsonArray.length() == 1)
				obj = jsonArray.getJSONObject(0);

			String access_token = obj.getString("access_token");
			System.out.println("Access token:" + access_token);

			Keyurl = Keyurl.replace("givenToken", access_token);
			res = given().when().get(Keyurl);
			System.out.println();

			responseBody = res.getBody().asString();

			System.out.println(responseBody);

			jsonobj = new JSONObject(responseBody);

			jsonArray = jsonobj.getJSONArray("RESULTSET");
			// System.out.println("Object length RESULTSET:" + jsonArray.length());
			jsonobj = jsonArray.getJSONObject(jsonArray.length() - 1);
			testName = jsonobj.getString("name");

			String constants = jsonobj.getString("constants");

			System.out.println("constants:" + constants);
			jsonobj = new JSONObject(constants);

			BaseSiteurl = jsonobj.getString("url");

			System.out.println("url:" + BaseSiteurl);

			jsonobj = new JSONObject(responseBody);

			jsonArray = jsonobj.getJSONArray("RESULTSET");
			// System.out.println("back to RESULTSET:");

			jsonobj = jsonArray.getJSONObject(jsonArray.length() - 1);

			jsonArray = jsonobj.getJSONArray("steps");

			try {

				jsonobjMasterPass = jsonobj;
				masterpass = jsonobjMasterPass.getString("masterpass");
				// System.out.println("Masterpass found-----------");

				jsonobjMasterPass = new JSONObject(masterpass);

				masterpass = jsonobjMasterPass.getString("locator");

				jsonobjMasterPass = new JSONObject(masterpass);

				masterpass = jsonobjMasterPass.getString("xpath");

				System.out.println("Masterpass xpath-----------" + masterpass);

			} catch (Exception e) {
				System.out.println("Master pass exception occur");

			}

			System.out.println("Total  steps:" + jsonArray.length());
			totalSteps = jsonArray.length();
			for (int i = 0; i < jsonArray.length(); i++) {

				jsonobj = jsonArray.getJSONObject(i);
				String action = jsonobj.getString("action");
				String actionid = jsonobj.getString("actionId");

				System.out.println("Step" + (i + 1) + "  having action " + action + " and action id  " + actionid);

				if (action.equals("getPage") || action.equals("sleep") ) {

					String value = jsonobj.getString("value");

					StepAction.put(i, action);
					StepSelector.put(i, value);
					StepIgnore.put(i, false);
					StepActionId.put(i, actionid);

				} else if (action.equals("findAndSelect")) {
					String value = null;

					if(jsonobj.has("value")){
						value = jsonobj.getString("value");
					}else if (jsonobj.has("name")){

						value = jsonobj.getString("name");


					}
					String locator = jsonobj.getString("locator");

					jsonobj = new JSONObject(locator);

					String xpath = jsonobj.getString("xpath");

					StepAction.put(i, action);
					StepSelector.put(i, xpath + "---" + value);
					StepIgnore.put(i, false);
					StepActionId.put(i, actionid);

				}

				else if (action.equals("findAndFill")) {

					String value = jsonobj.getString("value");
					String locator = jsonobj.getString("locator");

					jsonobj = new JSONObject(locator);

					String xpath = jsonobj.getString("xpath");

					StepAction.put(i, action);
					StepSelector.put(i, xpath + "---" + value);
					StepIgnore.put(i, false);
					StepActionId.put(i, actionid);

				}
				else if (action.equals("findAndSwitchToFrame")) {

					StepAction.put(i, action);
					StepSelector.put(i, "---");
					StepIgnore.put(i, false);
					StepActionId.put(i, actionid);

				}

				else {
					String locator = jsonobj.getString("locator");

					try {
						String ignoreCheck = jsonobj.getString("ignoreError");
						if (ignoreCheck.equals("true"))
							StepIgnore.put(i, true);
						else
							StepIgnore.put(i, false);

						// System.out.println("ignoreError value set at " + (i+1));

					} catch (Exception e) {
						// System.out.println("ignoreError not found");
						StepIgnore.put(i, false);

					}

					String selector;
					jsonobj = new JSONObject(locator);

					try {
						selector = jsonobj.getString("xpath");
					} catch (Exception e) {
						selector = jsonobj.getString("css");
					}

					if (selector.contains("svg")) {

						selector = selector.replaceAll("\"", "'");

					}

					StepAction.put(i, action);
					StepSelector.put(i, selector);
					StepActionId.put(i, actionid);

					System.out.println("Selector for click " + selector);
				}
				System.out.println();

			}

		}
		System.out.println("Total  steps:" + totalSteps);

		for (int i = 0; i < totalSteps; i++) {

			String currentAction = StepAction.get(i);
			String currentData = StepSelector.get(i);
			boolean currentDataIgnore = StepIgnore.get(i);
			String currentActionId = StepActionId.get(i);

			if (currentAction.equals("getPage")) {

				if (i == 0) {

					new TestActions(testName);

					ExtentHTML.starttest(testName);
					TestActions.getPage(BaseSiteurl, currentData, currentActionId, (i + 1));

				} else {
					System.out.println("wait for 5 seconds " + i);
					Thread.sleep(5000);
					System.out.println("current url:" + TestActions.driver.getCurrentUrl());

					String addOn = currentData.replace("{url}", "");

					System.out.println("url to open:" + BaseSiteurl + addOn);
					String url = BaseSiteurl + addOn;


					/*
					 * if(!url.equals(TestActions.driver.getCurrentUrl()) &&
					 * !StepAction.get(i-1).equals("findAndClick")){ //
					 * TestActions.getPage(BaseSiteurl, currentData, currentActionId, (i + 1)); }
					 */

					TestActions.redirection(BaseSiteurl, currentData, currentActionId, (i + 1));

				}
			} else if (currentAction.equals("findAndClick")) {

				TestActions.findAndClick(currentData, currentDataIgnore, currentActionId, (i + 1));

			} else if (currentAction.equals("sleep")) {
				currentData = currentData + "000";
				System.out.println("called sleep" + currentData);

				TestActions.sleep(currentData, currentActionId, (i + 1));

				System.out.println("called sleep end");

			} else if (currentAction.equals("findAndSelect")) {
				TestActions.findAndSelect(currentData, currentActionId, (i + 1));

			} else if (currentAction.equals("findAndFill")) {
				TestActions.findAndFill(currentData, currentActionId, (i + 1));

			} else if (currentAction.equals("findAndSwitchToFrame")) {
				// TestActions.findAndFill(currentData);

				System.out.println("frame--------------");

			}

			System.out.println("Step: " + i + " performed");

		}

		try {

			TestActions.masterpass(masterpass, "masterpass", 000);

		} catch (Exception e) {

			System.out.println("No element for masterpass  found on page" + e);

		}

		System.out.println("-----------Test Finish--------");

		TestActions.closeBrowser();

	}

}

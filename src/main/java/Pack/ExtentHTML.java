package Pack;

import java.io.File;
import java.io.IOException;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ExtentHTML {

	public static ExtentTest testrep;

	static String TestReportDir = null;

	static ExtentReports extent;

	static String testName;

	ExtentHTML(String testName) throws Exception {

		ExtentHTML.testName = testName;
		String destDir = ApiTest.path + "/src/main/resources/ApiScreeshots/" + testName;

		File file = new File(destDir);
		file.mkdirs();

		final File[] files = file.listFiles();
		for (File f : files)
			f.delete();

		TestReportDir = System.getProperty("user.dir") + "/src/main/resources/ApiScreeshots/" + testName + "/"
				+ "TestReport.html";

		new File(TestReportDir).createNewFile();

		extent = new ExtentReports(TestReportDir, true);

	}

	public static void starttest(String testName) {
		testrep = extent.startTest(testName);

	}

	public static void pass(int stepNo, String actionId, String msg, String priScreenshotPath,
			String postScreenshotPath) {

		testrep.log(LogStatus.PASS, "Step " + stepNo + " " + actionId, msg);

		testrep.log(LogStatus.PASS, "pre and post screenshot for step: " + stepNo,
				testrep.addScreenCapture(priScreenshotPath) + " " + testrep.addScreenCapture(postScreenshotPath));

	}

	public static void fail(int stepNo, String actionId, String msg, String priScreenshotPath,
			String postScreenshotPath) {
		testrep.log(LogStatus.FAIL, "Step " + stepNo + " " + actionId, msg);

		testrep.log(LogStatus.FAIL, "pre and post screenshot for step: " + stepNo,
				testrep.addScreenCapture(priScreenshotPath) + " " + testrep.addScreenCapture(postScreenshotPath));

	}

	public static void Skip(int stepNo, String actionId, String msg, String priScreenshotPath,
			String postScreenshotPath) {

		testrep.log(LogStatus.SKIP, "Step " + stepNo + " " + actionId, msg);

		testrep.log(LogStatus.SKIP, "pre and post screenshot for step: " + stepNo,
				testrep.addScreenCapture(priScreenshotPath) + " " + testrep.addScreenCapture(postScreenshotPath));

	}

	public static void Endtest() {
		extent.endTest(testrep);
		extent.flush();
	}

}

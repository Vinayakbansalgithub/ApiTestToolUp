package Pack;

import org.opencv.core.Mat;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class TestActions {

	static ChromeDriver driver = null;
	static WebElement element = null;
	static ExtentHTML extenthtml;
	static String ElemntScreenshotPath = null;
	static String priScreenshotPath = null;
	static String postScreenshotPath = null;

	TestActions(String testName) throws Exception {
		System.setProperty("webdriver.chrome.driver", ApiTest.path +"/Drivers/chromedriver");

		//	Proxy p=new Proxy();


		// Set HTTP Port to 7777
		//	p.setHttpProxy("104.155.166.119:3128");

		// Create desired Capability object
		DesiredCapabilities cap=new DesiredCapabilities();

		// Pass proxy object p
//		cap.setCapability(CapabilityType.PROXY, p);


		driver = new ChromeDriver(cap);
		driver.manage().deleteAllCookies();

		extenthtml = new ExtentHTML(testName);
	}

	public static void getPage(String baseurl, String addOn, String actionId, int stepNo) throws Exception {

		addOn = addOn.replace("{url}", "");
		System.out.println("url to open:" + baseurl + addOn);

		priScreenshotPath = SupportLib.takeScreenShot(driver, actionId, stepNo + "_pre");

		driver.get(baseurl + addOn);
		driver.manage().window().maximize();
		SupportLib.readyStateURL(driver);
		Thread.sleep(3000);

		postScreenshotPath = SupportLib.takeScreenShot(driver, actionId, stepNo + "_post");

		ExtentHTML.pass(stepNo, actionId, "Url  " + baseurl + addOn + " has opened sucessfully", "",
				"");

	}

	public static void redirection(String baseurl, String addOn, String actionId, int stepNo) throws Exception {
		Thread.sleep(2000);
		priScreenshotPath = SupportLib.takeScreenShot(driver, actionId, stepNo + "_pre");
		postScreenshotPath = SupportLib.takeScreenShot(driver, actionId, stepNo + "_post");

		ExtentHTML.pass(stepNo, actionId, "redirecting to  " + baseurl + addOn + "  performed sucessfully", "", "");

	}

	public static void findAndClick(String selector, boolean ignore, String actionId, int stepNo)
			throws InterruptedException {
		Thread.sleep(5000);

		System.out.println("Performing Action clcik");

		SupportLib.readyStateURL(driver);

		try {
			if (selector.contains("//"))
				element = driver.findElement(By.xpath(selector));
			else
				element = driver.findElement(By.cssSelector(selector));

			SupportLib.elementToBeClickable(driver, element);

			System.out.println("element:" + element + "  is displayed:" + element.isDisplayed());

			SupportLib.moveToElement(driver, element);
			priScreenshotPath = SupportLib.takeElementScreenShot(driver,element, actionId, stepNo + "_pre");

			ElemntScreenshotPath = SupportLib.takeElementScreenShot(driver,element, actionId, stepNo + "_pre");



			if (element.getTagName().contains("span") || element.getTagName().contains("svg")) {

				Actions builder = new Actions(driver);
				builder.click(element).build().perform();

				System.out.println("span occur------------");
			}

			else {
				System.out.println("other occur------------");

				JavascriptExecutor executor = driver;

				executor.executeScript("arguments[0].click();", element);

			}

			Thread.sleep(5000);
			postScreenshotPath = SupportLib.takeScreenShot(driver, actionId, stepNo + "_post");

			
		
//		
//		Mat img2=	SupportLib.bufferedImageToMat(SupportLib.loadImage(postScreenshotPath));
//
//		
//		Mat img1=	SupportLib.bufferedImageToMat(SupportLib.loadImage(priScreenshotPath));
//
//			long diff =SupportLib.findDiffPercentbeforConverting(img1,img2);
//			
//			System.out.println("diffrence is "+diff);
			
			ExtentHTML.pass(stepNo, actionId, "using selector  " + selector + " click performed sucessfully",
					priScreenshotPath, postScreenshotPath);

		} catch (Exception e) {

			if (!ignore) {
				System.out.println("no step found" + e);
				priScreenshotPath = SupportLib.takeScreenShot(driver, actionId, stepNo + "_pre");
				postScreenshotPath = SupportLib.takeScreenShot(driver, actionId, stepNo + "_post");
				ExtentHTML.fail(stepNo, actionId, " selector  " + selector + " Not found " + e, priScreenshotPath,
						postScreenshotPath);

			} else {
				System.out.println("Ignore flag set for element " + element);
				priScreenshotPath = SupportLib.takeScreenShot(driver, actionId, stepNo + "_pre");
				postScreenshotPath = SupportLib.takeScreenShot(driver, actionId, stepNo + "_post");
				ExtentHTML.Skip(stepNo, actionId, " selector  " + selector + " Not found and ignore flag is set",
						priScreenshotPath, postScreenshotPath);

			}
		}
	}

	public static void sleep(String miliSec, String actionId, int stepNo) throws InterruptedException {
		System.out.println("Performing Action sleep");

		priScreenshotPath = SupportLib.takeScreenShot(driver, actionId, stepNo + "_pre");
		Thread.sleep(Long.parseLong(miliSec));
		postScreenshotPath = SupportLib.takeScreenShot(driver, actionId, stepNo + "_post");
		ExtentHTML.pass(stepNo, actionId, "waited for  " + miliSec + " ms performed sucessfully", "",
				postScreenshotPath);

	}

	public static void findAndSelect(String Selector, String actionId, int stepNo) throws Exception {
		SupportLib.readyStateURL(driver);

		System.out.println(" Pending Performing Action Select");
		priScreenshotPath = SupportLib.takeScreenShot(driver, actionId, stepNo + "_pre");

		String[] arr = Selector.split("---");

		element = driver.findElement(By.xpath(arr[0]));

		SupportLib.moveToElement(driver, element);

		Select el = new Select(element);

		el.selectByValue(arr[1]);

		postScreenshotPath = SupportLib.takeScreenShot(driver, actionId, stepNo + "_post");
	}

	public static void findAndFill(String SelectorAndData, String actionId, int stepNo) throws Exception {
		SupportLib.readyStateURL(driver);

		System.out.println("Performing Action Input");

		String[] arr = SelectorAndData.split("---");

		try {
			element = driver.findElement(By.xpath(arr[0]));

			element.clear();

			priScreenshotPath = SupportLib.takeScreenShot(driver, actionId, stepNo + "_pre");
			element.sendKeys(arr[1]);
			postScreenshotPath = SupportLib.takeScreenShot(driver, actionId, stepNo + "_post");

			// SupportLib.blurEvent(driver, element);
		} catch (Exception e) {

			ExtentHTML.fail(stepNo, actionId, " selector  " + arr[0] + " Not found " + e, priScreenshotPath,
					postScreenshotPath);
		}

	}

	public static void masterpass(String Selector, String actionId, int last) throws Exception {
		SupportLib.readyStateURL(driver);

		priScreenshotPath = SupportLib.takeScreenShot(driver, actionId, "masterpass" + "_pre");

		try {

			Thread.sleep(5000);

			WebElement masterpasselement = TestActions.driver.findElement(By.xpath(Selector));
			Actions actions = new Actions(driver);
			actions.moveToElement(masterpasselement).perform();
			Thread.sleep(5000);

			actions.moveToElement(masterpasselement).perform();

			Thread.sleep(2000);

			actions.moveToElement(masterpasselement).perform();

			System.out.println("element for masterpass is found on page" + masterpasselement);
			postScreenshotPath = SupportLib.takeScreenShot(driver, actionId, "masterpass" + "_post");
			ExtentHTML.pass(last, "masterpass", "Master  selector  " + Selector + " found sucessfully",
					priScreenshotPath, postScreenshotPath);

		} catch (Exception e) {
			postScreenshotPath = SupportLib.takeScreenShot(driver, actionId, "masterpass" + "_post");

			System.out.println("No element for masterpass  found on page" + e);
			ExtentHTML.pass(last, "masterpass", "Master  selector " + Selector + " not found " + e, priScreenshotPath,
					postScreenshotPath);

		}

		postScreenshotPath = SupportLib.takeScreenShot(driver, actionId, "masterpass" + "_post");

		
		// blur event to clock search dropdowns
		// SupportLib.blurEvent(driver, element);

	}

	public static void closeBrowser() {
		ExtentHTML.extent.endTest(ExtentHTML.testrep);
		ExtentHTML.extent.flush();
		driver.quit();
		System.out.println("Everything ends here");

	}

}

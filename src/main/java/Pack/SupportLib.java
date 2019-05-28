package Pack;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;

public class SupportLib {
	static SimpleDateFormat dateFormat;

	public static boolean readyStateURL(WebDriver driver) {
		boolean result = false;

		try {
			String script = "return document.readyState";

			for (int i = 0; i < 15; i++) {

				JavascriptExecutor executor = (JavascriptExecutor) driver;

				Object readyStateCheck = executor.executeScript(script);
				System.out.println("readyStateCheck  " + readyStateCheck);

				if (readyStateCheck.toString().equals("complete")) {
					result = true;
					break;
				}
				Thread.sleep(1000);
			}

		} catch (Exception e) {
			System.out.println("exception executing script " + e.getMessage());
			return false;

		}
		return result;

	}

	public static boolean moveToClick(WebDriver driver, WebElement element) {
		try {
			System.out.println("We are in moveToClick");
			Thread.sleep(2000);
			Actions action = new Actions(driver);
			action.moveToElement(element).perform();
			// adding some delay after moving to element
			Thread.sleep(2000);
			action.moveToElement(element).click().perform();
			Thread.sleep(3000);
			System.out.println("click is performed return true");
			return true;

		} catch (Exception e) {
			System.out.println("exception on move to  " + e.getMessage());
			return false;
		}
	}

	public static void blurEvent(WebDriver driver, WebElement element) throws Exception {

		JavascriptExecutor executor = (JavascriptExecutor) driver;

		executor.executeScript("arguments[0].blur();", element);
		(new Robot()).mouseMove(0, 0);

	}

	public static void elementToBeClickable(WebDriver driver, WebElement element) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		Thread.sleep(2000);
	}

	public static void moveToElement(WebDriver driver, WebElement element) throws InterruptedException {
		Actions actions = new Actions(driver);
		Thread.sleep(4000);
		actions.moveToElement(element).perform();
		Thread.sleep(2000);
	}


	public static String takePreScreenShot(WebDriver driver,WebElement element ,String actionId, String step) {
		// Set folder name to store screenshots.
		String destDir = ApiTest.path + "/src/main/resources/ApiScreeshots/" + ApiTest.testName;

		// Capture screenshot.
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		String destFile = step + actionId + ".png";

		try {

			// Copy paste file at destination folder location
			FileUtils.copyFile(src, new File(destDir + "/" + destFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		destFile = destDir + "/" + destFile;
		File tmpDir = new File(destFile);
		boolean exists = tmpDir.exists();
		BufferedImage img=SupportLib.loadImage(destFile);
		img=SupportLib.convertImageToRGB(img);
		Point classname = element.getLocation();
		int xcordi = classname.getX();
		System.out.println("Element's Position from left side"+xcordi +" pixels.");
		int ycordi = classname.getY();
		System.out.println("Element's Position from top"+ycordi +" pixels.");
		xcordi=xcordi  *2;
		ycordi=ycordi*2;
		//Get width of element.
		int ImageWidth = element.getSize().getWidth();
		System.out.println("Image width Is "+ImageWidth+" pixels");
		//Get height of element.
		int ImageHeight = element.getSize().getHeight();
		System.out.println("Image height Is "+ImageHeight+" pixels");
		BufferedImage imgcut=img.getSubimage(xcordi+0,ycordi+0,ImageWidth*2,ImageHeight*2);
		// Capture screenshot.
		File outputfile = new File(destFile);
		try {
			ImageIO.write(imgcut, "jpg", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return destFile;
	}
	public static String takeScreenShot(WebDriver driver, String actionId, String step) {
		// Set folder name to store screenshots.
		String destDir = ApiTest.path + "/src/main/resources/ApiScreeshots/" + ApiTest.testName;

		// Capture screenshot.
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		String destFile = step + actionId + ".png";

		try {

			// Copy paste file at destination folder location
			FileUtils.copyFile(src, new File(destDir + "/" + destFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		destFile = destDir + "/" + destFile;
		File tmpDir = new File(destFile);
		boolean exists = tmpDir.exists();

		return destFile;
	}



	public static BufferedImage loadImage(String path){
		BufferedImage img=null;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
		}
		return img;
	}


	public static BufferedImage convertImageToRGB(BufferedImage img) {
		try {
			BufferedImage rgbImg = new BufferedImage(img.getWidth(),
					img.getHeight(), BufferedImage.TYPE_INT_RGB);

			Graphics2D g2d = rgbImg.createGraphics();
			g2d.drawImage(img, 0, 0, null);
			g2d.dispose();
			return rgbImg;
		} catch (Exception e) {
		}
		return null;
	}



	public static BufferedImage cropToBrowser(BufferedImage image) {
		int w;
		int h;
		System.out.println("image height"+image.getHeight());
		System.out.println("image width"+image.getWidth());

		w = Math.min(image.getWidth(), 1024);
		h = Math.min(image.getHeight(), 960);





return null;

	}
}

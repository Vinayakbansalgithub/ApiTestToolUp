package Pack;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;

public class SupportLib {
	static SimpleDateFormat dateFormat;

	
	
static{ loadLibrary(); }
	
	
	/**
	 * Load library.
	 */
	private static void loadLibrary() {
		 System.out.println("============loading opencv library for ");
	    try {
	        InputStream in = null;
	        File fileOut = null;
	        String osName = System.getProperty("os.name");
	       
	        if(osName.startsWith("Windows")){
	            int bitness = Integer.parseInt(System.getProperty("sun.arch.data.model"));
	            if(bitness == 32){
	               
	                //in = VisionUtils.class.getResourceAsStream("/x32/opencv_java300.dll");
	                in = new FileInputStream(new File("./opencv_java320.dll"));
	                fileOut = File.createTempFile("lib", ".dll");
	            }
	            else if (bitness == 64){
	               
	                //in = VisionUtils.class.getResourceAsStream("/libopencv_java300.dll");
	                in = new FileInputStream(new File("./opencv_java320.dll"));
	                fileOut = File.createTempFile("lib", ".dll");
	            }
	            else{
	              
	                //in = VisionUtils.class.getResourceAsStream("/x32/libopencv_java300.dll");
	                in = new FileInputStream(new File("./opencv_java320.dll"));
	                fileOut = File.createTempFile("lib", ".dll");
	            }
	        } else if(osName.equals("Mac OS X")){
	        	
	        	//in = VisionUtils.class.getResourceAsStream("/libopencv_java300.dylib");
	        	File f = new File("./libopencv_java300.dylib");
	        	if (f.exists())
	        		in = new FileInputStream(f);
	        	else
	        		in = new FileInputStream(new File("./libopencv_java300.dylib"));
	            fileOut = File.createTempFile("lib", ".dylib");
	        }else if(osName.equals("Linux")){
	        	
	        	in = new FileInputStream(new File("./libopencv_java320.so"));
	            fileOut = File.createTempFile("lib", ".so");
	        }


	        OutputStream out = FileUtils.openOutputStream(fileOut);
	       
	        IOUtils.copy(in, out);
	        in.close();
	        out.close();
	     
	        System.load(fileOut.toString());
	    } catch (Exception e) {
			System.out.println("Failed to load library! " + e);
	    }
	}
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


	public static String takeElementScreenShot(WebDriver driver,WebElement element ,String actionId, String step) {
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
	
	/**
	 * Buffered image to mat. Changes buffered image to a mat object
	 *
	 * @param nbi the nbi
	 * @return the mat
	 */
	public static Mat bufferedImageToMat(BufferedImage nbi) {
		//logger.debug("====vision utils converting to mat");	 
	//must make sure buffered image is the right type
		//should be TYPE_3BYTE_BGR to convert to mat but then converted back to TYPE_INT_RGB when converting back to buffered image
	  BufferedImage bi=toBufferedImageOfType(nbi,BufferedImage.TYPE_3BYTE_BGR);
	  Mat mat=null;
	  try{
		  	mat= new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);	  	
	   		byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
	   		mat.put(0, 0, data);
	  }catch(Exception e){
	  }
	  return mat;
	}	

	/**
	 * To buffered image of type. Changes type of buffered image
	 *
	 * @param original the original
	 * @param type the type
	 * @return the buffered image
	 */
	public static BufferedImage toBufferedImageOfType(BufferedImage original, int type) {
	    if (original == null) {
	        return original;
	    }

	    // Don't convert if it already has correct type
	   
	    if (original.getType() == type) {
	        return original;
	    }

	    // Create a buffered image
	    BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), type);

	    // Draw the image onto the new buffer
	    Graphics2D g = image.createGraphics();
	    try {
	        //g.setComposite(AlphaComposite.Src);
	        g.drawImage(original, 0, 0, null);
	    }
	    finally {
	        g.dispose();
	    }

	    return image;
	}
	 public static long findDiffPercentbeforConverting(Mat img1,Mat img2) {
		 //convert to gray
		 Imgproc.cvtColor(img1, img1, Imgproc.COLOR_BGR2GRAY);
		 Imgproc.cvtColor(img2, img2, Imgproc.COLOR_BGR2GRAY);
		 Mat dst=new Mat();
		 Core.absdiff(img1, img2, dst);
		 //find percentage of non zero 
		 int imgCnt=Core.countNonZero(dst);
		 return imgCnt; 
	 }
	 
}

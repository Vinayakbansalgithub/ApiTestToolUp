package Pack;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Clock;

public class HelloWorld {

    static ChromeDriver driver = null;

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "/Users/vinayakbansal/eclipse-workspace/APITestTool/Drivers/chromedriver");
        DesiredCapabilities cap=new DesiredCapabilities();

        // Pass proxy object p
//		cap.setCapability(CapabilityType.PROXY, p);


        driver = new ChromeDriver(cap);
        driver.manage().deleteAllCookies();

        driver.manage().window().maximize();

        driver.get("http://wayplus.url.ph");


        Thread.sleep(8000);


      //  #lnkStartOrder



        String destDir = ApiTest.path + "/src/main/resources/ApiScreeshots/" + "completescreen";

        // Capture screenshot.
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        System.out.println("Performing Action clcik"+src);

        String destFile = "/Users/vinayakbansal/eclipse-workspace/APITestTool/" +"9999"+ ".png";
        System.out.println("Performing Action clcik"+destFile);


        try {

            // Copy paste file at destination folder location
            FileUtils.copyFile(src, new File( destFile));
        } catch (IOException e) {
            e.printStackTrace();
        }



      WebElement element = driver.findElement(By.cssSelector("#login_Email_username "));



        JavascriptExecutor js=(JavascriptExecutor)driver;

        // find element using id attribute

        // call the executeScript method
        js.executeScript("arguments[0].setAttribute('style,'border: solid 2px red'');", element);

        BufferedImage img=SupportLib.loadImage(destFile);
        img=SupportLib.convertImageToRGB(img);



        Point classname = element.getLocation();
        int xcordi = classname.getX();
        System.out.println("Element's Position from left side"+xcordi +" pixels.");
        int ycordi = classname.getY();
        System.out.println("Element's Position from top"+ycordi +" pixels.");
/*
xcordi=2020;
ycordi=130;*/



        xcordi=xcordi  *2;
        ycordi=ycordi*2;
        //Get width of element.
        int ImageWidth = element.getSize().getWidth();
        System.out.println("Image width Is "+ImageWidth+" pixels");

        //Get height of element.
        int ImageHeight = element.getSize().getHeight();
        System.out.println("Image height Is "+ImageHeight+" pixels");

        BufferedImage imgcut=img.getSubimage(xcordi+0,ycordi+0,ImageWidth*2,ImageHeight*2);



        String destFilecut = "/Users/vinayakbansal/eclipse-workspace/APITestTool/" +"11111"+ ".png";
        // Capture screenshot.



        File outputfile = new File(destFilecut);


        try {
            ImageIO.write(imgcut, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        driver.close();
    }
}

package ExtentReport;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;



public class ExtentReportTesting {
	
	public WebDriver driver;
	

	 public ExtentHtmlReporter htmlReporter; // Provide path of the report. Mainly used for look and feel of the project 
	 public ExtentReports extent; // To provide entry of each test case on left pane
	 public ExtentTest test;// To update status of test (For example Pass, Fail, Test)
	 
	 
	 @BeforeTest
	 public void setExtent()
	 
	 {
	

		 htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir")+"/reports/myExentReport.html"); //Specify location of report
		 htmlReporter.config().setDocumentTitle("Automaion Test Report");//title of the report
		 htmlReporter.config().setReportName("Test Case results");
		 htmlReporter.config().setTheme(Theme.DARK);
		 
		 extent = new ExtentReports();
		 extent.attachReporter(htmlReporter);
		 
		 //Passing Information 
		 extent.setSystemInfo("Host name", System.getProperty("user.name")); 
	 }
	
	 @AfterTest
	 public void endReport()
	 {
		
		 Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		 String browserName = cap.getBrowserName().toLowerCase();
		 extent.setSystemInfo("Browser Name",browserName);
		 extent.flush();
	 }
	 
	 @BeforeMethod
	 public void setUp()
	 {
		 System.setProperty("webdriver.chrome.driver", "F:\\Selenium Drivers\\chromedriver.exe");
		 driver = new ChromeDriver();
		 driver.manage().window().maximize();
		 driver.get("http://demo.nopcommerce.com/");
	 }
	
	@Test
	public void noCommerceTitleTest()
	{
		test = extent.createTest("noCommerceTitleTest");
		String title = driver.getTitle();
		Assert.assertEquals(title, "nopCommerce demo store");
	}
	
	@AfterMethod
	public void teardown(ITestResult result) throws IOException
	{
		if(result.getStatus()==ITestResult.FAILURE)
		{
			test.log(Status.FAIL, "TEST CASE FAILED" + result.getName()+ "\n" + "\n"+ result.getThrowable());
			String screenShotPath = ExtentReportTesting.getScreenshot(driver, result.getName());
			test.addScreenCaptureFromPath(screenShotPath);
			
			
		}
		else if(result.getStatus()==ITestResult.SKIP)
		{
			test.log(Status.SKIP, result.getName());
		}
		
		else if (result.getStatus() == ITestResult.SUCCESS)
		{
			test.log(Status.PASS, "TEST CASE PASSED");
		}
	}
	
	
	public static String getScreenshot(WebDriver driver, String screenShotName)
	{
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
	String destination  = System.getProperty("user.dir")+"/screenshots/"+screenShotName+dateName+".png" ;
	
	File finalDestinaion = new File (destination);
	try {
		FileHandler.copy(source, finalDestinaion);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return destination ;
	}

}

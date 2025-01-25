package ojosanco.testcomponents;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import ojosanco.resources.ExtentReporterNG;


public class Listeners extends BaseTest implements ITestListener{
	ExtentReports extent =  ExtentReporterNG.getReportObject();
	ExtentTest test;
	ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
	@Override		
    public void onFinish(ITestContext arg0) {					
        // TODO Auto-generated method stub				
		extent.flush();	
    }		

    @Override		
    public void onStart(ITestContext arg0) {					
        // TODO Auto-generated method stub				
        		
    }		

    @Override		
    public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {					
        // TODO Auto-generated method stub				
        		
    }		

    @Override		
    public void onTestFailure(ITestResult result) {					
    	extentTest.get().fail(result.getThrowable());				
    	try {
    		driver = (WebDriver)result.getTestClass().getRealClass().getField("driver").get(result.getInstance());
    		
        }catch(Exception e1) {
        	e1.printStackTrace();
        }
    	String filePath = null;
        try {
        	filePath = getScreenshot(result.getMethod().getMethodName(), driver);
        }catch(IOException e) {
        	e.printStackTrace();
        }
        test.addScreenCaptureFromPath(filePath, result.getMethod().getMethodName());
    }		

	@Override		
    public void onTestSkipped(ITestResult arg0) {					
        // TODO Auto-generated method stub				
        		
    }		

    @Override		
    public void onTestStart(ITestResult result) {					
        test = extent.createTest(result.getMethod().getMethodName());			
        extentTest.set(test);
    
    }		

    @Override		
    public void onTestSuccess(ITestResult result) {					
        // TODO Auto-generated method stub				
        test.log(Status.PASS, "Test passed");
    }		
	
}

package ojosanco.pagesobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import ojosanco.pagesobjects.abstractcomponents.AbstractComponents;


public class LoginPage extends AbstractComponents{
	WebDriver driver;
	//Page Factory
	
	//Information Input
	@FindBy(id="input-email")
	WebElement userEmail;
	@FindBy(id="input-password")
	WebElement userPassword;
	//Catch messege alert
	@FindBy(css="div[class*='alert alert-danger']")
	WebElement errorLoginMessege;
	@FindBy(css="div[class*='alert alert-success']")
	WebElement confirmForgotPasswordMessege;
	//Links
	@FindBy(css="#content .row .col-lg-6:nth-of-type(2) a")
	WebElement linkForgotPassword;
	//Submit buttons
	@FindBy(css="button[class*='btn btn-primary']")
	WebElement submitConfirmForgotPassword;
	@FindBy(css="#content .row .col-lg-6:nth-of-type(2) input.btn")
	WebElement submitLogin;
	
	

	public LoginPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	public void loginApplication(String email, String password) {
		userEmail.sendKeys(email);
		userPassword.sendKeys(password);
		submitLogin.click();
	}
	public String getErrorMessage() {
		waitForElementToAppear(errorLoginMessege);
		return errorLoginMessege.getText();
	}
	public String getConfirmForgotPasswordMessege() {
		waitForElementToAppear(confirmForgotPasswordMessege);
		return confirmForgotPasswordMessege.getText();
	}
	public void goTo() {
        driver.get("https://ecommerce-playground.lambdatest.io/index.php?route=account/login");
	}
	public void forgotPasword(String email) {
		linkForgotPassword.click();
		userEmail.sendKeys(email);
		submitConfirmForgotPassword.click();
	}
	
}

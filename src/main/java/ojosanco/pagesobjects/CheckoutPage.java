package ojosanco.pagesobjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import ojosanco.pagesobjects.abstractcomponents.AbstractComponents;

public class CheckoutPage extends AbstractComponents{
	WebDriver driver;
	public CheckoutPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	@FindBy(xpath = "//table[@id='checkout-total']//td[1]")
	List<WebElement> totalAmount;
	@FindBy(css="#payment-address div[class='form-group']:nth-of-type(2) label")
	WebElement newAddressButton;
	@FindBy(id="input-telephone")
	WebElement phoneNumber;
	@FindBy(id="input-payment-firstname")
	WebElement firstNameElement;
	@FindBy(id="input-payment-lastname")
	WebElement lastNameElement;
	@FindBy(id="input-payment-address-1")
	WebElement addressElement;
	@FindBy(id="input-payment-city")
	WebElement cityElement;
	@FindBy(id="input-payment-postcode")
	WebElement postCodeElement;
	@FindBy(id="input-payment-country")
	WebElement countryElement;
	@FindBy(id="input-payment-zone")
	WebElement regionElement;
	@FindBy(id="input-comment")
	WebElement commentElement;
	@FindBy(id="payment-new")
	WebElement paymentAddressElement;
	@FindBy(id="input-shipping-address-same")
	WebElement radioElement;
	@FindBy(xpath="//div[@class='row']/div[@class='col-lg-7 mb-5 mb-lg-0']//div[@class='custom-control custom-checkbox']/label[@for='input-agree']")
	WebElement inputTermElement;
	@FindBy(id="button-save")
	WebElement confirmOrderButton;
	@FindBy(xpath = "//div[@id='checkout-cart']//td[2]")
	List<WebElement> productNameTable;
	
	
	public List<WebElement> getCheckoutTable(){
		return totalAmount;
	}
	public List<WebElement> getProductNameTable(){
		return productNameTable;
	}
	public String getPhoneNumber() {
		return phoneNumber.getAttribute("value");
	}
	public void fillBillingAddress(String firstName, String lastName, String address, String city, String postCode, String countryValue, String regionValue, String comments) {
		if(paymentAddressElement.isDisplayed()||newAddressButton.isDisplayed()) {
			if(newAddressButton.isDisplayed()) {
				newAddressButton.click();
			}
			firstNameElement.sendKeys(firstName);
			lastNameElement.sendKeys(lastName);
			addressElement.sendKeys(address);
			cityElement.sendKeys(city);
			postCodeElement.sendKeys(postCode);
			Select countryDropdown = new Select(countryElement);
			countryDropdown.selectByVisibleText(countryValue);
			Select regionDropdown = new Select(regionElement);
			regionDropdown.selectByVisibleText(regionValue);
			commentElement.sendKeys(comments);
		}
		if(!radioElement.isSelected()) {
			radioElement.click();
		}
		commentElement.sendKeys(comments);
		inputTermElement.click();
	}
	public ConfirmOrderPage goToConfirmOderPage() {
		confirmOrderButton.click();
		return new ConfirmOrderPage(driver);
	}
}

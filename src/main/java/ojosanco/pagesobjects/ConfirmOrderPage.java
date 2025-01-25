package ojosanco.pagesobjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import ojosanco.pagesobjects.abstractcomponents.AbstractComponents;

public class ConfirmOrderPage extends AbstractComponents{
	WebDriver driver;
	public ConfirmOrderPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	@FindBy(css = ".table-responsive.mb-4 tfoot td:nth-of-type(1)")
	List<WebElement> amountPaymentElement;
	@FindBy(css = "#content .row .col-sm-6:nth-of-type(1) .card-body")
	WebElement paymentAddressElement;
	@FindBy(id="button-confirm")
	WebElement confirmButton;
	@FindBy(css=".page-title.my-3")
	WebElement placeOrderSuccessMessege;
	public List<WebElement> getAmount(){
		return amountPaymentElement;
	}
	public boolean checkPaymentAdrress(String firstName, String lastName, String address, String city, String postCode, String countryValue, String regionValue) {
		String[] paymentAddressString = paymentAddressElement.getText().split("\\n");
		
		if(paymentAddressString[0].equalsIgnoreCase(firstName.concat(" "+lastName)) &&
			paymentAddressString[1].equalsIgnoreCase(address) &&
			paymentAddressString[2].equalsIgnoreCase(city.concat(" "+postCode))&&
			paymentAddressString[3].equalsIgnoreCase(regionValue.concat(","+countryValue))){
			return true;
		}
				
		return false;
	}
	public void buyProduct() {
		confirmButton.click();
	}
	public String getbuyProductStringSuccess() {
		waitForElementToAppear(placeOrderSuccessMessege);
		return placeOrderSuccessMessege.getText().trim();
	}
}

package ojosanco.pagesobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import ojosanco.pagesobjects.abstractcomponents.AbstractComponents;

public class CartPage extends AbstractComponents {
	WebDriver driver;
	
	@FindBy(xpath = "//a[@class='page-link'][text()='>']")
	List<WebElement> nextButtonLocator;
	By layoutProduct = By.cssSelector("div[class*='product-layout']");
	By productThumbnail = By.cssSelector(".product-thumb-top");
	By productAction = By.cssSelector(".product-action");
	By productAddToCartButton = By.cssSelector("button[class*='btn-cart']");
	By filter = By.xpath(
			"//div[@id='mz-filter-content-0']/div[@class='mz-filter-group stock_status ']//div[@class='custom-control custom-checkbox']");

	@FindBy(css = "div[class*='toast m-3']")
	List<WebElement> toastProduct;
	@FindBy(css=".toast.m-3.fade.show a[class*='btn-primary']")
	WebElement editCartButtonToast;
	@FindBy(css=".toast.m-3.fade.show a[class*='btn-secondary']")
	WebElement checkoutButtonToast;
	@FindBy(css=".cart-icon svg")
	WebElement cartHeader;
	@FindBy(css="#entry_217850 a")
	WebElement editCartIconHeader;
	@FindBy(css="#entry_217850 a")
	WebElement checkoutIconHeader;
	
	public CartPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void addProductIntoCart(Actions action, WebElement product) throws InterruptedException {
		waitForAllElementsToAppear(layoutProduct);
		WebElement img = product.findElement(productThumbnail);
		action.moveToElement(img).perform();
		waitForElementToAppear(img.findElement(productAction));
		action.moveToElement(img.findElement(productAction)).perform();
		action.moveToElement(img.findElement(productAddToCartButton)).click().build().perform();
		waitForBuyProductWithSleepWindow();
	}

	public List<WebElement> getNextButton() {
		return nextButtonLocator;
	}

	public boolean clickNextButton() {
		List<WebElement> nextButton = getNextButton();
		if (!nextButtonLocator.isEmpty()) {
			nextButton.get(0).click();
			return true;
		}
		return false;
	}

	public List<WebElement> getAllProductsLayout() {
		return driver.findElements(layoutProduct);
	}

	public String getNamePoduct(WebElement product) {
		return product.getText().split("\\n")[0].trim();
	}

	public List<WebElement> filtersProduct() {
		return driver.findElements(filter);
	}

	public List<WebElement> getToastNotification(){
		return toastProduct;
	}
	public EditCartPage goToEditCartPage() throws InterruptedException {
		if(!toastProduct.isEmpty()) {
			editCartButtonToast.click();
		}
		else {
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("arguments[0].scrollIntoView(true);",cartHeader);

			cartHeader.click();
			editCartIconHeader.click();
		}
		return new EditCartPage(driver);
	}
	public CheckoutPage goToCheckoutPage() {
		if(!toastProduct.isEmpty()) {
			checkoutButtonToast.click();
		}else {
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("arguments[0].scrollIntoView(true);",cartHeader);

			cartHeader.click();
			checkoutIconHeader.click();
		}
		return new CheckoutPage(driver);
	}
	
}

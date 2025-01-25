package ojosanco.pagesobjects.abstractcomponents;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ojosanco.pagesobjects.CartPage;
import ojosanco.pagesobjects.HistoryOrderPage;

public class AbstractComponents {
	WebDriver driver;

	public AbstractComponents(WebDriver driver) {
		this.driver = driver;
	}

	// Page Factory
	@FindBy(css = ".cart-icon svg")
	WebElement cartHeader;
	@FindBy(css = "#entry_217850 a")
	WebElement cartEdit;
	@FindBy(css= "#entry_217851 a")
	WebElement checkout;
//	@FindBy(xpath = "//ul[@class='navbar-nav horizontal']/li[@class='nav-item dropdown dropdown-hoverable'][2]")
//	WebElement accountDropdown;
//	@FindBy(css=".mz-sub-menu-96.dropdown-menu")
//	WebElement accountSelections;
	@FindBy(css=".mz-sub-menu-96.dropdown-menu li:nth-of-type(2)")
	WebElement historyLink;
	@FindBy(xpath = "//div[@id='widget-navbar-217834']")
	List<WebElement> dropdown;
	@FindBy(css="ul[class*='dropdown-menu']")
	WebElement dropdownMenu;
	
	By productItem = By.xpath("./ul/li[4]");
	By accountItem = By.xpath("./ul/li[6]");
	By productsDropdown = By.cssSelector("li.mega-menu");	
	By linksProductPage = By.cssSelector("#entry_217834 li[class*='dropdown dropdown-hoverable mega-menu'] a");
	
	public void waitForElementToAppear(WebElement el) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(el));
	}
	public void waitForElementToAppear(By findBy) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
	}
	public void waitForElementToClick(WebElement el) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(el));
	}
	public void waitForAllElementsToAppear(By el) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(el));
	}
	public void waitForAllWebElementsToAppear(List<WebElement> el) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfAllElements(el));
	}
	public void waitForSpinnerToDisappear() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("spinner"))); 
	}
	public void waitForElementsWithSleepWindow() throws InterruptedException {
		Thread.sleep(2000);
	}
	public void waitForBuyProductWithSleepWindow() throws InterruptedException {
		Thread.sleep(1000);
	}
	public void chooseDropdownCategory(Actions actions, By item) throws InterruptedException {
		for(WebElement itemDropdown : dropdown) {
			WebElement category = itemDropdown.findElement(item);
			if(category.isDisplayed()) {
				actions.moveToElement(category).perform();
				waitForElementsWithSleepWindow();
//				waitForElementToAppear(dropdownMenu);
				break;
			}
		}
	}
	public CartPage goToProductItemDropdown(Actions actions) throws InterruptedException {
		chooseDropdownCategory(actions, productItem);
		return new CartPage(driver);
	}
	public HistoryOrderPage goToOrderHistoryDropdown(Actions actions) throws InterruptedException {
		chooseDropdownCategory(actions, accountItem);
		actions.moveToElement(historyLink).click().build().perform();
		return new HistoryOrderPage(driver);
		
	}
	public List<WebElement> getproductCategoryLinks() {
		return driver.findElements(linksProductPage);
	}
	public void clickProductCategory(Actions actions, WebElement link) throws InterruptedException {
		actions.moveToElement(link).click().build().perform();
		waitForElementsWithSleepWindow();
	}
	public WebElement getLinkCategory(String category) {
		return driver.findElement(By.cssSelector("a[title='" + category + "']"));
	}
}

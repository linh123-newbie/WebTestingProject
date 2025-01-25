package ojosanco.pagesobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import ojosanco.pagesobjects.abstractcomponents.AbstractComponents;

public class HistoryOrderPage extends AbstractComponents{
	WebDriver driver;
	public HistoryOrderPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	@FindBy(xpath = "//div[@class='table-responsive']//tbody//td[1]")
	List<WebElement> idOrderElement;
	By namePurchaserElement = By.xpath("./following-sibling::td[1]");
	By numberOfProductsElement = By.xpath("./following-sibling::td[2]");
	By statusProductsElement = By.xpath("./following-sibling::td[3]");
	By totalProductsElement = By.xpath("./following-sibling::td[4]");
	By dateOrderProductsElement = By.xpath("./following-sibling::td[5]");
	By viewOrderElement = By.xpath("./following-sibling::td[6]//a");
	
	
	public boolean checkOrder(String name, String date, String numberOfProductData, String statusData, double total, List<String> productsNeed) throws InterruptedException {
		for (WebElement tdName : idOrderElement) {
			if(tdName.findElement(namePurchaserElement).getText().trim().equalsIgnoreCase(name) &&
					tdName.findElement(dateOrderProductsElement).getText().trim().equalsIgnoreCase(date)) {
				if(!tdName.findElement(statusProductsElement).getText().trim().equalsIgnoreCase(statusData)) {
					return false;
				}
				if(!tdName.findElement(numberOfProductsElement).getText().trim().equalsIgnoreCase(numberOfProductData)) {
					return false;
				}
				if(Float.parseFloat(tdName.findElement(totalProductsElement).getText().split("\\$")[1].replace(",", "").trim())>total) {
					return false;
				}
				tdName.findElement(viewOrderElement).click();
				break;
//				Assert.assertEquals(tdName.findElement(statusProductsElement).getText().trim(), statusData);
//				Assert.assertEquals(tdName.findElement(numberOfProductsElement).getText().trim(), numberOfProductData);
//				Assert.assertEquals(Float.parseFloat(tdName.findElement(totalProductsElement).getText().split("\\$")[1].replace(",", "").trim()), total);
//				tdName.findElement(viewOrderElement).click();
			}
		}
		return true;
	}
	public String getIdOrder(WebElement idOrderdElement) {
		return idOrderdElement.getText().trim();
	}
	
	public List<WebElement> getProductNameTable(){
		return idOrderElement;
	}
	
}

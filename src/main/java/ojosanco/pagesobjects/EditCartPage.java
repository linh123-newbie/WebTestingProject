package ojosanco.pagesobjects;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.google.common.util.concurrent.AtomicDouble;

import ojosanco.pagesobjects.abstractcomponents.AbstractComponents;

public class EditCartPage extends AbstractComponents {
	WebDriver driver;

	public EditCartPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//div[@id='checkout-cart']//form//td[2]")
	List<WebElement> productNameColumn;
	By deleteButton = By.xpath("./following-sibling::td[2]//button[@class='btn btn-danger']");
	By quantityElement = By.xpath("./following-sibling::td[2]//input[@type='text']");
	By priceElement = By.xpath("./following-sibling::td[3]");
	By amountTotalCheck = By.xpath("./following-sibling::td[1]");
	@FindBy(css = "#content p")
	WebElement emptyCartMessegeElement;
	By updateQuantityButton = By.xpath("./following-sibling::td[2]//button[@type='submit']");
	@FindBy(css = ".alert.alert-success.alert-dismissible")
	WebElement updateMessege;
	By updateQuantityInput = By.xpath("./following-sibling::td[2]//input");
	@FindBy(css = "div[class*='buttons'] a:nth-of-type(2)")
	WebElement checkoutButton;
	@FindBy(xpath = "//div[@class='row mb-3 align-items-end']/div[2]//table//td[1]")
	List<WebElement> totalCheckElement;
	
	public List<WebElement> getTotalCheckTable(){
		return totalCheckElement;
	}
	public String getNameProductTable(WebElement product) {
		CartPage cartPage = new CartPage(driver);
		return cartPage.getNamePoduct(product);
	}

	public Boolean checkEnoughProduct(List<String> productsNeed, List<WebElement>productNameColumn){
		List<String> listProducts = productNameColumn.stream().map(col -> getNameProductTable(col))
				.collect(Collectors.toList());
		listProducts.forEach(s->System.out.println(s));
		if (listProducts.size() >= productsNeed.size()) {
			return listProducts.stream().allMatch(product -> productsNeed.contains(product));
		} else
			return false;
	}

	public void deleteOneProduct(int index) throws InterruptedException {
		productNameColumn.get(index).findElement(deleteButton).click();
		waitForElementsWithSleepWindow();
	}

	public List<WebElement> productTable() {
		return productNameColumn;
	}

	public String checkEmptyCart() {
		return emptyCartMessegeElement.getText().trim();
		
	}

	public double getSumTotalProductTable(List<WebElement> productTable) {
		AtomicDouble sumTotal = new AtomicDouble(0);
		productTable.forEach(product -> {
			String quantity = product.findElement(quantityElement).getAttribute("value");
			String price = product.findElement(priceElement).getText().split("\\$")[1];
			sumTotal.addAndGet(Integer.parseInt(quantity) * Float.parseFloat(price));
		});
		return sumTotal.get();
	}
	public double getsubTotalSum(List<WebElement> totalCheck) {
		for (WebElement amountTitle : totalCheck) {
			if (amountTitle.getText().trim().contains("Sub-Total")) {
				System.out.println("Sub-Total");
				return Float.parseFloat(
						amountTitle.findElement(amountTotalCheck).getText().split("\\$")[1].replace(",", ""));
			}
		}
		return 0;
	}
	public double getSumTotalReality(int quantity, List<WebElement> totalCheck) {
		double sum = 0;
		double subTotalSum = 0;
		for (WebElement amountTitle : totalCheck) {
			if (amountTitle.getText().trim().contains("Sub-Total")) {
				subTotalSum = getsubTotalSum(totalCheck);
				sum += subTotalSum;
			}
			if (amountTitle.getText().trim().contains("Eco Tax")) {
				sum += quantity * 2.0;
			}
			if (amountTitle.getText().trim().contains("VAT")) {
				float vat = Float.parseFloat(
						amountTitle.findElement(amountTotalCheck).getText().split("\\$")[1].replace(",", ""));

				sum += vat;
			}
			if (amountTitle.getText().trim().contains("Flat Shipping Rate")) {
				float ship = Float.parseFloat(
						amountTitle.findElement(amountTotalCheck).getText().split("\\$")[1].replace(",", ""));
				sum += ship;
			}
		}
		return sum;
	}
	public double getSumTotalPrice(List<WebElement> totalCheck) {
		for (WebElement amountTitle : totalCheck) {
			if (amountTitle.getText().trim().equalsIgnoreCase("Total:")) {
				return Float.parseFloat(
						amountTitle.findElement(amountTotalCheck).getText().split("\\$")[1].replace(",", ""));
			}
		}
		return 0;
	}
	public boolean checkPrice(int quantity, List<WebElement> totalCheck) {
		double sum = 0;
		double subTotalSum = 0;
		for (WebElement amountTitle : totalCheck) {
			if (amountTitle.getText().trim().contains("Sub-Total")) {
				subTotalSum = getsubTotalSum(totalCheck);
				sum += subTotalSum;
			}
			if (amountTitle.getText().trim().contains("Eco Tax")) {
				float ecoTax = Float.parseFloat(
						amountTitle.findElement(amountTotalCheck).getText().split("\\$")[1].replace(",", ""));
				if(ecoTax>(quantity * 2)) {
					return false;
				}
			}
			if (amountTitle.getText().trim().contains("VAT")) {
				float vat = Float.parseFloat(
						amountTitle.findElement(amountTotalCheck).getText().split("\\$")[1].replace(",", ""));
				if(vat>(subTotalSum * 0.2)) {
					return false;
				}
			}
			if (amountTitle.getText().trim().contains("Flat Shipping Rate")) {
				float ship = Float.parseFloat(
						amountTitle.findElement(amountTotalCheck).getText().split("\\$")[1].replace(",", ""));
				if(ship>5.0) {
					return false;
				}
			}
			if (amountTitle.getText().trim().equalsIgnoreCase("Total:")) {
				float totalPrice = Float.parseFloat(
						amountTitle.findElement(amountTotalCheck).getText().split("\\$")[1].replace(",", ""));
				if(sum>totalPrice) {
					return false;
				}
			}
		}
		return true;
	}
//	public void updateProduct(List<HashMap<String, Integer>> quantity) throws InterruptedException {
//		waitForBuyProductWithSleepWindow();
//		List<WebElement> productNameTable = productTable();
//		for (int i = 0; i < productNameTable.size(); i++) {
//			WebElement s = productNameTable.get(i);
//			for (HashMap<String, Integer> hashMap : quantity) {
//				String productName = hashMap.keySet().iterator().next();
//				
//				if (s.getText().split("\\n")[0].trim().equalsIgnoreCase(productName)&&!s.findElement(updateQuantityInput).getAttribute("value").equalsIgnoreCase(String.valueOf(hashMap.get(productName)))) {
//					s.findElement(updateQuantityInput).clear();
//					int quantityValue = hashMap.get(productName);
//					s.findElement(updateQuantityInput).sendKeys(String.valueOf(quantityValue));
//					s.findElement(updateQuantityButton).click();
//				
//					waitForElementToAppear(updateMessege);
//					Assert.assertEquals(updateMessege.getText().split("\\n")[0], "Success: You have modified your shopping cart!");
//					break;
//				}
//			}
//			productNameTable = productTable();
//		}
//	
//	}
	public void updateProductWithHashMap(HashMap<String, Integer> hashMap, WebElement s, String productName) {
			s.findElement(updateQuantityInput).clear();
			int quantityValue = hashMap.get(productName);
			s.findElement(updateQuantityInput).sendKeys(String.valueOf(quantityValue));
			s.findElement(updateQuantityButton).click();
			waitForElementToAppear(updateMessege);
	}
	public void updateProduct(WebElement element, List<Integer> quantity, int index) {
		element.findElement(updateQuantityInput).clear();
		int quantityValue = quantity.get(index);
		element.findElement(updateQuantityInput).sendKeys(String.valueOf(quantityValue));
		element.findElement(updateQuantityButton).click();
		waitForElementToAppear(updateMessege);
}
	public String getUpdateMessege() {
		return updateMessege.getText().split("\\n")[0];
	}
	public CheckoutPage goToCheckoutPage() {
		checkoutButton.click();
		return new CheckoutPage(driver);
	}

	public int getAllProductQuantity(List<WebElement> productTableName) {
		AtomicInteger quantity = new AtomicInteger(0);
		productTableName.forEach(s -> {
			quantity.addAndGet(Integer.parseInt(s.findElement(updateQuantityInput).getAttribute("value")));
		});
		return quantity.get();
	}
	public By getUpdataQuantityInput() {
		return updateQuantityInput;
	}

}

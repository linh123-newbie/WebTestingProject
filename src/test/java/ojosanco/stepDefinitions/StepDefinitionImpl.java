package ojosanco.stepDefinitions;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import ojosanco.testcomponents.BaseTest;
import ojosanco.pagesobjects.CartPage;
import ojosanco.pagesobjects.CheckoutPage;
import ojosanco.pagesobjects.ConfirmOrderPage;
import ojosanco.pagesobjects.EditCartPage;
import ojosanco.pagesobjects.LoginPage;

public class StepDefinitionImpl extends BaseTest {

	public CartPage cartPage;
	public EditCartPage editCartPage;
	public LoginPage loginPage;
	public CheckoutPage checkoutPage;
	public ConfirmOrderPage confirmOrderPage;
	private List<String> productsNeed;
	private List<WebElement> productTable;
	private List<Integer> quantityList;
	private List<String> enoughProduct;
	private double subTotal;
	private double total;
	private int quantity;

	@Given("I landed on Ecommerce Page")
	public void I_landed_on_Ecommerce_Page() throws IOException {
		loginPage = launchApplication();
		// code
	}

	@Given("^Logged in with username (.+) and password (.+)$")
	public void logged_in_username_and_password(String username, String password) {
		loginPage.loginApplication(username, password);
	}

	@When("^I add products to Cart with the following details:$")
	public void i_add_products_to_cart(DataTable dataTable) throws InterruptedException {
		List<Map<String,String>> data = dataTable.asMaps();
		String productDataTable = data.get(0).get("productName");
		productsNeed = Arrays.asList(productDataTable.split(",")).stream()
				.map(product->product.trim())
				.collect(Collectors.toList());
	
		Actions actions = new Actions(driver);
		// Hover product category
		cartPage = loginPage.goToProductItemDropdown(actions);
		List<WebElement> items = loginPage.getproductCategoryLinks();
		List<WebElement> products;
		WebElement link;
		Set<String> seenProductsName = new HashSet<>();

		for (int i = 1; i <= items.size(); i++) {
			if (seenProductsName.size() < productsNeed.size()) {
				// Click product category
				loginPage.goToProductItemDropdown(actions);
				items = loginPage.getproductCategoryLinks();
				link = items.get(i);
				loginPage.clickProductCategory(actions, link);

				// Buy product
				do {

					products = cartPage.getAllProductsLayout();
					for (WebElement product : products) {

						String productName = cartPage.getNamePoduct(product);
						boolean check = productsNeed.contains(productName) && !seenProductsName.contains(productName);
						if (check) {

							seenProductsName.add(productName);
							Actions action = new Actions(driver);
							try {
								cartPage.addProductIntoCart(action, product);
								if (seenProductsName.size() >= productsNeed.size()) {
									break;
								}
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
					;

				} while ((seenProductsName.size() < productsNeed.size()) && cartPage.clickNextButton());

			}
		}
	}

	@When("^I go to edit cart page")
	public void go_to_edit_cart_page() throws InterruptedException {
		loginPage.waitForElementsWithSleepWindow();
		editCartPage = cartPage.goToEditCartPage();
		productTable = editCartPage.productTable();
		// Check enough product that we need
		Boolean check = editCartPage.checkEnoughProduct(productsNeed, productTable);
		Assert.assertTrue(check);
	}

	@When("^remove out of stock products$")
	public void remove_out_of_stock_product() throws InterruptedException {
		// Remove out of stock products
		for (int i = 0; i < productTable.size(); i++) {
			WebElement productName = productTable.get(i);
			if (productName.getText().contains("***")) {
				editCartPage.deleteOneProduct(i);
				productTable = editCartPage.productTable();
				i--;
			}
		}
		if (productTable.isEmpty()) {
			Assert.assertEquals(editCartPage.checkEmptyCart(), "Your shopping cart is empty!");
			driver.close();
		}
	}

	@When("^update quantity product$")
	public void update_quantity_product(DataTable dataTable) throws InterruptedException {
		List<Map<String,String>> data = dataTable.asMaps();
		String quantityString = data.get(0).get("quantity");
		quantityList = Arrays.asList(quantityString.split(",")).stream().map(s -> Integer.parseInt(s.trim()))
				.collect(Collectors.toList());

		for (int i = 0; i < productTable.size(); i++) {
			WebElement s = productTable.get(i);
			for (int j = 0; j < quantityList.size(); j++) {
				if (productsNeed.get(j).equalsIgnoreCase(s.getText().split("\\n")[0].trim())
						&& !s.findElement(editCartPage.getUpdataQuantityInput()).getAttribute("value")
								.equalsIgnoreCase(String.valueOf(quantityList.get(i)))) {
					editCartPage.updateProduct(s, quantityList, j);
					Assert.assertEquals(editCartPage.getUpdateMessege(),
							"Success: You have modified your shopping cart!");
					break;
				}
			}
			productTable = editCartPage.productTable();
		}
		// Check if correct total amount
		List<WebElement> totalTable = editCartPage.getTotalCheckTable();
		productTable = editCartPage.productTable();
		enoughProduct = productTable.stream().map(s -> editCartPage.getNameProductTable(s))
				.collect(Collectors.toList());
		quantity = editCartPage.getAllProductQuantity(productTable);
		double sumTotalProduct = editCartPage.getSumTotalProductTable(productTable);
		Assert.assertTrue(editCartPage.checkPrice(quantity, productTable));
		subTotal = editCartPage.getsubTotalSum(totalTable);
		double sumTotalReality = editCartPage.getSumTotalReality(quantity, totalTable);
		System.out.println(sumTotalProduct + "sdb");
		Assert.assertEquals(sumTotalProduct, subTotal);
		Assert.assertEquals(sumTotalReality, editCartPage.getSumTotalPrice(totalTable));
		System.out.println(sumTotalReality + "sdb");
	}

	@When("^I go to checkout page to fill address information:$")
	public void checkout_submit_order(DataTable dataTable) {
		List<Map<String,String>> data = dataTable.asMaps();
		String firstName = "", lastName = "", address = "", city = "", postCode = "", country = "", region = "", comments = "", telephone = "";
		for (Map<String, String> map : data) {
			if(!map.get("firstName").isEmpty()) {
				firstName = map.get("firstName");
			}
			if(!map.get("lastName").isEmpty()) {
				lastName = map.get("lastName");
			}
			if(!map.get("address").isEmpty()) {
				address = map.get("address");
			}
			if(!map.get("city").isEmpty()) {
				city = map.get("city");
			}
			if(!map.get("postCode").isEmpty()) {
				postCode = map.get("postCode");
			}
			if(!map.get("country").isEmpty()) {
				country = map.get("country");
			}
			if(!map.get("region").isEmpty()) {
				region = map.get("region");
			}
			if(!map.get("comments").isEmpty()) {
				comments = map.get("comments");
			}
			if(!map.get("telephone").isEmpty()) {
				telephone = map.get("telephone");
			}
		}
		checkoutPage = editCartPage.goToCheckoutPage();

		// Check out page

		// Check enough products
		Boolean checkProductPayment = editCartPage.checkEnoughProduct(enoughProduct,
				checkoutPage.getProductNameTable());
		Assert.assertTrue(checkProductPayment);
		// Check payment
		List<WebElement> totalCheckout = checkoutPage.getCheckoutTable();
		double subTotalCheckout = editCartPage.getsubTotalSum(totalCheckout);
		Assert.assertEquals(subTotalCheckout, subTotal);
		Assert.assertTrue(editCartPage.checkPrice(quantity, totalCheckout));
		total = editCartPage.getSumTotalReality(quantity, totalCheckout);
		Assert.assertEquals(total, editCartPage.getSumTotalPrice(totalCheckout));

		// Check if phone number is correct
		Assert.assertEquals(checkoutPage.getPhoneNumber(), telephone);
		checkoutPage.fillBillingAddress(firstName, lastName, address, city, postCode, country, region, comments);
	}
	
	@When("^I go to confirm page to check address information and buy products$")
	public void submit_the_order(DataTable dataTable) {
		confirmOrderPage = checkoutPage.goToConfirmOderPage();
		// Check if the payment amount is equal to total amount in confirm page
		Assert.assertEquals(total, editCartPage.getSumTotalPrice(confirmOrderPage.getAmount()));
		// Check payment address
		
		List<Map<String,String>> data = dataTable.asMaps();
		String firstName = "", lastName = "", address = "", city = "", postCode = "", country = "", region = "", comments = "", telephone = "";
		for (Map<String, String> map : data) {
			if(!map.get("firstName").isEmpty()) {
				firstName = map.get("firstName");
			}
			if(!map.get("lastName").isEmpty()) {
				lastName = map.get("lastName");
			}
			if(!map.get("address").isEmpty()) {
				address = map.get("address");
			}
			if(!map.get("city").isEmpty()) {
				city = map.get("city");
			}
			if(!map.get("postCode").isEmpty()) {
				postCode = map.get("postCode");
			}
			if(!map.get("country").isEmpty()) {
				country = map.get("country");
			}
			if(!map.get("region").isEmpty()) {
				region = map.get("region");
			}
		}
		
		Boolean checkConfirm = confirmOrderPage.checkPaymentAdrress(firstName, lastName,
				address, city, postCode, country,
				region);
		Assert.assertTrue(checkConfirm);
	}
	
	@Then("{string} message is displayed on ConfirmationPage")
	public void message_displayed_confirmationPage(String string) {
		String confirmMessage = confirmOrderPage.getbuyProductStringSuccess();
		Assert.assertTrue(confirmMessage.equalsIgnoreCase(string));
		driver.close();
	}

	@Then("^\"([^\"]*)\" message is displayed$")
	public void something_message_is_displayed(String strArg1) throws Throwable {

		Assert.assertEquals(strArg1, loginPage.getErrorMessage());
		driver.close();
	}

}

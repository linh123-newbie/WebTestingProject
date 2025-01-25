package ojosanco.tests;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ojosanco.pagesobjects.CartPage;
import ojosanco.pagesobjects.CheckoutPage;
import ojosanco.pagesobjects.ConfirmOrderPage;
import ojosanco.pagesobjects.EditCartPage;
import ojosanco.pagesobjects.HistoryOrderPage;
import ojosanco.pagesobjects.LoginPage;
import ojosanco.testcomponents.BaseTest;

public class CartTest extends BaseTest {
	String filePath = "jdbc:mysql://localhost:3306/usertest";
	String root = "root";
	String password = "01656107662aA&";
	String query = "select users.userName, users.password, products.name, orderItems.quantity, products.stockQuantity, users.lastName, users.firstName, users.telephone, orders.address, orders.city, orders.postCode, orders.country, orders.region ,orders.comments, orders.numberOfProducts, orders.status"
			+ " from orderItems" + " join users on orderItems.idUser=users.id"
			+ " join products on products.productId=orderItems.productId"
			+ " join orders on orderItems.idUser=orders.userId" + " where users.id=2";

	private double total = 0;
	List<String> enoughProduct = new ArrayList<>();

	@Test(enabled = true, dataProvider = "getData")
	public void addMultipleProductsToCart(HashMap<String, String> input)
			throws InterruptedException, IOException, SQLException {
		loginPage.loginApplication(input.get("userName"), input.get("password"));
		List<String> productsNeed = getProductNeed();
		Actions actions = new Actions(driver);
		// Hover product category
		CartPage cartPage = loginPage.goToProductItemDropdown(actions);
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
		loginPage.waitForElementsWithSleepWindow();
		EditCartPage editCartPage = cartPage.goToEditCartPage();
		List<WebElement> productTable = editCartPage.productTable();
		// Check enough product that we need
		Boolean check = editCartPage.checkEnoughProduct(productsNeed, productTable);
		Assert.assertTrue(check);
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
		} else {

			// Update quantity product
			for (int i = 0; i < productTable.size(); i++) {
				WebElement s = productTable.get(i);
				for (HashMap<String, Integer> hashMap : getQuantity()) {
					String productName = hashMap.keySet().iterator().next();
					if (s.getText().split("\\n")[0].trim().equalsIgnoreCase(productName)
							&& !s.findElement(editCartPage.getUpdataQuantityInput()).getAttribute("value")
									.equalsIgnoreCase(String.valueOf(hashMap.get(productName)))) {

						editCartPage.updateProductWithHashMap(hashMap, s, productName);
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
			int quantity = editCartPage.getAllProductQuantity(productTable);
			double sumTotalProduct = editCartPage.getSumTotalProductTable(productTable);
			Assert.assertTrue(editCartPage.checkPrice(quantity, productTable));
			double subTotal = editCartPage.getsubTotalSum(totalTable);
			double sumTotalReality = editCartPage.getSumTotalReality(quantity, totalTable);
			Assert.assertEquals(sumTotalProduct, subTotal);
			Assert.assertEquals(sumTotalReality, editCartPage.getSumTotalPrice(totalTable));

			CheckoutPage checkoutPage = editCartPage.goToCheckoutPage();

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
			Assert.assertEquals(checkoutPage.getPhoneNumber(), input.get("telephone"));
			checkoutPage.fillBillingAddress(input.get("firstName"), input.get("lastName"), input.get("address"),
					input.get("city"), input.get("postCode"), input.get("country"), input.get("region"),
					input.get("comments"));
			// Confirm page
			ConfirmOrderPage confirmPage = checkoutPage.goToConfirmOderPage();
			// Check if the payment amount is equal to total amount in confirm page
			Assert.assertEquals(total, editCartPage.getSumTotalPrice(confirmPage.getAmount()));
			// Check payment address
			Boolean checkConfirm = confirmPage.checkPaymentAdrress(input.get("firstName"), input.get("lastName"),
					input.get("address"), input.get("city"), input.get("postCode"), input.get("country"),
					input.get("region"));
			Assert.assertTrue(checkConfirm);
			// Buy product
			confirmPage.buyProduct();
			Assert.assertEquals(confirmPage.getbuyProductStringSuccess(), "Your order has been placed!");
		}

	}

	@Test(enabled = true, dataProvider = "getData", dependsOnMethods = "addMultipleProductsToCart")
	public void OrderHistoryTest(HashMap<String, String> input) throws InterruptedException {
		Actions action = new Actions(driver);
		if (!enoughProduct.isEmpty()) {
			// Login
			loginPage.loginApplication(input.get("userName"), input.get("password"));
			HistoryOrderPage historyPage = loginPage.goToOrderHistoryDropdown(action);
			// History order page
			String fullName = input.get("firstName").concat(" " + input.get("lastName"));
			String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
			// Check information of order
			Assert.assertTrue(historyPage.checkOrder(fullName, timeStamp, input.get("numberOfProducts"),
					input.get("status"), total, enoughProduct));
			EditCartPage editCartPage = new EditCartPage(driver);
			// Check enough product
			Assert.assertTrue(editCartPage.checkEnoughProduct(enoughProduct, historyPage.getProductNameTable()));
		}
	}

	@DataProvider
	public Object[][] getData() throws IOException, SQLException {
		List<HashMap<String, String>> data = getDatabaseDataMap(filePath, root, password, query);

		return new Object[][] { { data.get(0) } };
	}

	public List<String> getProductNeed() throws IOException, SQLException {
		List<String> productNeed = new ArrayList<>();
		List<HashMap<String, String>> data = getDatabaseDataMap(filePath, root, password, query);
		for (HashMap<String, String> product : data) {
			if (product.get("name") != null) {
				productNeed.add(product.get("name"));
			}
		}

		return productNeed;
	}

	public List<HashMap<String, Integer>> getQuantity() throws IOException, SQLException {
		List<HashMap<String, String>> data = getDatabaseDataMap(filePath, root, password, query);
		List<HashMap<String, Integer>> quantityProduct = new ArrayList<>();
		for (HashMap<String, String> product : data) {
			if (product.get("quantity") != null && !product.get("stockQuantity").equalsIgnoreCase("0")) {
				HashMap<String, Integer> quantity = new HashMap<>();
				quantity.put(product.get("name").trim(), Integer.parseInt(product.get("quantity")));
				quantityProduct.add(quantity);
			}
		}
		return quantityProduct;
	}
}

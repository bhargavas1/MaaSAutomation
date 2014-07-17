package com.phunware.util;

import io.selendroid.SelendroidCapabilities;
import io.selendroid.SelendroidConfiguration;
import io.selendroid.SelendroidDriver;
import io.selendroid.SelendroidLauncher;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import com.phunware.constants.GlobalConstants;
import com.phunware.test.Suite;

/**
 * @author bhargavas This class contains all Selenium methods
 * 
 */
public class SeleniumUtils extends Suite {
	private static Logger logger = Logger.getLogger(SeleniumUtils.class);
	private static WebDriver driver = null;

	/**
	 * This method is used to launch browser as per the config properties
	 */
	public static boolean launchBrowser() {
		try {
			// Identify the platform on which user is working on
			Platform current = Platform.getCurrent();
			if (current.toString().equalsIgnoreCase("MAC")) {
				if (configproperties.get(0).equalsIgnoreCase("FIREFOX")) {
					driver = new FirefoxDriver();
				} else if (configproperties.get(0).equalsIgnoreCase("IE")) {
					System.setProperty("webdriver.ie.driver",
							GlobalConstants.iebrowser);
					driver = new InternetExplorerDriver();
				} else if (configproperties.get(0).equalsIgnoreCase("CHROME")) {
					System.setProperty("webdriver.chrome.driver",
							GlobalConstants.CHROME_DRIVER_FOR_MAC);
					driver = new ChromeDriver();
				} else if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
					driver = new SafariDriver();
				}
			} else {
				if (configproperties.get(0).equalsIgnoreCase("FIREFOX")) {
					driver = new FirefoxDriver();
				} else if (configproperties.get(0).equalsIgnoreCase("IE")) {
					System.setProperty("webdriver.ie.driver",
							GlobalConstants.iebrowser);
					driver = new InternetExplorerDriver();
				} else if (configproperties.get(0).equalsIgnoreCase("CHROME")) {
					System.setProperty("webdriver.chrome.driver",
							GlobalConstants.chromebrowser);
					driver = new ChromeDriver();
				} else if (configproperties.get(0).equalsIgnoreCase("SAFARI")) {
					driver = new SafariDriver();
				} else if (configproperties.get(0).equalsIgnoreCase("ANDROID")) {
					SelendroidConfiguration config = new SelendroidConfiguration();
					// Add the selendroid-test-app to the standalone server
					config.addSupportedApp("D:\\mobile_Automation\\android\\"
							+ "adt-bundle-windows-x86_64-20130219\\sdk\\"
							+ "platform-tools\\selendroid-test-app-0.9.0 .apk");
					SelendroidLauncher selendroidServer = new SelendroidLauncher(
							config);
					selendroidServer.lauchSelendroid();
					// Create the selendroid capabilities and specify to use an
					// emulator and selendroid's test app
					SelendroidCapabilities caps = new SelendroidCapabilities(
							"io.selendroid.testapp:0.9.0");
					driver = new SelendroidDriver(caps);
				}
			}
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			driver.get(configproperties.get(1));
			driver.manage().window().maximize();
			ReportUtils.setWebDriverReports(driver);
			return true;
		} catch (Exception e) {
			logger.error("Error while Initializing the driver...");
			logger.error(e);
			return false;
		}
	}

	/**
	 * @param type
	 * @param value
	 * 
	 *            This method is used to clean the field
	 */
	public static void clean(String type, String value) {
		try {
			WebElement element = null;
			element = findobject(type, value);
			element.clear();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * @param locatortype
	 * @param locatorvalue
	 * @return element if true else null find the element in the application
	 * 
	 */
	public static WebElement findobject(String locatortype, String locatorvalue) {
		WebElement element = null;
		try {
			if (locatortype.equalsIgnoreCase("name")) {
				element = driver.findElement(By.name(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("link")) {
				element = driver.findElement(By.linkText(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("id")) {
				element = driver.findElement(By.id(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("class")) {
				element = driver.findElement(By.className(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("cssSelector")) {
				element = driver.findElement(By.cssSelector(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("xpath")) {
				element = driver.findElement(By.xpath(locatorvalue));
			}
			return element;
		} catch (Exception e) {
			logger.error("Unable to identify the element with id "
					+ locatortype + " " + "and value " + locatorvalue);
		}
		return null;
	}

	/**
	 * @param alertText
	 * @param expected
	 *            compares the expected text with return text and asserts the
	 *            user
	 * @return
	 */
	public static boolean assertEqual(String alertText, String expected) {
		boolean assertionFlag = false;
		try {
			if (alertText.equalsIgnoreCase(expected)) {
				assertionFlag = true;
				return assertionFlag;
			} else {
				logger.error("Assertion error");
				return assertionFlag;
			}
		} catch (Exception e) {
			logger.error("AssertionEqual has failed.And the error message is "
					+ e);
			return assertionFlag;
		}

	}

	/**
	 * @param userelement
	 * @param passwordelement
	 * @param button
	 * @param username
	 * @param password
	 *            performs login operation
	 */
	public static void login(WebElement userelement,
			WebElement passwordelement, WebElement button, String username,
			String password) {
		try {
			userelement.sendKeys(username);
			passwordelement.sendKeys(password);
			button.click();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * @param errorelement
	 * @return text of the object if exists else null
	 * 
	 */
	public static String getText(WebElement element) {
		try {
			return element.getText();
		} catch (Exception e) {
			logger.error("Error while reading the text of a element. The error message is "
					+ e);
			return null;
		}
	}

	/**
	 * @param locatortype
	 * @param locatorvalue
	 *            performs click operation on element
	 */
	public static void click(String locatortype, String locatorvalue) {
		WebElement element = null;
		try {
			element = findobject(locatortype, locatorvalue);
			if (element != null) {
				element.click();
			} else {
				logger.error("Unable to identify the element. So Click operation is not performed");
			}
		} catch (Exception e) {
			logger.error("Error while clicking on element");
		}
	}

	/**
	 * close the browser
	 */
	public static void closeBrowser() {
		try {
			// driver.close();
			driver.quit();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/*
	 * public static void captureScreenShot(String filename) { try { File
	 * scrnsht = ((TakesScreenshot) driver) .getScreenshotAs(OutputType.FILE);
	 * FileUtils.copyFile(scrnsht, new File( GlobalConstants.SCREENSHOT_PATH +
	 * filename + ".jpg")); } catch (Exception e) { logger.error(e); } }
	 */

	/*
	 * public static boolean assertTrue(String locatortype, String locatorvalue)
	 * { WebElement element = null; boolean assertionFlag = false; try { element
	 * = findobject(locatortype, locatorvalue); if (element != null) {
	 * assertionFlag = true; return assertionFlag; } else { return
	 * assertionFlag; } } catch (Exception e) {
	 * logger.error("Unable to identify the element with " + locatortype + " " +
	 * "and" + " " + locatorvalue); } return assertionFlag; }
	 */

	/**
	 * @param element
	 * @param string
	 *            this method is used to enter the text in textbox
	 */
	public static void type(WebElement element, String string) {
		try {
			element.sendKeys(string);
		} catch (Exception e) {
			logger.error("Error while typing the text" + e);
		}
	}

	/**
	 * @param locatortype
	 * @param locatorvalue
	 * @param option
	 *            - option value text
	 * @return
	 * 
	 *         this method is used to select the item from the drop down using
	 *         text
	 */
	public static boolean selectDropdownByText(String locatortype,
			String locatorvalue, String option) {
		WebElement element = null;
		try {
			element = findobject(locatortype, locatorvalue);
			Select select = new Select(element);
			select.selectByVisibleText(option);
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/*
	 * public static String getDropDownValue(String locatortype, String
	 * locatorvalue) { WebElement element = null; String value = null; try {
	 * element = findobject(locatortype, locatorvalue); Select select = new
	 * Select(element); value = select.getFirstSelectedOption().getText();
	 * return value; } catch (Exception e) { logger.error(e); } return null; }
	 */

	/**
	 * To scroll down the browser using javascript
	 */
	public static void scrollDown() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollBy(0,450)", "");
		} catch (Exception e) {
			logger.error("Error while scrolling down");
		}
	}

	/**
	 * Clicks on OK button of Alert window
	 */
	public static void acceptAlertWindow() {
		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
			sleepThread(1);
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * This method is used to close the Alert window
	 * 
	 */
	public static void dismissAlertWindow() {
		try {
			Alert alert = driver.switchTo().alert();
			alert.dismiss();
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * @param element
	 *            To clean the text for webelement
	 */
	public static void clearText(WebElement element) {
		try {
			element.clear();
		} catch (Exception e) {
			logger.error(e);
		}

	}

	public static int getCountOfDropDownValues(WebElement element) {
		int number = 0;
		try {
			Select select = new Select(element);
			number = select.getOptions().size();
			return number;
		} catch (Exception e) {
			logger.error(e);
		}
		return number;
	}

	public static String getTitle() {
		try {
			return driver.getTitle();
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public static void navigateToBackWard() {
		driver.navigate().back();
	}

	public static boolean checkValueInDropDown(WebElement element,
			String application) {
		List<WebElement> dropdownListOptions = null;
		try {
			Select select = new Select(element);
			dropdownListOptions = select.getOptions();
			for (WebElement e : dropdownListOptions) {
				if (application.equalsIgnoreCase(e.getText())) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * This method is used to wait until the element is identified
	 * 
	 * @param locatortype
	 * @param locatorvalue
	 * @return
	 */
	public static WebElement waitForElementToIdentify(String locatortype,
			String locatorvalue) {
		WebElement element = null;
		try {
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class)
					.ignoring(StaleElementReferenceException.class,
							TimeoutException.class);
			element = wait.until(ExpectedConditions.visibilityOf(findobject(
					locatortype, locatorvalue)));
		} catch (Exception e) {
			logger.error(e);
		}
		return element;
	}

	@SuppressWarnings("deprecation")
	public static boolean isElementPresentHavingText(String locatortype,
			String locatorvalue, String text) {
		Boolean flag = false;
		try {
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class)
					.ignoring(StaleElementReferenceException.class,
							TimeoutException.class);
			if (locatortype.equalsIgnoreCase("xpath")) {
				flag = wait.until(ExpectedConditions.textToBePresentInElement(
						By.xpath(locatorvalue), text));
			} else if (locatortype.equalsIgnoreCase("id")) {
				flag = wait.until(ExpectedConditions.textToBePresentInElement(
						By.id(locatorvalue), text));
			} else if (locatortype.equalsIgnoreCase("name")) {
				flag = wait.until(ExpectedConditions.textToBePresentInElement(
						By.id(locatorvalue), text));
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}

	public static void refreshPage() {
		try {
			driver.navigate().refresh();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static int getCountFromTable(WebElement element) {
		try {
			return element.findElements(By.tagName("tr")).size();
		} catch (Exception e) {
			logger.error(e);
		}
		return 0;
	}

	public static void mouseHoverOnElement(WebElement element) {
		try {
			Actions actions = new Actions(driver);
			actions.moveToElement(element);
		} catch (Exception e) {
			logger.error(e);
		}

	}

	public static String getAttributeValue(WebElement element, String attribute) {
		try {
			return element.getAttribute(attribute);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public static void switchToDefaultWindow() {
		try {
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			logger.error(e);
		}

	}

	public static void sleepThread(int i) {
		try {
			Thread.sleep(i * 1000);
		} catch (Exception e) {
			logger.error(e);
		}

	}

	public static String getURL() {
		try {
			return driver.getCurrentUrl();
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public static void ClickAndHoldElement(WebElement someElement) {
		try {
			Actions builder = new Actions(driver);
			builder.clickAndHold(someElement).perform();

		} catch (Exception e) {
			logger.error(e);
		}

	}

	public static List<WebElement> findobjects(String locatortype,
			String locatorvalue) {
		List<WebElement> elements = null;
		try {
			if (locatortype.equalsIgnoreCase("name")) {
				elements = driver.findElements(By.name(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("link")) {
				elements = driver.findElements(By.linkText(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("id")) {
				elements = driver.findElements(By.id(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("class")) {
				elements = driver.findElements(By.className(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("cssSelector")) {
				elements = driver.findElements(By.cssSelector(locatorvalue));
			} else if (locatortype.equalsIgnoreCase("xpath")) {
				elements = driver.findElements(By.xpath(locatorvalue));
			}
			return elements;
		} catch (Exception e) {
			logger.error("Unable to identify the element with id "
					+ locatortype + " " + "and value " + locatorvalue);
		}

		return null;
	}

	public static boolean isAlertWindowDialogPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException Ex) {
			return false;
		}
	}

	public static boolean checkSearchInTable(WebElement element,
			String actualtext) {
		List<WebElement> rows = null;
		List<WebElement> cols = null;
		try {
			rows = element.findElements(By.tagName("tr"));
			for (WebElement row : rows) {
				cols = row.findElements(By.tagName("td"));
				for (WebElement col : cols) {
					if (actualtext.equalsIgnoreCase(col.getText())) {
						return true;
					}
				}

			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static int getTextFromTable(WebElement element, int row, int col) {
		int number = 0;
		try {
			number = Integer.parseInt(element.findElement(
					By.xpath(".//tr[" + row + "]/td[" + col + "]")).getText());
			return number;
		} catch (Exception e) {
			logger.error(e);
		}
		return 0;
	}

	/**
	 * @return
	 */
	public static boolean switchToChildWindow() {
		boolean flag = false;
		try {
			// get the current window handle
			String parentHandle = driver.getWindowHandle();
			// Identify number of Child windows
			Set<String> windows = driver.getWindowHandles();
			int numberOfWindows = windows.size();
			if (numberOfWindows > 1) {
				for (String popUpHandle : windows) {
					if (!popUpHandle.equals(parentHandle)) {
						driver.switchTo().window(popUpHandle);
						driver.close();
						flag = true;
					}
				}
			} else {
				logger.error("No Child windows are present");
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}

	public static boolean selectDropdownByTextFromList(WebElement element,
			String value) {
		List<WebElement> numberOfLists = null;
		try {
			numberOfLists = element.findElements(By.tagName("li"));
			for (WebElement list : numberOfLists) {
				if (value.equalsIgnoreCase(list.getText())) {
					list.click();
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean PressEnterKey(WebElement element) {
		try {
			element.sendKeys(Keys.ENTER);
			return true;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public static boolean clickOnElement(WebElement element) {
		try {
			if (element != null) {
				element.click();
				return true;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	/**
	 * 
	 */
	public static boolean typeKeys(WebElement element, Keys key) {
		boolean flag = false;
		try {			
			element.sendKeys(key);
			flag = true;
			return flag;
		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}

	public static void createZone(WebElement element, int xoffset, int yoffset) {
		try {
			element.click();
			Actions actions = new Actions(driver);
			actions.moveByOffset(xoffset, yoffset);
			actions.clickAndHold().moveByOffset(50, 50).release().perform();
		} catch (Exception e) {
			logger.error("x, y are required");
		}

	}

	public static void createPOI(WebElement element, int xoffset, int yoffset) {
		try {
			element.click();
			Actions actions = new Actions(driver);
			actions.moveByOffset(xoffset, yoffset);
			actions.clickAndHold().release().perform();
		} catch (Exception e) {
			logger.error("Unable to create Point Of Interest with x " + xoffset
					+ " Y " + yoffset + " coordinates");
		}
	}

	public static void createPointOfInterest(int xoffset, int yoffset) {
		try {
			Actions actions = new Actions(driver);
			actions.moveByOffset(xoffset, yoffset);
			actions.clickAndHold().release().perform();
		} catch (Exception e) {
			logger.error("Unable to create Point Of Interest with x " + xoffset
					+ " Y " + yoffset + " coordinates");
		}

	}

	public static void createWayPoint(WebElement element, int xoffset,
			int yoffset) {
		try {
			element.click();
			Actions actions = new Actions(driver);
			actions.moveByOffset(xoffset, yoffset);
			actions.clickAndHold().release().perform();
		} catch (Exception e) {
			logger.error("x, y are required");
		}
	}

	public static void clickOnOffset(int xOffset, int yOffset) {

		try {

			Actions actions = new Actions(driver);
			actions.moveByOffset(xOffset, yOffset);
			actions.clickAndHold().release().perform();

		} catch (Exception e) {
			logger.error("x, y are required");
		}

	}

	public static void routes(WebElement element) {
		try {
			Actions actions = new Actions(driver);
			actions.moveToElement(element).click().perform();
		} catch (Exception e) {
			logger.error("no such route found");
		}
	}

	public static void deleteCreatedZone(int xoffset, int yoffset) {
		try {
			Actions actions = new Actions(driver);
			actions.moveByOffset(xoffset, yoffset).click().release().perform();
		} catch (Exception e) {
			logger.error("x, y are required");
		}
	}

	// Alert handling for Safari browser (Click Ok)
	public static void acceptAlertWindowInSafariBrowser() {
		try {
			((JavascriptExecutor) driver)
					.executeScript("window.confirm = function(msg){return true;};");
		} catch (Exception e) {
			logger.error(e);
		}
	}

	// Navigate to specific url
	public static void navigateToUrl(String url) {
		try {
			driver.navigate().to(url);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static void closeCurrentWindow() {
		try {
			driver.get("");
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * This method is used to get the current window handle ID
	 * 
	 * @return
	 */
	public static String getWindowHandle() {
		String window = null;
		try {
			window = driver.getWindowHandle();
		} catch (Exception e) {
			logger.error(e);
		}
		return window;
	}

	/**
	 * This method is used to switch the driver to window having given window
	 * handle ID
	 * 
	 * @param windowHandle
	 */
	public static void switchToWindow(String windowHandle) {
		try {
			driver.switchTo().window(windowHandle);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * This method is used to highlight the given element
	 * 
	 * @param element
	 */
	public static void highlight(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (int i = 0; i <= 5; i++) {
			js.executeScript(
					"arguments[0].setAttribute('style', arguments[1]);",
					element, "color: red; border: 5px solid red;");
			js.executeScript(
					"arguments[0].setAttribute('style', arguments[1]);",
					element, "");
		}
	}

	public static void moveToPoint(WebElement element, int xoffset, int yoffset) {
		try {
			element.click();
			Actions actions = new Actions(driver);
			actions.clickAndHold(element);
			actions.moveByOffset(xoffset, yoffset).build().perform();
		} catch (Exception e) {
			logger.error("x, y are required");
		}

	}

	public static void moveToPoint_chrome(WebElement element, int xoffset,
			int yoffset) {
		try {
			element.click();
			Actions actions = new Actions(driver);
			actions.clickAndHold(element);
			actions.moveByOffset(xoffset, yoffset).build().perform();
			SeleniumUtils.sleepThread(2);
			actions.moveByOffset(xoffset, yoffset).release().perform();
			// actions.release();

		} catch (Exception e) {
			logger.error("x, y are required");
		}

	}

	/**
	 * Identify new child window if available then navigate to child window
	 */
	public static void identifyAndNavigateNewWindow() {
		try {
			Set<String> childWindows = driver.getWindowHandles();
			if (childWindows.size() == 1) {
				logger.error("No new window is available");
				return;
			} else if (childWindows.size() == 0) {
				logger.error("No windows are available");
				return;
			} else {
				String currentWindow = driver.getWindowHandle();
				for (String window : childWindows) {
					if (!currentWindow.equalsIgnoreCase(window)) {
						driver.switchTo().window(window);
						return;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * @param element
	 */
	public static void switchToFrame(WebElement element) {
		try {
			driver.switchTo().frame(element);
		} catch (Exception e) {
			logger.error("Unable to switch to Frame");
		}
	}
	
	/**
	 * @param element
	 * This method is used to perform Ctrl+A operation on element
	 */
	public static void selectTextUsingKeys(WebElement element){		
		try{
			element.sendKeys(Keys.chord(Keys.CONTROL,"a"));					
		}catch(Exception e){
			logger.error("Unable to perform select operation on given text");
		}
		
	}

}

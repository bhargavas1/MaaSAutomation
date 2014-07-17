package com.phunware.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;
import com.phunware.constants.GlobalConstants;
import com.phunware.jaxb.entity.Testcase;
import com.phunware.jaxb.entity.Testcase.Case;
import com.phunware.jaxb.entity.Testcase.Case.Param;
import com.phunware.util.JaxbUtil;
import com.phunware.util.ReportUtils;
import com.phunware.util.SeleniumUtils;
import com.phunware.util.SoftAssert;

@Listeners({ ATUReportsListener.class, ConfigurationListener.class,
		MethodListener.class })
public class Location extends Suite {
	private static Logger logger = Logger.getLogger(Location.class);
	private static List<String> testcaseList = new ArrayList<String>();
	private static boolean isTextMatching;
	private static WebElement element = null;
	private static String childSuite = "location";
	private static boolean suiteExecution = false;
	private static Map<String, String> testcaseArgs = new HashMap<String, String>();
	private Testcase locationSuite;
	private List<Case> testcases = null;
	private boolean isClicked;
	private SoftAssert m_assert;

	@BeforeClass
	public void setUp() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Index Page Description for Results
		ReportUtils.setIndexPageDescription();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check the Test suite is added for execution
		for (String testSuite : scenarioslist) {
			if (childSuite.equalsIgnoreCase(testSuite)) {
				suiteExecution = true;
				break;
			}
		}
		// if suiteExecution is false then skip the location test suite
		if (!suiteExecution) {
			logger.info("Test suite [Location] is not added for execution");
			ReportUtils.setStepDescription(
					"Test suite [Location] is not added for execution", false);
			throw new SkipException(
					"Test suite [Location] is not added for execution");
		}
		logger.info("reading [Location] Input file");
		// reading Advertising input file
		locationSuite = (Testcase) JaxbUtil.unMarshal(
				GlobalConstants.INPUT_XML_PATH + GlobalConstants.LOCATION_FILE,
				Testcase.class);
		if (locationSuite != null) {
			// Add the test cases into testcaseList
			testcases = locationSuite.getCase();
			for (Case testcase : testcases) {
				String runMode = testcase.getRunmode();
				if ("Y".equalsIgnoreCase(runMode)) {
					testcaseList.add(testcase.getName());
				}
			}
		}
		// If testcaseList size is zero skip the execution
		if (testcaseList.size() == 0) {
			logger.warn("No testCase added for execution in [Location] test suite");
			ReportUtils.setStepDescription(
					"No TestCase added for execution in [Location] suite",
					false);
			throw new SkipException(
					"No testCases added for execution in [Location] test suite");
		}
		logger.info("reading [Location] Input file successful");
		ReportUtils.setStepDescription(
				"reading [Location] input file successful", false);
		logger.info(" {" + testcaseList + "} for execution in [Location] suite");
		m_assert.assertAll();
	}

	/**
	 * This method login into the application as per the input
	 */
	@Test(priority = 0)
	public void loginAs() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check loginAs Test case is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("loginAs")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [loginAs] is not added for execution in [PhunwareClients]");
			ReportUtils
					.setStepDescription(
							"Test case [loginAs] is not added for execution in [PhunwareClients]",
							false);
			throw new SkipException(
					"Test case [loginAs] is not added for execution in [PhunwareClients]");
		}
		// read the params data
		testcaseArgs = getTestData("loginAs");
		// Opening the browser
		logger.info("opening [" + configproperties.get(0) + "] browser");
		boolean isOpened = SeleniumUtils.launchBrowser();
		if (!isOpened) {
			logger.error("Error while launching " + configproperties.get(0)
					+ " browser");
			ReportUtils.setStepDescription("Error while launching "
					+ configproperties.get(0) + " browser", true);
			m_assert.assertTrue(isOpened, "Error while launching "
					+ configproperties.get(0) + " browser");
		}
		// Identify the username and password fields
		logger.info("Identify Username, Password & Signin buttons");
		WebElement userelement = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("UserNameTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("UserNameTextbox")
						.getLocatorvalue());
		WebElement passwordelement = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("PasswordTextbox")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("PasswordTextbox")
						.getLocatorvalue());
		WebElement buttonelement = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("SigninButton")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("SigninButton")
								.getLocatorvalue());
		// If username and password empty then throw exception...
		if (userelement == null || passwordelement == null
				|| buttonelement == null) {
			logger.error("Unable to identify username or password or signin elements");
			ReportUtils
					.setStepDescription(
							"Unable to identify username or password or signin elements",
							true);
			m_assert.fail("Unable to identify username or password or signin elements");
		}
		// Login into the application with username and password
		logger.info("login into the application");
		SeleniumUtils.login(userelement, passwordelement, buttonelement,
				testcaseArgs.get("username"), testcaseArgs.get("password"));
		logger.info("Verify the landing page after login");
		SeleniumUtils.sleepThread(5);
		// Identify Landing page header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
						.getLocatorvalue());
		// If Landing page header element is null then throw the error and exit
		if (element == null) {
			logger.error("Unable to identify [Initial Page] Header Element");
			ReportUtils.setStepDescription(
					"Unable to identify [Initial Page] Header Element", true);
			m_assert.fail("Unable to identify [Initial Page] Header Element");
		}
		// Get the text of the header element
		String InitialHeaderText = SeleniumUtils.getText(element);
		// Get the Expected text
		String ExpInitialHeaderText = Suite.objectRepositoryMap.get(
				"ClientsApplicationsHeader").getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(InitialHeaderText,
				ExpInitialHeaderText);
		if (isTextMatching) {
			logger.info("User is on [Your Applications] page");
			// Identify Company name
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientTabOrganizationName")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientTabOrganizationName")
							.getLocatorvalue());
			// Get the text
			String organizationName = SeleniumUtils.getText(element);
			isTextMatching = SeleniumUtils.assertEqual(organizationName,
					testcaseArgs.get("organization"));
			if (!isTextMatching) {
				logger.info("User is landed on [Your Applications] page of "
						+ organizationName + " organization");
				logger.info("Navigate to " + testcaseArgs.get("organization")
						+ " organization");
				// Identify Switch element
				element = SeleniumUtils.findobject(Suite.objectRepositoryMap
						.get("SwitchBtn").getLocatortype(),
						Suite.objectRepositoryMap.get("SwitchBtn")
								.getLocatorvalue());
				// If Switch element is null then throw the error and exit
				if (element == null) {
					logger.error("Unable to identify [Switch Button]");
					ReportUtils.setStepDescription(
							"Unable to identify [Switch Button]", true);
					m_assert.fail("Unable to identify [Switch Button]");
				}
				// Click on Switch button
				logger.info("Click on [Switch Button]");
				isClicked = SeleniumUtils.clickOnElement(element);
				if (!isClicked) {
					logger.error("Unable to click on [Switch Button]");
					ReportUtils.setStepDescription(
							"Unable to click on [Switch Button]", true);
					m_assert.fail("Unable to click on [Switch Button]");
				}
				logger.info("Click operation on [Switch Button] is successful");
				// Enter organization name in text box
				logger.info("Identify Switch button - Organization text box");
				SeleniumUtils.sleepThread(1);
				element = SeleniumUtils
						.findobject(
								Suite.objectRepositoryMap.get(
										"SwitcBtnOrganizationTextbox")
										.getLocatortype(),
								Suite.objectRepositoryMap.get(
										"SwitcBtnOrganizationTextbox")
										.getLocatorvalue());
				if (element == null) {
					logger.error("Switch button Organization textbox is not present. "
							+ "So unable to move to ["
							+ testcaseArgs.get("organization")
							+ "] Organization");
					ReportUtils.setStepDescription(
							"Switch button Organization textbox is not present. "
									+ "So unable to move to ["
									+ testcaseArgs.get("organization")
									+ "] Organization", true);
					m_assert.fail("Switch button Organization textbox is not present. "
							+ "So unable to move to ["
							+ testcaseArgs.get("organization")
							+ "] Organization");
				}
				logger.info("Enter the Organization ["
						+ testcaseArgs.get("organization")
						+ "] name in the Switch btn Organization text box");
				// Enter the organization name in text box
				SeleniumUtils.type(element, testcaseArgs.get("organization"));
				SeleniumUtils.sleepThread(3);
				// Switch to organization as per the browser
				if (configproperties.get(0).equalsIgnoreCase("FIREFOX")) {
					// Identify the Organization dropdown
					WebElement orgListbox = SeleniumUtils.findobject(
							Suite.objectRepositoryMap.get(
									"SwitcBtnOrganizationDropdown")
									.getLocatortype(),
							Suite.objectRepositoryMap.get(
									"SwitcBtnOrganizationDropdown")
									.getLocatorvalue());
					// If Organization dropdown is null then throw the error and
					// exit
					if (orgListbox == null) {
						logger.error("Switch button Organization dropdown is not present. "
								+ "So unable to move to Organization");
						ReportUtils.setStepDescription(
								"Switch button Organization dropdown is not present. "
										+ "So unable to move to Organization",
								false);
						m_assert.fail("Switch button Organization dropdown is not present. "
								+ "So unable to move to Organization");
					}
					// Click on Organization dropdown
					isClicked = SeleniumUtils.clickOnElement(orgListbox);
					if (!isClicked) {
						logger.error("Unable to click on organization in a list");
						ReportUtils.setStepDescription(
								"Unable to click on organization in a list",
								true);
						m_assert.fail("Unable to click on organization in a list");

					}
				} else if (configproperties.get(0).equalsIgnoreCase("CHROME")
						|| configproperties.get(0).equalsIgnoreCase("SAFARI")
						|| configproperties.get(0).equalsIgnoreCase("IE")) {
					element.sendKeys(Keys.ENTER);
					SeleniumUtils.sleepThread(4);
				}
				// Verify the landing Page
				logger.info("Verify if user is landed on [Your Applications] page");
				element = SeleniumUtils.waitForElementToIdentify(
						Suite.objectRepositoryMap.get(
								"ClientsApplicationsHeader").getLocatortype(),
						Suite.objectRepositoryMap.get(
								"ClientsApplicationsHeader").getLocatorvalue());
				if (element == null) {
					logger.error("Login failed : Unabel to identify [Your Applications]"
							+ " header element");
					ReportUtils.setStepDescription(
							"Login failed : Unabel to identify [Your Applications]"
									+ " header element", true);
					m_assert.fail("Login failed : Unabel to identify [Your Applications]"
							+ " header element");
				}
				String landingPageText = SeleniumUtils.getText(element);
				// Get the actual text
				String ExpLandingPageText = Suite.objectRepositoryMap.get(
						"ClientsApplicationsHeader").getExptext();
				// Compare the both texts
				isTextMatching = SeleniumUtils.assertEqual(landingPageText,
						ExpLandingPageText);
				if (!isTextMatching) {
					logger.error("Login failed. User is not landed on [Your Applications] page");
					ReportUtils
							.setStepDescription(
									"Login failed. User is not landed on [Your Applications] page",
									true);
					m_assert.fail("Login failed. User is not landed on [Your Applications] page");
				}
			}
		} else {
			logger.info("User is landed on [" + InitialHeaderText + "] page");
			logger.info("Navigate to [" + ExpInitialHeaderText + "] page");
			logger.info("Verify if User has Switch button ");
			// Identify Switch element
			element = SeleniumUtils
					.findobject(Suite.objectRepositoryMap.get("SwitchBtn")
							.getLocatortype(),
							Suite.objectRepositoryMap.get("SwitchBtn")
									.getLocatorvalue());
			// If Switch element is null then throw the error and exit
			if (element == null) {
				logger.error("Unable to identify [Switch Button]");
				ReportUtils.setStepDescription(
						"Unable to identify [Switch Button]", true);
				m_assert.fail("Unable to identify [Switch Button]");
			}
			// Click on Switch button
			logger.info("Click on [Switch Button]");
			isClicked = SeleniumUtils.clickOnElement(element);
			if (!isClicked) {
				logger.error("Unable to click on [Switch Button]");
				ReportUtils.setStepDescription(
						"Unable to click on [Switch Button]", true);
				m_assert.fail("Unable to click on [Switch Button]");
			}
			logger.info("Click operation on [Switch Button] is successful");
			// Enter organization name in text box
			logger.info("Identify Switch button - Organization text box");
			SeleniumUtils.sleepThread(1);
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap
							.get("SwitcBtnOrganizationTextbox")
							.getLocatortype(),
					Suite.objectRepositoryMap
							.get("SwitcBtnOrganizationTextbox")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Switch button Organization textbox is not present. "
						+ "So unable to move to ["
						+ testcaseArgs.get("organization") + "] Organization");
				ReportUtils.setStepDescription(
						"Switch button Organization textbox is not present. "
								+ "So unable to move to ["
								+ testcaseArgs.get("organization")
								+ "] Organization", true);
				m_assert.fail("Switch button Organization textbox is not present. "
						+ "So unable to move to ["
						+ testcaseArgs.get("organization") + "] Organization");
			}
			logger.info("Enter the Organization ["
					+ testcaseArgs.get("organization")
					+ "] name in the Switch btn Organization text box");
			// Enter the organization name in text box
			SeleniumUtils.type(element, testcaseArgs.get("organization"));
			SeleniumUtils.sleepThread(3);
			// Switch to organization as per the browser
			if (configproperties.get(0).equalsIgnoreCase("FIREFOX")) {
				// Identify the Organization dropdown
				WebElement orgListbox = SeleniumUtils.findobject(
						Suite.objectRepositoryMap.get(
								"SwitcBtnOrganizationDropdown")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("SwitcBtnOrganizationDropdown")
								.getLocatorvalue());
				// If Organization dropdown is null then throw the error and
				// exit
				if (orgListbox == null) {
					logger.error("Switch button Organization dropdown is not present. "
							+ "So unable to move to Organization");
					ReportUtils.setStepDescription(
							"Switch button Organization dropdown is not present. "
									+ "So unable to move to Organization",
							false);
					m_assert.fail("Switch button Organization dropdown is not present. "
							+ "So unable to move to Organization");
				}
				// Click on Organization dropdown
				isClicked = SeleniumUtils.clickOnElement(orgListbox);
				if (!isClicked) {
					logger.error("Unable to select on organization in a list");
					ReportUtils.setStepDescription(
							"Unable to select on organization in a list", true);
					m_assert.fail("Unable to select on organization in a list");

				}
			} else if (configproperties.get(0).equalsIgnoreCase("CHROME")
					|| configproperties.get(0).equalsIgnoreCase("SAFARI")
					|| configproperties.get(0).equalsIgnoreCase("IE")) {
				element.sendKeys(Keys.ENTER);
				SeleniumUtils.sleepThread(4);
			}
			// Verify the landing Page
			logger.info("Verify if user is landed on [Your Applications] page");
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("ClientsApplicationsHeader")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Login failed : Unabel to identify [Your Applications]"
						+ " header element");
				ReportUtils.setStepDescription(
						"Login failed : Unabel to identify [Your Applications]"
								+ " header element", true);
				m_assert.fail("Login failed : Unabel to identify [Your Applications]"
						+ " header element");
			}
			String landingPageText = SeleniumUtils.getText(element);
			// Get the actual text
			String ExpLandingPageText = Suite.objectRepositoryMap.get(
					"ClientsApplicationsHeader").getExptext();
			// Compare the both texts
			isTextMatching = SeleniumUtils.assertEqual(landingPageText,
					ExpLandingPageText);
			if (!isTextMatching) {
				logger.error("Login failed. User is not landed on "
						+ "[Your Applications] page");
				ReportUtils.setStepDescription(
						"Login failed. User is not landed on "
								+ "[Your Applications] page", true);
				m_assert.fail("Login failed. User is not landed on "
						+ "[Your Applications] page");
			}
		}
		m_assert.assertAll();
	}

	@Test(priority = 1, dependsOnMethods = "loginAs")
	public void clickAndVerifyLocationTab() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();		
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("clickAndVerifyLocationTab")) {
				forExecution = true;
				break;
			}
		}
		// if not added then skip the test case
		if (!forExecution) {
			logger.info("Test case [clickAndVerifyLocationTab] is not "
					+ "added for execution");
			ReportUtils.setStepDescription(
					"Test case [clickAndVerifyLocationTab] is not "
							+ "added for execution", false);
			throw new SkipException(
					"Test case [clickAndVerifyLocationTab] is not "
							+ "added for execution");
		}
		logger.info("Starting [clickAndVerifyLocationTab] execution");
		logger.info("Verify if [LOCATION] tab is present");
		// Identify Location tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationTab").getLocatortype(),
				Suite.objectRepositoryMap.get("LocationTab").getLocatorvalue());
		// Get the text of the LOCATION tab
		String LocationTabText = SeleniumUtils.getText(element);
		// Get the expected text
		String ExpLocationTabText = Suite.objectRepositoryMap
				.get("LocationTab").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(LocationTabText,
				ExpLocationTabText);
		if (!isTextMatching) {
			logger.error("[LOCATION] tab text matching failed."
					+ " The Expected text is [" + ExpLocationTabText
					+ "] and the return text is [" + LocationTabText + "]");
			ReportUtils.setStepDescription(
					"[LOCATION] tab text matching failed", "",
					ExpLocationTabText, LocationTabText, true);
			m_assert.fail("[LOCATION] tab text matching failed."
					+ " The Expected text is [" + ExpLocationTabText
					+ "] and the return text is [" + LocationTabText + "]");
		}
		logger.info("[LOCATION] tab is present");
		logger.info("Navigate to [LOCATION] tab");
		// Click on lOCATION tab
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on [LOCATION] tab");
			ReportUtils.setStepDescription("Unable to click on [LOCATION] tab",
					true);
			m_assert.fail("Unable to click on [LOCATION] tab");
		}
		// Verify the landing Page
		SeleniumUtils.sleepThread(4);
		logger.info("Verify if user is landed on [LOCATION] page");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("LocationHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationHeader")
						.getLocatorvalue());
		// Get the text of the header element
		String ConfigureTabHeaderElement = SeleniumUtils.getText(element);
		// Get the expected text
		String ExpConfigureTabHeaderElement = Suite.objectRepositoryMap.get(
				"LocationHeader").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(ConfigureTabHeaderElement,
				ExpConfigureTabHeaderElement);
		if (!isTextMatching) {
			logger.error("[Configure MSE] page header text matching failed:"
					+ " The expected text is [" + ExpConfigureTabHeaderElement
					+ "] and the actual return text is ["
					+ ConfigureTabHeaderElement + "]");
			ReportUtils.setStepDescription(
					"[Analytics] tab text matching failed", "",
					ExpConfigureTabHeaderElement, ConfigureTabHeaderElement,
					true);
			m_assert.fail("[Configure MSE] page header text matching failed:"
					+ " The expected text is [" + ExpConfigureTabHeaderElement
					+ "] and the actual return text is ["
					+ ConfigureTabHeaderElement + "]");
		}
		logger.info("Click operation on [LOCATION] tab successful");
		logger.info("Test case [clickAndVerifyLocationTab] execution"
				+ " is successful");
		m_assert.assertAll();
	}

	@Test(priority = 2, dependsOnMethods = "loginAs")
	public void verifyLocationTabLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyLocationTabLayout")) {
				forExecution = true;
				break;
			}
		}
		// if not added then skip the testcase
		if (!forExecution) {
			logger.info("Test case [verifyLocationTabLayout] is not added"
					+ " for execution");
			ReportUtils.setStepDescription(
					"Test case [verifyLocationTabLayout] is not added"
							+ " for execution", false);
			throw new SkipException(
					"Test case [verifyLocationTabLayout] is not added"
							+ " for execution");
		}
		logger.info("Verify all the sub tabs present in [LOCATION] tab");
		// Identify Configure sub tab element
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationConfigure")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationConfigure")
						.getLocatorvalue());
		// Get the text of the header element
		String LocationConfigure = SeleniumUtils.getText(element);
		// Get the expected text
		String ExpLocationConfigure = Suite.objectRepositoryMap.get(
				"LocationConfigure").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(LocationConfigure,
				ExpLocationConfigure);
		if (!isTextMatching) {
			logger.error("[Configure] tab text matching failed:"
					+ " The expected text is [" + ExpLocationConfigure
					+ "] and the actual return text is [" + LocationConfigure
					+ "]");
			ReportUtils.setStepDescription(
					"[Analytics] tab text matching failed", "",
					ExpLocationConfigure, LocationConfigure, true);
			m_assert.fail("[Configure] tab text matching failed:"
					+ " The expected text is [" + ExpLocationConfigure
					+ "] and the actual return text is [" + LocationConfigure
					+ "]");
		}
		logger.info("Identification of [Configure] sub-tab is Successful");
		// Identify Map Editor Button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditor")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditor")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditor]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditor]", true);
			m_assert.fail("Unable to identify [LocationMapEditor]");
		}
		// Get the text of the New Venue
		String mapEditor = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpmapEditor = Suite.objectRepositoryMap
				.get("LocationMapEditor").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(mapEditor, ExpmapEditor);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[MapEditor] tab text matching failed:"
					+ " The expected text is [" + ExpmapEditor
					+ "] and the actual return text is [" + mapEditor + "]");
			ReportUtils.setStepDescription(
					"[LocationMapEditor] tab text matching failed", "",
					ExpmapEditor, mapEditor, true);
			m_assert.fail("[MapEditor] tab text matching failed:"
					+ " The expected text is [" + ExpmapEditor
					+ "] and the actual return text is [" + mapEditor + "]");
		}
		logger.info("Identificatio of [Map Editor] sub-tab is Successful");
		// Identify Support tab
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationSupportTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationSupportTab")
						.getLocatorvalue());
		// Get the text of the New Venue
		String LocationSupportTab = SeleniumUtils.getText(element);
		// Get the exp text
		String ExpLocationSupportTab = Suite.objectRepositoryMap.get(
				"LocationSupportTab").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(mapEditor, ExpmapEditor);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[Support] tab text matching failed:"
					+ " The expected text is [" + ExpLocationSupportTab
					+ "] and the actual return text is [" + LocationSupportTab
					+ "]");
			ReportUtils.setStepDescription(
					"[LocationSupportTab] tab text matching failed", "",
					ExpLocationSupportTab, LocationSupportTab, true);
			m_assert.fail("[Support] tab text matching failed:"
					+ " The expected text is [" + ExpLocationSupportTab
					+ "] and the actual return text is [" + LocationSupportTab
					+ "]");
		}
		logger.info("Identificatio of [Support] sub-tab is Successful");
		logger.info("Identification of all the sub tabs in [LOCATION] tab successful");
		m_assert.assertAll();
	}

	@Test(priority = 3, dependsOnMethods = "loginAs")
	public void verifyConfigureTabLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyConfigureTabLayout")) {
				forExecution = true;
				break;
			}
		}
		// if not added then skip the testcase
		if (!forExecution) {
			logger.info("Test case [verifyConfigureTabLayout] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [verifyConfigureTabLayout] is not added"
							+ " for execution", false);
			throw new SkipException(
					"Test case [verifyConfigureTabLayout] is not added for execution");
		}
		logger.info("Starting [verifyConfigureTabLayout] execution");
		logger.info("Verify if user is on [Configure] tab");
		// Identify Configure tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("LocationHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationHeader")
						.getLocatorvalue());
		// Get the text of the header element
		String ConfigureTabHeaderElement = SeleniumUtils.getText(element);
		// Get the expected text
		String ExpConfigureTabHeaderElement = Suite.objectRepositoryMap.get(
				"LocationHeader").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(ConfigureTabHeaderElement,
				ExpConfigureTabHeaderElement);
		if (!isTextMatching) {
			logger.info("User is not on [Configure MSE] page");
			logger.info("Navigate to [Configure MSE] page");
			// Identify Configure tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("LocationConfigure")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("LocationConfigure")
							.getLocatorvalue());
			// Get the text
			String ConfigureText = SeleniumUtils.getText(element);
			String ExpConfigureText = Suite.objectRepositoryMap.get(
					"LocationConfigure").getExptext();
			// Compare the return text with expected text from OR
			isTextMatching = SeleniumUtils.assertEqual(ConfigureText,
					ExpConfigureText);
			// If both texts are not same then throw the error and exit
			if (!isTextMatching) {
				logger.error("[Configure] tab text matching failed:"
						+ " The expected text is [" + ExpConfigureText
						+ "] and the actual return text is [" + ConfigureText
						+ "]");
				ReportUtils.setStepDescription(
						"[Configure] tab text matching failed", "",
						ExpConfigureText, ConfigureText, true);
				m_assert.fail("[Configure] tab text matching failed:"
						+ " The expected text is [" + ExpConfigureText
						+ "] and the actual return text is [" + ConfigureText
						+ "]");
			}
			// Click on Configure tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify Configure tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("LocationHeader")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("LocationHeader")
							.getLocatorvalue());
			// Get the text of the header element
			ConfigureTabHeaderElement = SeleniumUtils.getText(element);
			// Get the expected text
			ExpConfigureTabHeaderElement = Suite.objectRepositoryMap.get(
					"LocationHeader").getExptext();
			// Compare the text with expected text
			isTextMatching = SeleniumUtils.assertEqual(
					ConfigureTabHeaderElement, ExpConfigureTabHeaderElement);
			if (!isTextMatching) {
				logger.error("[Configure MSE] page header text matching failed:"
						+ " The expected text is ["
						+ ExpConfigureTabHeaderElement
						+ "] and the actual return text is ["
						+ ConfigureTabHeaderElement + "]");
				ReportUtils.setStepDescription(
						"[Analytics] tab text matching failed", "",
						ExpConfigureTabHeaderElement,
						ConfigureTabHeaderElement, true);
				m_assert.fail("[Configure MSE] page header text matching "
						+ "failed:" + " The expected text is ["
						+ ExpConfigureTabHeaderElement
						+ "] and the actual return text is ["
						+ ConfigureTabHeaderElement + "]");
			}
		}
		// Identify New Venue Button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationNewVenueBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationNewVenueBtn")
						.getLocatorvalue());
		// Get the text of the New Venue
		String ActTextnewVenue = SeleniumUtils.getText(element);
		String ExpTextnewVenue = Suite.objectRepositoryMap.get(
				"LocationNewVenueBtn").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(ActTextnewVenue,
				ExpTextnewVenue);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[New Venue] button in Configure tab text "
					+ "matching failed: " + "The expected text is ["
					+ ExpTextnewVenue + "] and the actual return text is ["
					+ ActTextnewVenue + "]");
			ReportUtils.setStepDescription(
					"[New Venue] button in Configure tab text matching failed",
					"", ExpTextnewVenue, ActTextnewVenue, true);
			m_assert.fail("[New Venue] button in Configure tab text "
					+ "matching failed: " + "The expected text is ["
					+ ExpTextnewVenue + "] and the actual return text is ["
					+ ActTextnewVenue + "]");
		}
		logger.info("Verify [New Venue Button]  is Successful");
		// Identify Configure Data
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ConfigureTable")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ConfigureTable")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Venue list] in Configure tab");
			ReportUtils.setStepDescription(
					"Unable to identify [Venue list] in Configure tab", true);
			m_assert.fail("Unable to identify [Venue list] in Configure tab");
		}
		logger.info("Identification  of Configure data is successful");
		m_assert.assertAll();
	}

	@Test(priority = 4, dependsOnMethods = "loginAs")
	public void verifyNewVenueTabLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyNewVenueTabLayout")) {
				forExecution = true;
				break;
			}
		}
		// if not added then skip the testcase
		if (!forExecution) {
			logger.info("Test case [verifyNewVenueTabLayout] is not added"
					+ " for execution");
			ReportUtils.setStepDescription(
					"Test case [verifyNewVenueTabLayout] is not"
							+ " added for execution", false);
			throw new SkipException(
					"Test case [verifyNewVenueTabLayout] is not added"
							+ " for execution");
		}
		logger.info("Starting [verifyNewVenueTabLayout] execution");
		// Identify Configure tab header element
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("LocationHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationHeader")
						.getLocatorvalue());
		// Get the text of the header element
		String ConfigureTabHeaderElement = SeleniumUtils.getText(element);
		// Get the expected text
		String ExpConfigureTabHeaderElement = Suite.objectRepositoryMap.get(
				"LocationHeader").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(ConfigureTabHeaderElement,
				ExpConfigureTabHeaderElement);
		if (!isTextMatching) {
			logger.info("User is not on [Configure MSE] page");
			logger.info("Navigate to [Configure MSE] page");
			// Identify Configure tab
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("LocationConfigure")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("LocationConfigure")
							.getLocatorvalue());
			// Get the text
			String ConfigureText = SeleniumUtils.getText(element);
			String ExpConfigureText = Suite.objectRepositoryMap.get(
					"LocationConfigure").getExptext();
			// Compare the return text with expected text from OR
			isTextMatching = SeleniumUtils.assertEqual(ConfigureText,
					ExpConfigureText);
			// If both texts are not same then throw the error and exit
			if (!isTextMatching) {
				logger.error("[Configure] tab text matching failed:"
						+ " The expected text is [" + ExpConfigureText
						+ "] and the actual return text is [" + ConfigureText
						+ "]");
				ReportUtils.setStepDescription(
						"[Configure] tab text matching failed", "",
						ExpConfigureText, ConfigureText, true);
				m_assert.fail("[Configure] tab text matching failed:"
						+ " The expected text is [" + ExpConfigureText
						+ "] and the actual return text is [" + ConfigureText
						+ "]");
			}
			// Click on Configure tab
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			// Identify Configure tab header element
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("LocationHeader")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("LocationHeader")
							.getLocatorvalue());
			// Get the text of the header element
			ConfigureTabHeaderElement = SeleniumUtils.getText(element);
			// Get the expected text
			ExpConfigureTabHeaderElement = Suite.objectRepositoryMap.get(
					"LocationHeader").getExptext();
			// Compare the text with expected text
			isTextMatching = SeleniumUtils.assertEqual(
					ConfigureTabHeaderElement, ExpConfigureTabHeaderElement);
			if (!isTextMatching) {
				logger.error("[Configure MSE] page header text matching failed:"
						+ " The expected text is ["
						+ ExpConfigureTabHeaderElement
						+ "] and the actual return text is ["
						+ ConfigureTabHeaderElement + "]");
				ReportUtils.setStepDescription(
						"[Analytics] tab text matching failed", "",
						ExpConfigureTabHeaderElement,
						ConfigureTabHeaderElement, true);
				m_assert.fail("[Configure MSE] page header text matching "
						+ "failed:" + " The expected text is ["
						+ ExpConfigureTabHeaderElement
						+ "] and the actual return text is ["
						+ ConfigureTabHeaderElement + "]");
			}
		}
		// Identify New Venue Button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationNewVenueBtn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationNewVenueBtn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationNewVenueBtn]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationNewVenueBtn]", true);
			m_assert.fail("Unable to identify [LocationNewVenueBtn]");
		}
		// Click on New Venue Button
		isClicked = SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		// Identify subHeader Venue Information Text
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"LocationAddVenueSubHeaderVenueInformation")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"LocationAddVenueSubHeaderVenueInformation")
						.getLocatorvalue());
		// Get the text of the newVenueSubHeader
		String newVenueSubHeader1 = SeleniumUtils.getText(element);
		String ExpTextSubHeader1 = Suite.objectRepositoryMap.get(
				"LocationAddVenueSubHeaderVenueInformation").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(newVenueSubHeader1,
				ExpTextSubHeader1);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[Add New Venue] page header text matching failed:"
					+ " The expected text is [" + ExpTextSubHeader1
					+ "] and the actual return text is [" + newVenueSubHeader1
					+ "]");
			ReportUtils.setStepDescription(
					"[Add New Venue] page header text matching failed", "",
					ExpTextSubHeader1, newVenueSubHeader1, true);
			m_assert.fail("[Add New Venue] page header text matching failed:"
					+ " The expected text is [" + ExpTextSubHeader1
					+ "] and the actual return text is [" + newVenueSubHeader1
					+ "]");
		}

		// Identify Venue Name Text Box
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationAddVenueNameInput")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueNameInput")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenueNameInput]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationAddVenueNameInput]", true);
			m_assert.fail("Unable to identify [LocationAddVenueNameInput]");
		}
		// Identify subHeader Perimeter NotificationsText
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"LocationAddVenueSubHeaderPerimeterNotification")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"LocationAddVenueSubHeaderPerimeterNotification")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenueSubHeaderPerimeterNotification]");
			ReportUtils
					.setStepDescription(
							"Unable to identify [LocationAddVenueSubHeaderPerimeterNotification]",
							true);
			m_assert.fail("Unable to identify [LocationAddVenueSubHeaderPerimeterNotification]");
		}
		// Get the text of the Perimeter Notification Text tab
		String newVenueSubHeader2 = SeleniumUtils.getText(element);
		String ExpTextSubHeader2 = Suite.objectRepositoryMap.get(
				"LocationAddVenueSubHeaderPerimeterNotification").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(newVenueSubHeader2,
				ExpTextSubHeader2);

		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[LocationAddVenueSubHeaderPerimeterNotification]  text matching failed: The expected text is ["
					+ ExpTextSubHeader2
					+ "] and the actual return text is ["
					+ newVenueSubHeader2 + "]");
			ReportUtils
					.setStepDescription(
							"[LocationAddVenueSubHeaderPerimeterNotification] tab text matching failed",
							"", ExpTextSubHeader2, newVenueSubHeader2, true);
			m_assert.assertEquals(ExpTextSubHeader2, newVenueSubHeader2);
		}
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationAddVenueHours")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueHours")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenueHours]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationAddVenueHours]", true);
			m_assert.fail("Unable to identify [LocationAddVenueHours]");
		}
		logger.info("Verify [cool Down Hours] Drop Down is Successful");
		// verify cool Down Minutes Drop Down
		// Identify cool Down Minutes Drop Down
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationAddVenueHoursMin")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueHoursMin")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenueHoursMin]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationAddVenueHoursMin]", true);
			m_assert.fail("Unable to identify [LocationAddVenueHoursMin]");
		}
		logger.info("Verify [cool Down Minutes] Drop Down is Successful");

		// verify location add venue check box pane
		// Identify location add venue check box pane
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationAddVenueCheckBoxPane")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueCheckBoxPane")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenueCheckBoxPane]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationAddVenueCheckBoxPane]", true);
			m_assert.fail("Unable to identify [LocationAddVenueCheckBoxPane]");
		}
		logger.info("Verify [location add venue]check box pane is Successful");
		// verify subHeader Application Availability Text
		// Identify subHeader Application Availability Text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"LocationAddVenueSubHeaderApplicationAvailability")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"LocationAddVenueSubHeaderApplicationAvailability")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenueSubHeaderApplicationAvailability]");
			ReportUtils
					.setStepDescription(
							"Unable to identify [LocationAddVenueSubHeaderApplicationAvailability]",
							true);
			m_assert.fail("Unable to identify [LocationAddVenueSubHeaderApplicationAvailability]");
		}
		// Get the text of the Application Availability Text tab
		String newVenueSubHeader3 = SeleniumUtils.getText(element);
		String ExpnewVenueSubHeader3 = Suite.objectRepositoryMap.get(
				"LocationAddVenueSubHeaderApplicationAvailability")
				.getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(newVenueSubHeader3,
				ExpnewVenueSubHeader3);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[LocationAddVenueSubHeaderApplicationAvailability]  text matching failed: The expected text is ["
					+ ExpnewVenueSubHeader3
					+ "] and the actual return text is ["
					+ newVenueSubHeader3
					+ "]");
			ReportUtils
					.setStepDescription(
							"[LocationAddVenueSubHeaderApplicationAvailability] tab text matching failed",
							"", ExpTextSubHeader2, newVenueSubHeader3, true);
			m_assert.assertEquals(ExpnewVenueSubHeader3, newVenueSubHeader3);
		}
		logger.info("Verify [Application Availability] Text is Successful");
		// verify cancel button is present or not
		// Identify cancel button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationAddVenueCancel")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueCancel")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenueCancel]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationAddVenueCancel]", true);
			m_assert.fail("Unable to identify [LocationAddVenueCancel]");
		}
		// Get the text of the cancel button tab
		String newVenueCancel = SeleniumUtils.getText(element);
		String ExpnewVenueCancel = Suite.objectRepositoryMap.get(
				"LocationAddVenueCancel").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(newVenueCancel,
				ExpnewVenueCancel);

		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[LocationAddVenueCancel]  text matching failed: The expected text is ["
					+ ExpnewVenueCancel
					+ "] and the actual return text is ["
					+ newVenueCancel + "]");
			ReportUtils.setStepDescription(
					"[LocationAddVenueCancel] tab text matching failed", "",
					ExpnewVenueCancel, newVenueCancel, true);
			m_assert.assertEquals(ExpnewVenueCancel, newVenueCancel);
		}
		logger.info("Verify [Cancel Button] Text is Successful");
		// verify Save button is present or not
		// Identify Save button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationAddVenueSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueSave")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenueSave]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationAddVenueSave]", true);
			m_assert.fail("Unable to identify [LocationAddVenueSave]");
		}
		// Get the text of the Application Availability Text tab
		String newVenueSave = SeleniumUtils.getText(element);
		String ExpnewVenueSave = Suite.objectRepositoryMap.get(
				"LocationAddVenueSave").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(newVenueSave,
				ExpnewVenueSave);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[LocationAddVenueSave]  text matching failed:"
					+ " The expected text is [" + newVenueSave
					+ "] and the actual return text is [" + ExpnewVenueSave
					+ "]");
			ReportUtils.setStepDescription(
					"[LocationAddVenueSave] tab text matching failed", "",
					ExpnewVenueSave, newVenueSave, true);
			m_assert.assertEquals(ExpnewVenueSave, newVenueSave);
		}
		logger.info("Verify [Save Button] Text is Successful");
		logger.info("Test case [verifyNewVenueLayout] is successful");
		ReportUtils.setStepDescription(
				"Test case [verifyNewVenueLayout] is successful", false);
		m_assert.assertAll();
	}

	@Test(priority = 5, dependsOnMethods = "loginAs")
	public void validatingAddingNewVenue() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("validatingAddingNewVenue")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [validatingAddingNewVenue] is not added for execution in [Location]");
			ReportUtils
					.setStepDescription(
							"Test case [validatingAddingNewVenue] is not added for execution in [Location]",
							false);
			throw new SkipException(
					"Test case [validatingAddingNewVenue] is not added for execution in [Location]");
		}
		// Click On Save Button without giving any input and check whether it is
		// in the same page or not
		SeleniumUtils.click(
				Suite.objectRepositoryMap.get("LocationAddVenueSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueSave")
						.getLocatorvalue());
		// check the header of the page
		// Identify Header Text
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationAddVenuesHeader")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenuesHeader")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationAddVenuesHeader]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationAddVenuesHeader]", true);
			m_assert.fail("Unable to identify [LocationAddVenuesHeader]");
		}
		String ValidatingHeader = SeleniumUtils.getText(element);
		String ExpnewValidatingHeader = Suite.objectRepositoryMap.get(
				"LocationAddVenuesHeader").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(ValidatingHeader,
				ExpnewValidatingHeader);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[LocationAddVenuesHeader]  text matching failed: The expected text is ["
					+ ValidatingHeader
					+ "] and the actual return text is ["
					+ ExpnewValidatingHeader + "]");
			ReportUtils.setStepDescription(
					"[LocationAddVenuesHeader] tab text matching failed", "",
					ExpnewValidatingHeader, ValidatingHeader, true);
			m_assert.assertEquals(ExpnewValidatingHeader, ValidatingHeader);
		}
		m_assert.assertAll();
	}

	@Test(priority = 6, dependsOnMethods = "loginAs")
	public void createNewVenue() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		boolean isSelected = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("createNewVenue")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.warn("Test case [createNewVenue] is not added for execution in [Location]");
			ReportUtils
					.setStepDescription(
							"Test case [createNewVenue] is not added for execution in [Location]",
							false);
			throw new SkipException(
					"Test case [createNewVenue] is not added for execution in [Location]");
		}
		testcaseArgs = getTestData("createNewVenue");
		WebElement venuename = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationAddVenueNameInput")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueNameInput")
						.getLocatorvalue());
		/*
		 * WebElement onentry =
		 * SeleniumUtils.findobject(Suite.objectRepositoryMap
		 * .get("LocationAddVenueOnEntry").getLocatortype(),
		 * Suite.objectRepositoryMap.get("LocationAddVenueOnEntry")
		 * .getLocatorvalue());
		 */
		SeleniumUtils.type(venuename, testcaseArgs.get("venueName"));
		logger.info("inserted name successfully");
		/*
		 * SeleniumUtils.type(onentry, testcaseArgs.get("onEntry"));
		 * logger.info("inserted on entry succesfully");
		 */
		logger.info("Selecting hours and minutes from newvenue");
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("LocationAddVenueHours")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueHours")
						.getLocatorvalue(), testcaseArgs.get("hours"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("hours")
					+ "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("hours")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("hours")
					+ "] from dropdown");
		}
		SeleniumUtils.sleepThread(4);
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("LocationAddVenueHoursMin")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueHoursMin")
						.getLocatorvalue(), testcaseArgs.get("minutes"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("minutes")
					+ "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("minutes")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("calandar")
					+ "] from dropdown");
		}
		logger.info("Selection of hours and minutes successful");

		// selecting the checkboxes and saving the new venue
		// Click on checkboxes 1
		SeleniumUtils.click(
				Suite.objectRepositoryMap.get("LocationAddVenueCheckBox1")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueCheckBox1")
						.getLocatorvalue());
		// Click on checkboxes 2
		SeleniumUtils.click(
				Suite.objectRepositoryMap.get("LocationAddVenueCheckBox2")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueCheckBox2")
						.getLocatorvalue());

		// Click on Save Button
		SeleniumUtils.click(
				Suite.objectRepositoryMap.get("LocationAddVenueSave")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationAddVenueSave")
						.getLocatorvalue());

		ReportUtils.setStepDescription("Selected  first checkbox succesfully",
				"Alerts Sample (ios) ",
				Suite.objectRepositoryMap.get("LocationAddVenueCheckBox1")
						.getExptext(), false);
		ReportUtils.setStepDescription("Selected  first checkbox succesfully",
				"Alerts Sample (Stage) (android) ", Suite.objectRepositoryMap
						.get("LocationAddVenueCheckBox2").getExptext(), false);
		ReportUtils.setStepDescription("clicked on save button succesfully",
				false);
		m_assert.assertAll();
	}

	@Test(priority = 7, dependsOnMethods = "loginAs")
	public void verifyMappingLayout() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyMappingLayout")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [verifyMappingLayout] is not added for execution in [Location]");
			ReportUtils
					.setStepDescription(
							"Test case [verifyMappingLayout] is not added for execution in [Location]",
							false);
			throw new SkipException(
					"Test case [verifyMappingLayout] is not added for execution in [Location]");
		}
		// verify Map Editor Button
		// Identify Map Editor Button
		SeleniumUtils.sleepThread(2);
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditor")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditor")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditor]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditor]", true);
			m_assert.fail("Unable to identify [LocationMapEditor]");
		}
		// Get the text of the New Venue
		String mapEditor = SeleniumUtils.getText(element);
		String ExpmapEditor = Suite.objectRepositoryMap
				.get("LocationMapEditor").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(mapEditor, ExpmapEditor);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[LocationMapEditor]  text matching failed: The expected text is ["
					+ mapEditor
					+ "] and the actual return text is ["
					+ ExpmapEditor + "]");
			ReportUtils.setStepDescription(
					"[LocationMapEditor] tab text matching failed", "",
					mapEditor, ExpmapEditor, true);
			m_assert.assertEquals(mapEditor, ExpmapEditor);
		}
		logger.info("Verify [Map Editor] is Successful");
		// Click on Map Editor Button
		SeleniumUtils.click(Suite.objectRepositoryMap.get("LocationMapEditor")
				.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditor")
						.getLocatorvalue());
		SeleniumUtils.sleepThread(5);
		// verify Venue Selection DD is present or not
		// Identify Venue Select DD
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorVenueSelectDD")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorVenueSelectDD")
						.getLocatorvalue());

		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorVenueSelectDD]");
			ReportUtils
					.setStepDescription(
							"Unable to identify [LocationMapEditorVenueSelectDD]",
							true);
			m_assert.fail("Unable to identify [LocationMapEditorVenueSelectDD]");
		}
		logger.info("Identifying of  [Venue Selection DD] is Successful");
		// verify Building Selection DD is present or not
		// Identify Building Select DD
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"LocationMapEditorBuildingSelectDD").getLocatortype(),
				Suite.objectRepositoryMap.get(
						"LocationMapEditorBuildingSelectDD").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorBuildingSelectDD]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorBuildingSelectDD]",
					true);
			m_assert.fail("Unable to identify [LocationMapEditorBuildingSelectDD]");
		}
		logger.info("Identifying of  [Building Selection DD] is Successful");

		// verify Level Selection DD is present or not
		// Identify Building Select DD
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorLevelSelectDD")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorLevelSelectDD")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorLevelSelectDD]");
			ReportUtils
					.setStepDescription(
							"Unable to identify [LocationMapEditorLevelSelectDD]",
							true);
			m_assert.fail("Unable to identify [LocationMapEditorLevelSelectDD]");
		}
		logger.info("Identifying of  [Level Selection DD] is Successful");

		// verify Zoom-IN Butoon is present or not
		// Identify Zoom-In Butoon
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorZoomIn")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorZoomIn")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorZoomIn]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorZoomIn]", true);
			m_assert.fail("Unable to identify [LocationMapEditorZoomIn]");
		}
		logger.info("Identifying of  [Zoom In Button] is Successful");

		// verify Zoom-Out Butoon is present or not
		// Identify Zoom-Out Butoon
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorZoomOut")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorZoomOut")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorZoomOut]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorZoomOut]", true);
			m_assert.fail("Unable to identify [LocationMapEditorZoomOut]");
		}
		logger.info("Identifying of  [Zoom-Out Button] is Successful");

		// verify Draw-Draw-Marker Butoon is present or not
		// Identify Draw-Draw-Marker
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDMarker")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDMarker")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDMarker]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDMarker]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDMarker]");
		}
		logger.info("Identifying of  [Draw-Draw-Marker] is Successful");

		// verify Draw-Draw-Point Butoon is present or not
		// Identify Draw-Draw-Point
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDPoint")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDPoint")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDPoint]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDPoint]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDPoint]");
		}
		logger.info("Identifying of  [Draw-Draw-Point] is Successful");

		// verify Draw-Draw-Point Butoon is present or not
		// Identify Draw-Draw-Point
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDPoint")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDPoint")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDPoint]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDPoint]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDPoint]");
		}
		logger.info("Identifying of  [Draw-Draw-Point] is Successful");

		// verify Draw-Draw-PolyLine Butoon is present or not
		// Identify Draw-Draw-Point
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDPolyline")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDPolyline")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDPolyline]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDPolyline]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDPolyline]");
		}
		logger.info("Identifying of  [Draw-Draw-PolyLine] is Successful");

		// verify Draw-Draw-Circle Butoon is present or not
		// Identify Draw-Draw-Circle
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDCircle")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDCircle")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDCircle]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDCircle]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDCircle]");
		}
		logger.info("Identifying of  [Draw-Draw-Circle] is Successful");

		// verify Edit-Edit Butoon is present or not
		// Identify Edit-Edit Butoon
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditEdit")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditEdit")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDEditEdit]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDEditEdit]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDEditEdit]");
		}
		logger.info("Identifying of  [Edit-Edit  Butoon] is Successful");

		// verify Edit-Remove Butoon is present or not
		// Identify Edit-Remove Butoon
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditRemove")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditRemove")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDEditRemove]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDEditRemove]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDEditRemove]");
		}
		logger.info("Identifying of  [Edit-Remove] is Successful");

		// verify Toggle Segments checkBox
		// Identify Toggle Segments checkBox
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEdiorToggleSegment")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEdiorToggleSegment")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEdiorToggleSegment]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEdiorToggleSegment]", true);
			m_assert.fail("Unable to identify [LocationMapEdiorToggleSegment]");
		}
		// Get the text of the Toggle Segments checkBox
		String mapEditorToggleSegment = SeleniumUtils.getText(element);
		String ExpmapEditorToggleSegment = Suite.objectRepositoryMap.get(
				"LocationMapEdiorToggleSegment").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(mapEditorToggleSegment,
				ExpmapEditorToggleSegment);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[LocationMapEdiorToggleSegment]  text matching failed: The expected text is ["
					+ mapEditorToggleSegment
					+ "] and the actual return text is ["
					+ ExpmapEditorToggleSegment + "]");
			ReportUtils
					.setStepDescription(
							"[LocationMapEdiorToggleSegment] tab text matching failed",
							"", mapEditorToggleSegment,
							ExpmapEditorToggleSegment, true);
			m_assert.assertEquals(mapEditorToggleSegment,
					ExpmapEditorToggleSegment);
		}
		logger.info("Verify [Toggle Segments checkBox] is Successful");

		// verify Toggle Routes checkBox
		// Identify Toggle Routes checkBox
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEdiorToggleRoutes")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEdiorToggleRoutes")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEdiorToggleRoutes]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEdiorToggleRoutes]", true);
			m_assert.fail("Unable to identify [LocationMapEdiorToggleRoutes]");
		}
		// Get the text of the Toggle Segments checkBox
		String mapEditorToggleRoutes = SeleniumUtils.getText(element);
		String ExpmapEditorToggleRoutes = Suite.objectRepositoryMap.get(
				"LocationMapEdiorToggleRoutes").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(mapEditorToggleRoutes,
				ExpmapEditorToggleRoutes);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[LocationMapEdiorToggleRoutes]  text matching failed: The expected text is ["
					+ mapEditorToggleRoutes
					+ "] and the actual return text is ["
					+ ExpmapEditorToggleRoutes + "]");
			ReportUtils.setStepDescription(
					"[LocationMapEdiorToggleRoutes] tab text matching failed",
					"", mapEditorToggleRoutes, ExpmapEditorToggleRoutes, true);
			m_assert.assertEquals(mapEditorToggleRoutes,
					ExpmapEditorToggleRoutes);
		}
		logger.info("Verify [Toggle Routes checkBox] is Successful");

		// verify Toggle Zones checkBox
		// Identify Toggle Zones checkBox
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEdiorToggleZones")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEdiorToggleZones")
						.getLocatorvalue());
		// Get the text of the Toggle Segments checkBox
		String mapEditorToggleZones = SeleniumUtils.getText(element);
		String ExpmapEditorToggleZones = Suite.objectRepositoryMap.get(
				"LocationMapEdiorToggleZones").getExptext();
		// Compare the text with expected text
		isTextMatching = SeleniumUtils.assertEqual(mapEditorToggleZones,
				ExpmapEditorToggleZones);
		// If texts are not same then throw the error and exit
		if (!isTextMatching) {
			logger.error("[LocationMapEdiorToggleZones]  text matching failed: The expected text is ["
					+ mapEditorToggleZones
					+ "] and the actual return text is ["
					+ ExpmapEditorToggleZones + "]");
			ReportUtils.setStepDescription(
					"[LocationMapEdiorToggleZones] tab text matching failed",
					"", mapEditorToggleZones, ExpmapEditorToggleZones, true);
			m_assert.assertEquals(mapEditorToggleZones, ExpmapEditorToggleZones);
		}
		logger.info("Verify [Toggle Zones checkBox] is Successful");

		m_assert.assertAll();

	}

	@Test(priority = 8, dependsOnMethods = "loginAs")
	public void creatingDDCircle_One() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("creatingDDCircle_One")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [creatingDDCircle_One] is not added for execution in [Location]");
			ReportUtils
					.setStepDescription(
							"Test case [creatingDDCircle_One] is not added for execution in [Location]",
							false);
			throw new SkipException(
					"Test case [creatingDDCircle_One] is not added for execution in [Location]");
		}
		logger.info("Test case [creatingDDCircle_One] is added for execution in [Location]");
		// Click on Draw Draw Circle
		SeleniumUtils.sleepThread(2);
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDCircle")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDCircle")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDCircle]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDCircle]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDCircle]");
		}
		logger.info("Identifying of  [LocationMapEditorDDCircle] is Successful");
		logger.info("creating the zone in the map");
		// clicks the zone element and holds the element and drags the mouse
		// w.r.t offset values
		SeleniumUtils.sleepThread(5);

		if (configproperties.get(0).equalsIgnoreCase("CHROME")) {
			SeleniumUtils.createZone(element, 200, 250);
		} else if (configproperties.get(0).equalsIgnoreCase("FIREFOX")) {

			SeleniumUtils.createZone(element, 200, 300);
		}

		logger.info("creating Circle is completed ");
		m_assert.assertAll();
	}

	@Test(priority = 9, dependsOnMethods = "loginAs")
	public void AddingZoneTrigger_One() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution

		boolean forExecution = false;
		boolean isSelected = false;
		testcaseArgs = getTestData("AddingZoneTrigger_One");
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("AddingZoneTrigger_One")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [AddingZoneTrigger_One] is not added for execution in [Location]");
			ReportUtils
					.setStepDescription(
							"Test case [AddingZoneTrigger_One] is not added for execution in [Location]",
							false);
			throw new SkipException(
					"Test case [AddingZoneTrigger_One] is not added for execution in [Location]");
		}
		logger.info("saving the created cirle_one by providing name, message, cooldownHours and CoolDownMinutes");
		WebElement TriggerName = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ZoneName").getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneName").getLocatorvalue());
		SeleniumUtils.clickOnElement(TriggerName);
		SeleniumUtils.type(TriggerName, testcaseArgs.get("Name"));
		logger.info("inserted name successfully");

		WebElement TriggerMessage = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ZoneMessage").getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneMessage").getLocatorvalue());
		SeleniumUtils.clickOnElement(TriggerMessage);
		SeleniumUtils.type(TriggerMessage, testcaseArgs.get("Message"));
		logger.info("inserted Message successfully");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ZoneCoolDownHours")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneCoolDownHours")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [ZoneCoolDownHours]");
			ReportUtils.setStepDescription(
					"Unable to identify [ZoneCoolDownHours]", true);
			m_assert.fail("Unable to identify [ZoneCoolDownHours]");
		}
		// SeleniumUtils.clickOnElement(Hours_One);
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("ZoneCoolDownHours")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneCoolDownHours")
						.getLocatorvalue(), testcaseArgs.get("zoneHours"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("zoneHours")
					+ "] from dropdown");
			ReportUtils
					.setStepDescription(
							"Unable to identify [ZoneCoolDownHours]Unable to select  ["
									+ testcaseArgs.get("zoneHours")
									+ "] from dropdown", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("zoneHours")
					+ "] from dropdown");
		}
		logger.info("cooldownHours selected succesfully");
		ReportUtils.setStepDescription("selected Hours succesfully", "15",
				testcaseArgs.get("zoneHours"), false);
		SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ZoneCoolDownMnts")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneCoolDownMnts")
						.getLocatorvalue());
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("ZoneCoolDownMnts")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneCoolDownMnts")
						.getLocatorvalue(), testcaseArgs.get("zoneMin"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("zoneMin")
					+ "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("zoneMin")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("zoneMin")
					+ "] from dropdown");
		}
		logger.info("cooldownMints selected succesfully");
		ReportUtils.setStepDescription("inserted minutes succesfully", "45",
				testcaseArgs.get("zoneMin"), false);
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ZoneSaveBtn").getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneSaveBtn").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [ZoneSaveBtn]");
			ReportUtils.setStepDescription("Unable to identify [ZoneSaveBtn]",
					true);
			m_assert.fail("Unable to identify [ZoneSaveBtn]");
		}
		/*
		 * WebElement ZoneSave = SeleniumUtils.findobject(
		 * Suite.objectRepositoryMap.get("ZoneSaveBtn").getLocatortype(),
		 * Suite.objectRepositoryMap.get("ZoneSaveBtn").getLocatorvalue());
		 */
		SeleniumUtils.clickOnElement(element);
		logger.info("zone1 saved succesfully");
		m_assert.assertAll();
	}

	@Test(priority = 10, dependsOnMethods = "loginAs")
	public void creatingPointOfInterest_One() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("creatingPointOfInterest_One")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [creatingPointOfInterest_One] is not added for execution in [Location]");
			ReportUtils
					.setStepDescription(
							"Test case [creatingPointOfInterest_One] is not added for execution in [Location]",
							false);
			throw new SkipException(
					"Test case [creatingPointOfInterest_One] is not added for execution in [Location]");
		}
		// Creating Point Of Interest
		SeleniumUtils.sleepThread(2);
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDMarker")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDMarker")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDMarker]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDMarker]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDMarker]");
		}
		SeleniumUtils.createPOI(element, 200, 300);
		logger.info("POI1 is created successfully ");
		m_assert.assertAll();
	}

	@Test(priority = 11, dependsOnMethods = "loginAs")
	public void savingPOI_One() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution

		boolean forExecution = false;
		boolean isSelected = false;
		testcaseArgs = getTestData("savingPOI_One");
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("savingPOI_One")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [savingPOI_One] is not added for execution in [Location]");
			ReportUtils
					.setStepDescription(
							"Test case [savingPOI_One] is not added for execution in [Location]",
							false);
			throw new SkipException(
					"Test case [savingPOI_One] is not added for execution in [Location]");
		}
		WebElement poiName = SeleniumUtils.findobject(Suite.objectRepositoryMap
				.get("POIName").getLocatortype(), Suite.objectRepositoryMap
				.get("POIName").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [POIName]");
			ReportUtils
					.setStepDescription("Unable to identify [POIName]", true);
			m_assert.fail("Unable to identify [POIName]");
		}
		SeleniumUtils.clickOnElement(poiName);
		SeleniumUtils.type(poiName, testcaseArgs.get("Name"));
		logger.info("POI1 : inserted name successfully");
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("POIType").getLocatortype(),
				Suite.objectRepositoryMap.get("POIType").getLocatorvalue(),
				testcaseArgs.get("Type"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("Type")
					+ "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("Type")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("Type")
					+ "] from dropdown");
		}
		logger.info("POI1 Type is selected succesfully");
		WebElement poiPortal = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("POIPortalId").getLocatortype(),
				Suite.objectRepositoryMap.get("POIPortalId").getLocatorvalue());
		SeleniumUtils.clickOnElement(poiPortal);
		if (poiPortal == null) {
			logger.error("Unable to identify [POIPortalId]");
			ReportUtils.setStepDescription("Unable to identify [POIPortalId]",
					true);
			m_assert.fail("Unable to identify [POIPortalId]");
		}
		SeleniumUtils.type(poiPortal, testcaseArgs.get("portalId"));
		logger.info("inserted portalID successfully");

		isSelected = SeleniumUtils
				.selectDropdownByText(
						Suite.objectRepositoryMap.get("POIZoomLevel")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("POIZoomLevel").getLocatorvalue(),
						testcaseArgs.get("zoomLevel"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("zoomLevel")
					+ "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("zoomLevel")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("zoomLevel")
					+ "] from dropdown");
		}
		logger.info("POI1 Zoom Level succesfully");
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("POIMaximumZoomLevel")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("POIMaximumZoomLevel")
						.getLocatorvalue(), testcaseArgs.get("maxZoomLevel"));
		if (!isSelected) {
			logger.error("Unable to select  ["
					+ testcaseArgs.get("maxZoomLevel") + "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("maxZoomLevel")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  ["
					+ testcaseArgs.get("maxZoomLevel") + "] from dropdown");
		}
		logger.info("POI1 Max Zoom Level succesfully");
		WebElement poiDescription = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("POIDescription")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("POIDescription")
						.getLocatorvalue());
		if (poiDescription == null) {
			logger.error("Unable to identify [POIDescription]");
			ReportUtils.setStepDescription(
					"Unable to identify [POIDescription]", true);
			m_assert.fail("Unable to identify [POIDescription]");
		}
		SeleniumUtils.clickOnElement(poiDescription);
		SeleniumUtils.type(poiDescription, testcaseArgs.get("description"));
		logger.info("Entered Description successfully");

		WebElement isActive = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("POIIsActive").getLocatortype(),
				Suite.objectRepositoryMap.get("POIIsActive").getLocatorvalue());
		if (isActive == null) {
			logger.error("Unable to identify [POIIsActive]");
			ReportUtils.setStepDescription("Unable to identify [POIIsActive]",
					true);
			m_assert.fail("Unable to identify [POIIsActive]");
		}
		SeleniumUtils.clickOnElement(isActive);

		WebElement poiSave = SeleniumUtils.findobject(Suite.objectRepositoryMap
				.get("POISave").getLocatortype(), Suite.objectRepositoryMap
				.get("POISave").getLocatorvalue());
		if (poiSave == null) {
			logger.error("Unable to identify [POISave]");
			ReportUtils
					.setStepDescription("Unable to identify [POISave]", true);
			m_assert.fail("Unable to identify [POISave]");
		}
		SeleniumUtils.clickOnElement(poiSave);
		logger.info("POI1 is saved succesfully");
		m_assert.assertAll();
	}

	@Test(priority = 12, dependsOnMethods = "loginAs")
	public void creatingDDCircle_Two() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("creatingDDCircle_Two")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [creatingDDCircle_Two] is not added for execution in [Location]");
			ReportUtils
					.setStepDescription(
							"Test case [creatingDDCircle_Two] is not added for execution in [Location]",
							false);
			throw new SkipException(
					"Test case [creatingDDCircle_Two] is not added for execution in [Location]");
		}
		logger.info("Test case [creatingDDCircle_Two] is added for execution in [Location]");
		// Click on Draw Draw Circle
		SeleniumUtils.sleepThread(2);
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDCircle")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDCircle")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDCircle]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDCircle]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDCircle]");
		}

		ReportUtils.setStepDescription("creating Zone ", true);
		logger.info("Identifying of  [LocationMapEditorDDCircle] is Successful");
		logger.info("creating the zone in the map");
		// clicks the zone element and holds the element and drags the mouse
		// w.r.t offset values
		SeleniumUtils.sleepThread(5);
		SeleniumUtils.createZone(element, 400, 200);
		ReportUtils.setStepDescription("creating Circle is completed ", true);
		logger.info("creating Circle is completed ");
		m_assert.assertAll();
	}

	@Test(priority = 13, dependsOnMethods = "loginAs")
	public void AddingZoneTrigger_Two() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		boolean isSelected = false;
		testcaseArgs = getTestData("AddingZoneTrigger_Two");
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("AddingZoneTrigger_Two")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [AddingZoneTrigger_Two] is not added for execution in [Location]");
			ReportUtils
					.setStepDescription(
							"Test case [AddingZoneTrigger_Two] is not added for execution in [Location]",
							false);
			throw new SkipException(
					"Test case [AddingZoneTrigger_Two] is not added for execution in [Location]");
		}
		logger.info("saving the created cirle_two by providing name, message, cooldownHours and CoolDownMinutes");
		SeleniumUtils.sleepThread(1);
		SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ZoneName").getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneName").getLocatorvalue());
		WebElement TriggerName = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ZoneName").getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneName").getLocatorvalue());
		if (TriggerName == null) {
			logger.error("Unable to identify [ZoneName]");
			ReportUtils.setStepDescription("Unable to identify [ZoneName]",
					true);
			m_assert.fail("Unable to identify [ZoneName]");
		}
		SeleniumUtils.clickOnElement(TriggerName);
		SeleniumUtils.type(TriggerName, testcaseArgs.get("NameTwo"));
		SeleniumUtils.sleepThread(3);
		logger.info("inserted name successfully");

		WebElement TriggerMessage = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ZoneMessage").getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneMessage").getLocatorvalue());
		if (TriggerMessage == null) {
			logger.error("Unable to identify [ZoneMessage]");
			ReportUtils.setStepDescription("Unable to identify [ZoneMessage]",
					true);
			m_assert.fail("Unable to identify [ZoneMessage]");
		}
		SeleniumUtils.clickOnElement(TriggerMessage);
		SeleniumUtils.type(TriggerMessage, testcaseArgs.get("Message"));
		logger.info("inserted Message successfully");
		SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ZoneCoolDownHours")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneCoolDownHours")
						.getLocatorvalue());
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("ZoneCoolDownHours")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneCoolDownHours")
						.getLocatorvalue(), testcaseArgs.get("zoneHours"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("zoneHours")
					+ "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("zoneHours")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("zoneHours")
					+ "] from dropdown");
		}
		logger.info("cooldownHours selected succesfully");
		SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("ZoneCoolDownMnts")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneCoolDownMnts")
						.getLocatorvalue());
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("ZoneCoolDownMnts")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneCoolDownMnts")
						.getLocatorvalue(), testcaseArgs.get("zoneMin"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("zoneMin")
					+ "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("zoneMin")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("zoneMin")
					+ "] from dropdown");
		}
		logger.info("cooldownMints selected succesfully");
		WebElement ZoneSave = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ZoneSaveBtn").getLocatortype(),
				Suite.objectRepositoryMap.get("ZoneSaveBtn").getLocatorvalue());
		if (ZoneSave == null) {
			logger.error("Unable to identify [ZoneSaveBtn]");
			ReportUtils.setStepDescription("Unable to identify [ZoneSaveBtn]",
					true);
			m_assert.fail("Unable to identify [ZoneSaveBtn]");
		}
		SeleniumUtils.clickOnElement(ZoneSave);
		logger.info("zone2 saved succesfully");
		m_assert.assertAll();
	}

	@Test(priority = 14, dependsOnMethods = "loginAs")
	public void creatingPointOfInterest_Two() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("creatingPointOfInterest_Two")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [creatingPointOfInterest_Two] is not added for execution in [Location]");
			ReportUtils
					.setStepDescription(
							"Test case [creatingPointOfInterest_Two] is not added for execution in [Location]",
							false);
			throw new SkipException(
					"Test case [creatingPointOfInterest_Two] is not added for execution in [Location]");
		}
		// Creating Point Of Interest
		SeleniumUtils.sleepThread(2);
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDMarker")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDMarker")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDMarker]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDMarker]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDMarker]");
		}
		SeleniumUtils.createPOI(element, 400, 200);
		logger.info("POI2 is created successfully ");
		m_assert.assertAll();
	}

	@Test(priority = 15, dependsOnMethods = "loginAs")
	public void savingPOI_Two() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution

		boolean forExecution = false;
		boolean isSelected = false;
		testcaseArgs = getTestData("savingPOI_Two");
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("savingPOI_Two")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [savingPOI_Two] is not added for execution in [Location]");
			ReportUtils
					.setStepDescription(
							"Test case [savingPOI_Two] is not added for execution in [Location]",
							false);
			throw new SkipException(
					"Test case [savingPOI_Two] is not added for execution in [Location]");
		}
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("POIPortalId").getLocatortype(),
				Suite.objectRepositoryMap.get("POIPortalId").getLocatorvalue());
		SeleniumUtils.clickOnElement(element);
		if (element == null) {
			logger.error("Unable to identify [POIPortalId]");
			ReportUtils.setStepDescription("Unable to identify [POIPortalId]",
					true);
			m_assert.fail("Unable to identify [POIPortalId]");
		}
		SeleniumUtils.type(element, testcaseArgs.get("portalId"));
		logger.info("inserted portalID successfully");
		SeleniumUtils.sleepThread(2);
		element = SeleniumUtils
				.waitForElementToIdentify(
						Suite.objectRepositoryMap.get("AddingPOIName")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("AddingPOIName").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [AddingPOIName]");
			ReportUtils.setStepDescription(
					"Unable to identify [AddingPOIName]", true);
			m_assert.fail("Unable to identify [AddingPOIName]");
		}
		SeleniumUtils.sleepThread(5);
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(5);
		SeleniumUtils.type(element, testcaseArgs.get("Name"));
		logger.info("POI2 : inserted name successfully");
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("POIType").getLocatortype(),
				Suite.objectRepositoryMap.get("POIType").getLocatorvalue(),
				testcaseArgs.get("Type"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("Type")
					+ "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("Type")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("Type")
					+ "] from dropdown");
		}
		logger.info("POI2 Type is selected succesfully");

		isSelected = SeleniumUtils
				.selectDropdownByText(
						Suite.objectRepositoryMap.get("POIZoomLevel")
								.getLocatortype(), Suite.objectRepositoryMap
								.get("POIZoomLevel").getLocatorvalue(),
						testcaseArgs.get("zoomLevel"));
		if (!isSelected) {
			logger.error("Unable to select  [" + testcaseArgs.get("zoomLevel")
					+ "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("zoomLevel")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  [" + testcaseArgs.get("zoomLevel")
					+ "] from dropdown");
		}
		logger.info("POI2 Zoom Level succesfully");
		isSelected = SeleniumUtils.selectDropdownByText(
				Suite.objectRepositoryMap.get("POIMaximumZoomLevel")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("POIMaximumZoomLevel")
						.getLocatorvalue(), testcaseArgs.get("maxZoomLevel"));
		if (!isSelected) {
			logger.error("Unable to select  ["
					+ testcaseArgs.get("maxZoomLevel") + "] from dropdown");
			ReportUtils.setStepDescription(
					"Unable to select  [" + testcaseArgs.get("maxZoomLevel")
							+ "] from dropdown", true);
			m_assert.fail("Unable to select  ["
					+ testcaseArgs.get("maxZoomLevel") + "] from dropdown");
		}
		logger.info("POI2 Max Zoom Level succesfully");
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("POIDescription")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("POIDescription")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [POIDescription]");
			ReportUtils.setStepDescription(
					"Unable to identify [POIDescription]", true);
			m_assert.fail("Unable to identify [POIDescription]");
		}
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.type(element, testcaseArgs.get("description"));
		logger.info("Entered Description successfully");

		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("POIIsActive").getLocatortype(),
				Suite.objectRepositoryMap.get("POIIsActive").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [POIIsActive]");
			ReportUtils.setStepDescription("Unable to identify [POIIsActive]",
					true);
			m_assert.fail("Unable to identify [POIIsActive]");
		}
		SeleniumUtils.clickOnElement(element);

		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("POISave").getLocatortype(),
				Suite.objectRepositoryMap.get("POISave").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [POISave]");
			ReportUtils
					.setStepDescription("Unable to identify [POISave]", true);
			m_assert.fail("Unable to identify [POISave]");
		}
		SeleniumUtils.clickOnElement(element);
		logger.info("POI2 is saved succesfully");
		m_assert.assertAll();
	}

	@Test(priority = 16, dependsOnMethods = "loginAs")
	public void CreatingWayPoint() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("CreatingWayPoint")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [CreatingWayPoint] is not added for execution in [Location]");
			ReportUtils
					.setStepDescription(
							"Test case [CreatingWayPoint] is not added for execution in [Location]",
							false);
			throw new SkipException(
					"Test case [CreatingWayPoint] is not added for execution in [Location]");
		}
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDPoint")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDPoint")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [POISave]");
			ReportUtils
					.setStepDescription("Unable to identify [POISave]", true);
			m_assert.fail("Unable to identify [POISave]");
		}
		// Click and hold on routes
		if (configproperties.get(0).equalsIgnoreCase("FIREFOX")) {
			SeleniumUtils.createWayPoint(element, 303, 279);
			SeleniumUtils.sleepThread(1);
			SeleniumUtils.createWayPoint(element, 364, 279);
			SeleniumUtils.sleepThread(1);
			SeleniumUtils.createWayPoint(element, 365, 169);
			SeleniumUtils.sleepThread(1);
			SeleniumUtils.createWayPoint(element, 433, 162);
			SeleniumUtils.sleepThread(1);
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap
							.get("LocationMapEditorDDPolyline")
							.getLocatortype(),
					Suite.objectRepositoryMap
							.get("LocationMapEditorDDPolyline")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [LocationMapEditorDDPolyline]");
				ReportUtils.setStepDescription(
						"Unable to identify [LocationMapEditorDDPolyline]",
						true);
				m_assert.fail("Unable to identify [LocationMapEditorDDPolyline]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.waitForElementToIdentify(Suite.objectRepositoryMap
					.get("wayPoint1_firefox").getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint1_firefox")
							.getLocatorvalue());
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("wayPoint1_firefox")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint1_firefox")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint1_firefox]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint1_firefox]", true);
				m_assert.fail("Unable to identify [wayPoint1_firefox]");
			}
			SeleniumUtils.sleepThread(2);
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.waitForElementToIdentify(Suite.objectRepositoryMap
					.get("wayPoint2_firefox").getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint2_firefox")
							.getLocatorvalue());
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("wayPoint2_firefox")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint2_firefox")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint2_firefox]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint2_firefox]", true);
				m_assert.fail("Unable to identify [wayPoint2_firefox]");
			}
			SeleniumUtils.routes(element);
			SeleniumUtils.sleepThread(2);
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.waitForElementToIdentify(Suite.objectRepositoryMap
					.get("wayPoint3_firefox").getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint3_firefox")
							.getLocatorvalue());
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("wayPoint3_firefox")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint3_firefox")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint3_firefox]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint3_firefox]", true);
				m_assert.fail("Unable to identify [wayPoint3_firefox]");
			}
			SeleniumUtils.routes(element);
			SeleniumUtils.sleepThread(2);
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.waitForElementToIdentify(Suite.objectRepositoryMap
					.get("wayPoint4_firefox").getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint4_firefox")
							.getLocatorvalue());
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get("wayPoint4_firefox")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint4_firefox")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint4_firefox]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint4_firefox]", true);
				m_assert.fail("Unable to identify [wayPoint4_firefox]");
			}
			SeleniumUtils.routes(element);
			SeleniumUtils.sleepThread(2);
		} else if (configproperties.get(0).equalsIgnoreCase("CHROME")) {
			SeleniumUtils.createWayPoint(element, 303, 279);
			SeleniumUtils.sleepThread(1);
			SeleniumUtils.createWayPoint(element, 364, 279);
			SeleniumUtils.sleepThread(1);
			SeleniumUtils.createWayPoint(element, 365, 169);
			SeleniumUtils.sleepThread(1);
			SeleniumUtils.createWayPoint(element, 433, 162);
			SeleniumUtils.sleepThread(1);
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap
							.get("LocationMapEditorDDPolyline")
							.getLocatortype(),
					Suite.objectRepositoryMap
							.get("LocationMapEditorDDPolyline")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [LocationMapEditorDDPolyline]");
				ReportUtils.setStepDescription(
						"Unable to identify [LocationMapEditorDDPolyline]",
						true);
				m_assert.fail("Unable to identify [LocationMapEditorDDPolyline]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.waitForElementToIdentify(Suite.objectRepositoryMap
					.get("wayPoint1").getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint1")
							.getLocatorvalue());
			element = SeleniumUtils
					.findobject(Suite.objectRepositoryMap.get("wayPoint1")
							.getLocatortype(),
							Suite.objectRepositoryMap.get("wayPoint1")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint1]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint1]", true);
				m_assert.fail("Unable to identify [wayPoint1]");
			}
			SeleniumUtils.sleepThread(2);
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.waitForElementToIdentify(Suite.objectRepositoryMap
					.get("wayPoint2").getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint2")
							.getLocatorvalue());
			element = SeleniumUtils
					.findobject(Suite.objectRepositoryMap.get("wayPoint2")
							.getLocatortype(),
							Suite.objectRepositoryMap.get("wayPoint2")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint2]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint2]", true);
				m_assert.fail("Unable to identify [wayPoint2]");
			}
			SeleniumUtils.routes(element);
			SeleniumUtils.sleepThread(2);
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.waitForElementToIdentify(Suite.objectRepositoryMap
					.get("wayPoint3").getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint3")
							.getLocatorvalue());
			element = SeleniumUtils
					.findobject(Suite.objectRepositoryMap.get("wayPoint3")
							.getLocatortype(),
							Suite.objectRepositoryMap.get("wayPoint3")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint3]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint3]", true);
				m_assert.fail("Unable to identify [wayPoint3]");
			}
			SeleniumUtils.routes(element);
			SeleniumUtils.sleepThread(2);
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.waitForElementToIdentify(Suite.objectRepositoryMap
					.get("wayPoint4").getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint4")
							.getLocatorvalue());
			element = SeleniumUtils
					.findobject(Suite.objectRepositoryMap.get("wayPoint4")
							.getLocatortype(),
							Suite.objectRepositoryMap.get("wayPoint4")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint4]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint4]", true);
				m_assert.fail("Unable to identify [wayPoint4]");
			}
			SeleniumUtils.routes(element);
			SeleniumUtils.sleepThread(2);
		}
		m_assert.assertAll();
	}

	@Test(priority = 17, dependsOnMethods = "loginAs")
	public void verifyMappingLayoutPullTab() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution

		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("verifyMappingLayoutPullTab")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [verifyMappingLayoutPullTab] is not added for execution in [Location]");
			ReportUtils
					.setStepDescription(
							"Test case [verifyMappingLayoutPullTab] is not added for execution in [Location]",
							false);
			throw new SkipException(
					"Test case [verifyMappingLayoutPullTab] is not added for execution in [Location]");
		}
		SeleniumUtils.sleepThread(10);
		// verify Pull-Tab Butoon is present or not
		// Identify Edit-Remove Butoon
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEdiorPullTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEdiorPullTab")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEdiorPullTab]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEdiorPullTab]", true);
			m_assert.fail("Unable to identify [LocationMapEdiorPullTab]");
		}
		// Click on Pull-Tab Button
		SeleniumUtils.click(
				Suite.objectRepositoryMap.get("LocationMapEdiorPullTab")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEdiorPullTab")
						.getLocatorvalue());
		SeleniumUtils.sleepThread(10);
		m_assert.assertAll();

	}

	@Test(priority = 18, dependsOnMethods = "loginAs")
	public void editingElements() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check if test case verifyLocationLayout is added for
		// execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("editingElements")) {
				forExecution = true;
				break;
			}
		}
		// If Test case [loginAs] is not added then skip the suite
		if (!forExecution) {
			logger.warn("Test case [editingElements] is not added for execution in [Location]");
			ReportUtils
					.setStepDescription(
							"Test case [editingElements] is not added for execution in [Location]",
							false);
			throw new SkipException(
					"Test case [editingElements] is not added for execution in [Location]");
		}
		// Identify Edit button
		element = SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditEdit")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditEdit")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDEditEdit]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDEditEdit]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDEditEdit]");
		}
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		if (configproperties.get(0).equalsIgnoreCase("CHROME")) {
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("poiOne").getLocatortype(),
					Suite.objectRepositoryMap.get("poiOne").getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [poiOne]");
				ReportUtils.setStepDescription("Unable to identify [poiOne]",
						true);
				m_assert.fail("Unable to identify [poiOne]");
			}
			SeleniumUtils.sleepThread(3);
			SeleniumUtils.moveToPoint_chrome(element, 0, -100);
			SeleniumUtils.sleepThread(4);

			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditEditEnabled")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditEditEnabled")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [LocationMapEditorDDEditEditEnabled]");
				ReportUtils
						.setStepDescription(
								"Unable to identify [LocationMapEditorDDEditEditEnabled]",
								true);
				m_assert.fail("Unable to identify [LocationMapEditorDDEditEditEnabled]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);

			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("moveZone1Point")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("moveZone1Point")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [moveZone1Point]");
				ReportUtils.setStepDescription(
						"Unable to identify [moveZone1Point]", true);
				m_assert.fail("Unable to identify [moveZone1Point]");
			}
			SeleniumUtils.sleepThread(3);
			SeleniumUtils.moveToPoint_chrome(element, 0, -100);
			SeleniumUtils.sleepThread(4);

			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditEditEnabled")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditEditEnabled")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [LocationMapEditorDDEditEditEnabled]");
				ReportUtils
						.setStepDescription(
								"Unable to identify [LocationMapEditorDDEditEditEnabled]",
								true);
				m_assert.fail("Unable to identify [LocationMapEditorDDEditEditEnabled]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			element = SeleniumUtils
					.waitForElementToIdentify(
							Suite.objectRepositoryMap.get("wayPoint1")
									.getLocatortype(),
							Suite.objectRepositoryMap.get("wayPoint1")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint1]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint1]", true);
				m_assert.fail("Unable to identify [wayPoint1]");
			}
			SeleniumUtils.sleepThread(3);
			SeleniumUtils.moveToPoint_chrome(element, 0, -100);
			SeleniumUtils.sleepThread(4);
		} else if (configproperties.get(0).equalsIgnoreCase("FIREFOX")) {
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("poiOne_firefox")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("poiOne_firefox")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [poiOne_firefox]");
				ReportUtils.setStepDescription(
						"Unable to identify [poiOne_firefox]", true);
				m_assert.fail("Unable to identify [poiOne_firefox]");
			}
			SeleniumUtils.sleepThread(3);
			SeleniumUtils.moveToPoint(element, 0, -100);
			SeleniumUtils.sleepThread(4);

			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditEditEnabled")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditEditEnabled")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [LocationMapEditorDDEditEditEnabled]");
				ReportUtils
						.setStepDescription(
								"Unable to identify [LocationMapEditorDDEditEditEnabled]",
								true);
				m_assert.fail("Unable to identify [LocationMapEditorDDEditEditEnabled]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("moveZone1Point_firefox")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("moveZone1Point_firefox")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [moveZone1Point_firefox]");
				ReportUtils.setStepDescription(
						"Unable to identify [moveZone1Point_firefox]", true);
				m_assert.fail("Unable to identify [moveZone1Point_firefox]");
			}
			SeleniumUtils.sleepThread(3);
			SeleniumUtils.moveToPoint(element, 0, -100);
			SeleniumUtils.sleepThread(4);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditEditEnabled")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditEditEnabled")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [LocationMapEditorDDEditEditEnabled]");
				ReportUtils
						.setStepDescription(
								"Unable to identify [LocationMapEditorDDEditEditEnabled]",
								true);
				m_assert.fail("Unable to identify [LocationMapEditorDDEditEditEnabled]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(4);

			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("wayPoint1_firefox")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint1_firefox")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint1_firefox]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint1_firefox]", true);
				m_assert.fail("Unable to identify [wayPoint1_firefox]");
			}
			SeleniumUtils.sleepThread(3);
			SeleniumUtils.moveToPoint(element, 0, -100);
			SeleniumUtils.sleepThread(4);

		}
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("saveChanges").getLocatortype(),
				Suite.objectRepositoryMap.get("saveChanges").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [saveChanges]");
			ReportUtils.setStepDescription("Unable to identify [saveChanges]",
					true);
			m_assert.fail("Unable to identify [saveChanges]");
		}
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(4);
		SeleniumUtils.acceptAlertWindow();
		m_assert.assertAll();
	}

	@Test(priority = 19, dependsOnMethods = "loginAs")
	public void removingElementsOnMap() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("removingElementsOnMap")) {
				forExecution = true;
				break;
			}
		}		
		if (!forExecution) {
			logger.warn("Test case [removingElementsOnMap] is not added for execution in [Location]");
			ReportUtils
					.setStepDescription(
							"Test case [removingElementsOnMap] is not added for execution in [Location]",
							false);
			throw new SkipException(
					"Test case [removingElementsOnMap] is not added for execution in [Location]");
		}		
		SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditRemove")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditRemove")
						.getLocatorvalue());
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditRemove")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("LocationMapEditorDDEditRemove")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDEditRemove]");
			ReportUtils.setStepDescription(
					"Unable to identify [LocationMapEditorDDEditRemove]", true);
			m_assert.fail("Unable to identify [LocationMapEditorDDEditRemove]");
		}
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.deleteCreatedZone(200, 150);
		SeleniumUtils.sleepThread(1);
		SeleniumUtils.waitForElementToIdentify(
				Suite.objectRepositoryMap.get(
						"LocationMapEditorDDEditRemoveEnabled")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"LocationMapEditorDDEditRemoveEnabled")
						.getLocatorvalue());
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get(
						"LocationMapEditorDDEditRemoveEnabled")
						.getLocatortype(),
				Suite.objectRepositoryMap.get(
						"LocationMapEditorDDEditRemoveEnabled")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
			ReportUtils
					.setStepDescription(
							"Unable to identify [LocationMapEditorDDEditRemoveEnabled]",
							true);
			m_assert.fail("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
		}
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.deleteCreatedZone(375, 175);
		SeleniumUtils.sleepThread(1);
		if (configproperties.get(0).equalsIgnoreCase("CHROME")) {
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
				ReportUtils
						.setStepDescription(
								"Unable to identify [LocationMapEditorDDEditRemoveEnabled]",
								true);
				m_assert.fail("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.waitForElementToIdentify(Suite.objectRepositoryMap
					.get("wayPoint1AfterEditing").getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint4")
							.getLocatorvalue());
			element = SeleniumUtils
					.findobject(Suite.objectRepositoryMap.get("wayPoint4")
							.getLocatortype(),
							Suite.objectRepositoryMap.get("wayPoint4")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint4]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint4]", true);
				m_assert.fail("Unable to identify [wayPoint4]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(1);
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
				ReportUtils
						.setStepDescription(
								"Unable to identify [LocationMapEditorDDEditRemoveEnabled]",
								true);
				m_assert.fail("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.waitForElementToIdentify(Suite.objectRepositoryMap
					.get("wayPoint2").getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint3")
							.getLocatorvalue());
			element = SeleniumUtils
					.findobject(Suite.objectRepositoryMap.get("wayPoint3")
							.getLocatortype(),
							Suite.objectRepositoryMap.get("wayPoint3")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint3]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint3]", true);
				m_assert.fail("Unable to identify [wayPoint3]");
			}
			SeleniumUtils.sleepThread(1);
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(1);
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
				ReportUtils
						.setStepDescription(
								"Unable to identify [LocationMapEditorDDEditRemoveEnabled]",
								true);
				m_assert.fail("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.waitForElementToIdentify(Suite.objectRepositoryMap
					.get("wayPoint3_firefox").getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint2")
							.getLocatorvalue());
			element = SeleniumUtils
					.findobject(Suite.objectRepositoryMap.get("wayPoint2")
							.getLocatortype(),
							Suite.objectRepositoryMap.get("wayPoint2")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint2]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint3]", true);
				m_assert.fail("Unable to identify [wayPoint2]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(1);
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
				ReportUtils
						.setStepDescription(
								"Unable to identify [LocationMapEditorDDEditRemoveEnabled]",
								true);
				m_assert.fail("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.waitForElementToIdentify(Suite.objectRepositoryMap
					.get("wayPoint4_firefox").getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint1")
							.getLocatorvalue());
			element = SeleniumUtils
					.findobject(Suite.objectRepositoryMap.get("wayPoint1")
							.getLocatortype(),
							Suite.objectRepositoryMap.get("wayPoint1")
									.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint1]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint4]", true);
				m_assert.fail("Unable to identify [wayPoint4]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(2);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
				ReportUtils
						.setStepDescription(
								"Unable to identify [LocationMapEditorDDEditRemoveEnabled]",
								true);
				m_assert.fail("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(2);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"LocationMapEdiorPullTab_Right").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"LocationMapEdiorPullTab_Right").getLocatorvalue());

			if (element == null) {
				logger.error("Unable to identify [LocationMapEdiorPullTab_Right]");
				ReportUtils.setStepDescription(
						"Unable to identify [LocationMapEdiorPullTab_Right]",
						true);
				m_assert.fail("Unable to identify [LocationMapEdiorPullTab_Right]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(2);

			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("poiTwo").getLocatortype(),
					Suite.objectRepositoryMap.get("poiTwo").getLocatorvalue());

			if (element == null) {
				logger.error("Unable to identify [poiTwo]");
				ReportUtils.setStepDescription("Unable to identify [poiTwo]",
						true);
				m_assert.fail("Unable to identify [poiTwo]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(2);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
				ReportUtils
						.setStepDescription(
								"Unable to identify [LocationMapEditorDDEditRemoveEnabled]",
								true);
				m_assert.fail("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(2);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("poiOneDelete")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("poiOneDelete")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [poiOneDelete]");
				ReportUtils.setStepDescription(
						"Unable to identify [poiOneDelete]", true);
				m_assert.fail("Unable to identify [poiOneDelete]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(2);

		} else if (configproperties.get(0).equalsIgnoreCase("FIREFOX")) {
			element = SeleniumUtils.findobject(
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
				ReportUtils
						.setStepDescription(
								"Unable to identify [LocationMapEditorDDEditRemoveEnabled]",
								true);
				m_assert.fail("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
			}
			SeleniumUtils.clickOnElement(element);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("wayPoint4_firefox")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint4_firefox")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint4_firefox]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint4_firefox]", true);
				m_assert.fail("Unable to identify [wayPoint4_firefox]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(1);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
				ReportUtils
						.setStepDescription(
								"Unable to identify [LocationMapEditorDDEditRemoveEnabled]",
								true);
				m_assert.fail("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
			}
			SeleniumUtils.clickOnElement(element);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("wayPoint3_firefox")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint3_firefox")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint3_firefox]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint3_firefox]", true);
				m_assert.fail("Unable to identify [wayPoint3_firefox]");
			}
			SeleniumUtils.sleepThread(1);
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(1);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
				ReportUtils
						.setStepDescription(
								"Unable to identify [LocationMapEditorDDEditRemoveEnabled]",
								true);
				m_assert.fail("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
			}
			SeleniumUtils.clickOnElement(element);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("wayPoint2_firefox")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("wayPoint2_firefox")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint2_firefox]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint2_firefox]", true);
				m_assert.fail("Unable to identify [wayPoint2_firefox]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(1);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatortype(),
					Suite.objectRepositoryMap.get(
							"LocationMapEditorDDEditRemoveEnabled")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
				ReportUtils
						.setStepDescription(
								"Unable to identify [LocationMapEditorDDEditRemoveEnabled]",
								true);
				m_assert.fail("Unable to identify [LocationMapEditorDDEditRemoveEnabled]");
			}
			SeleniumUtils.clickOnElement(element);
			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get(
							"wayPoint1AfterEditing_firefox").getLocatortype(),
					Suite.objectRepositoryMap.get(
							"wayPoint1AfterEditing_firefox").getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [wayPoint1AfterEditing_firefox]");
				ReportUtils.setStepDescription(
						"Unable to identify [wayPoint1AfterEditing_firefox]",
						true);
				m_assert.fail("Unable to identify [wayPoint1AfterEditing_firefox]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(1);

			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("poiTwo_firefox")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("poiTwo_firefox")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [poiTwo_firefox]");
				ReportUtils.setStepDescription(
						"Unable to identify [poiTwo_firefox]", true);
				m_assert.fail("Unable to identify [poiTwo_firefox]");
			}
			SeleniumUtils.clickOnElement(element);
			SeleniumUtils.sleepThread(1);

			element = SeleniumUtils.waitForElementToIdentify(
					Suite.objectRepositoryMap.get("poiOneDelete_firefox")
							.getLocatortype(),
					Suite.objectRepositoryMap.get("poiOneDelete_firefox")
							.getLocatorvalue());
			if (element == null) {
				logger.error("Unable to identify [poiOneDelete_firefox]");
				ReportUtils.setStepDescription(
						"Unable to identify [poiOneDelete_firefox]", true);
				m_assert.fail("Unable to identify [poiOneDelete_firefox]");
			}
			SeleniumUtils.clickOnElement(element);
		}
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("saveChanges").getLocatortype(),
				Suite.objectRepositoryMap.get("saveChanges").getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [saveChanges]");
			ReportUtils.setStepDescription("Unable to identify [saveChanges]",
					true);
			m_assert.fail("Unable to identify [saveChanges]");
		}
		SeleniumUtils.clickOnElement(element);
		SeleniumUtils.sleepThread(1);
		SeleniumUtils.acceptAlertWindow();
		m_assert.assertAll();

	}

	@Test(priority = 20, dependsOnMethods = { "loginAs" })
	public void logOut() {
		// Initialize SoftAssert Object
		m_assert = new SoftAssert();
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		// Check logOut is added for execution
		boolean forExecution = false;
		for (String testcase : testcaseList) {
			if (testcase.equalsIgnoreCase("logOut")) {
				forExecution = true;
				break;
			}
		}
		if (!forExecution) {
			logger.info("Test case [logOut] is not added for execution");
			ReportUtils.setStepDescription(
					"Test case [logOut] is not added for execution", false);
			throw new SkipException(
					"Test case [logOut] is not added for execution");
		}
		logger.info("Starting [logOut] execution");
		logger.info("Navigating to [Accounts] page");
		// Identify Clients tab
		element = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("ClientTablogo")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("ClientTablogo")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Accounts] tab");
			ReportUtils.setStepDescription("Unable to identify [Accounts] tab",
					true);
			m_assert.fail("Unable to identify [Accounts] tab");
		}
		// Click on Clients tab
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click on [Accounts] tab");
			ReportUtils.setStepDescription("Unable to click on [Accounts] tab",
					true);
			m_assert.fail("Unable to click on [Accounts] tab");
		}
		// Identify Sign Out button
		element = SeleniumUtils.findobject(
				Suite.objectRepositoryMap.get("ClientTabSignOut")
						.getLocatortype(),
				Suite.objectRepositoryMap.get("ClientTabSignOut")
						.getLocatorvalue());
		if (element == null) {
			logger.error("Unable to identify [Accounts tab - Sign Out] button");
			ReportUtils
					.setStepDescription(
							"Unable to identify [Accounts tab - Sign Out] button",
							true);
			m_assert.fail("Unable to identify [Accounts tab - Sign Out] button");
		}
		logger.info("Click on Sign Out button");
		isClicked = SeleniumUtils.clickOnElement(element);
		if (!isClicked) {
			logger.error("Unable to click [Accounts tab - Sign Out] button");
			ReportUtils.setStepDescription(
					"Unable to click [Accounts tab - Sign Out] button", true);
			m_assert.fail("Unable to click [Accounts tab - Sign Out] button");
		}
		// Identify Login page text
		element = SeleniumUtils
				.findobject(Suite.objectRepositoryMap.get("LoginPageText")
						.getLocatortype(),
						Suite.objectRepositoryMap.get("LoginPageText")
								.getLocatorvalue());
		if (element == null) {
			logger.error("Sign Out unsuccessful : Unable to identify Login page elements");
			ReportUtils
					.setStepDescription(
							"Sign Out unsuccessful : Unable to identify Login page elements",
							true);
			m_assert.fail("Sign Out unsuccessful : Unable to identify Login page elements");
		}
		// Get the text
		String LoginText = SeleniumUtils.getText(element);
		// Get the Exp text
		String ExpLoginText = Suite.objectRepositoryMap.get("LoginPageText")
				.getExptext();
		// Compare both texts
		isTextMatching = SeleniumUtils.assertEqual(LoginText, ExpLoginText);
		if (!isTextMatching) {
			logger.error("Sign Out fails : The expected text in Login page is ["
					+ ExpLoginText
					+ "] and the actual return text is ["
					+ LoginText + "]");
			ReportUtils.setStepDescription("Sign Out fails", "", ExpLoginText,
					LoginText, true);
			m_assert.assertEquals(LoginText, ExpLoginText);
		}
		m_assert.assertAll();
	}

	/**
	 * This method sign out from the application
	 */
	@AfterClass
	public void tearDown() {
		// Set Author Reports
		ReportUtils.setAuthorInfoForReports();
		SeleniumUtils.closeBrowser();
	}

	/**
	 * @param testcase
	 * @return Method return the params list based on the input testcase
	 */
	public Map<String, String> getTestData(String testcase) {
		Testcase locationTestCase = (Testcase) JaxbUtil.unMarshal(
				GlobalConstants.INPUT_XML_PATH + GlobalConstants.LOCATION_FILE,
				Testcase.class);
		if (locationTestCase != null) {
			Map<String, String> testcasesMap = new HashMap<String, String>();
			List<Case> cases = locationTestCase.getCase();
			for (Case thisCase : cases) {
				String runMode = thisCase.getRunmode();
				if ("Y".equalsIgnoreCase(runMode)
						&& testcase.equalsIgnoreCase(thisCase.getName())) {
					List<Param> paramList = thisCase.getParam();
					for (Param param : paramList) {
						testcasesMap.put(param.getId(), param.getValue());
					}
				}
			}
			return testcasesMap;
		}
		return null;
	}
}
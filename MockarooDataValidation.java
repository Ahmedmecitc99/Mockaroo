package Mockaroo;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MockarooDataValidation {
	WebDriver driver;
	List<String> cities;
	List<String> countries;
	Set<String> citiesSet;


	@BeforeClass
	public void setUp() {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		String url = "https://mockaroo.com/";
		driver.get(url);
		cities = new ArrayList();
		countries = new ArrayList();
	}

	@Test
	public void verifyTitle() {
		String actual = driver.getTitle();
		String expected = "Mockaroo - Random Data Generator and API Mocking Tool | JSON / CSV / SQL / Excel";
		assertEquals(actual, expected);
	}

	@Test
	public void verifyHeader() {
		String actual = driver.findElement(By.xpath("//a[@href='/']/div[1]")).getText();
		String expected = "mockaroo";
		assertEquals(actual, expected);

		actual = driver.findElement(By.xpath("//a[@href='/']/div[2]")).getText();
		expected = "realistic data generator";
		assertEquals(actual, expected);

		removeExistingFields();
	}

	@Test
	public void verifyTableHeader() {
		assertTrue(verifyStrings());
		assertTrue(driver.findElement(By.xpath("//a[.='Add another field']")).isEnabled());

		String actual = driver.findElement(By.cssSelector("input[id='num_rows']")).getAttribute("value");
		assertEquals(actual, "1000");

		Select select = new Select(driver.findElement(By.cssSelector("select.form-control#schema_file_format")));
		actual = select.getFirstSelectedOption().getText();
		String expected = "CSV";
		assertEquals(actual, expected);

		select = new Select(driver.findElement(By.cssSelector("select.form-control#schema_line_ending")));
		actual = select.getFirstSelectedOption().getText();
		expected = "Unix (LF)";
		assertEquals(actual, expected);

		assertTrue(driver.findElement(By.xpath("//input[@name='schema[include_header]'][@value='1']")).isSelected());
		assertFalse(driver.findElement(By.xpath("//input[@name='schema[bom]'][@value='1']")).isSelected());

	}

	@Test(priority = 4)
	public void creatingFields() throws InterruptedException {
		// driver.findElement(By.xpath("//a[@class='btn btn-default add-column-btn
		// add_nested_fields']")).click();
		driver.findElement(By.xpath("//a[.='Add another field']")).click();
		driver.findElement(By.xpath("(//div[@class='column']//input[@placeholder='enter name...'])[7]"))
				.sendKeys("City");
		driver.findElement(By.xpath("(//input[@class='btn btn-default'])[7]")).click();

		Thread.sleep(2000);
		Assert.assertTrue(
				driver.findElement(By.xpath("//h3[@class='modal-title'][1]")).getText().contains("Choose a Type"));
	}

	@Test(priority = 12)
	public void searchForCity() throws InterruptedException {
		driver.findElement(By.id("type_search_field")).sendKeys("City");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@tabindex='1']")).click();
		Thread.sleep(2000);

	}

	@Test(priority = 13)
	public void addAnotherCity2() {

		driver.findElement(By.xpath("//a[@class='btn btn-default add-column-btn add_nested_fields']")).click();
		driver.findElement(By.xpath("(//input[@class='column-name form-control'][@type='text'])[8]"))
				.sendKeys("Country");

	}

	@Test(priority = 14)
	public void ChooseTypeVerification2() throws InterruptedException {

		driver.findElement(By.xpath("(//input[@class='btn btn-default'][@type='text'])[8]")).click();
		Thread.sleep(2000);
		Assert.assertTrue(
				driver.findElement(By.xpath("//h3[@class='modal-title'][1]")).getText().contains("Choose a Type"));
	}

	@Test(priority = 15)
	public void searchForCity2() throws InterruptedException {
		driver.findElement(By.id("type_search_field")).sendKeys("Country");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@tabindex='1']")).click();
		Thread.sleep(2000);

	}

	@Test(priority = 16)
	public void clickDownload() throws InterruptedException {
		driver.findElement(By.id("download")).click();

		Thread.sleep(5000);

	}

	@Test(priority = 17)
	public void openFile() throws InterruptedException, IOException {
		FileReader reader = new FileReader("/Users/primaryuser/Downloads/MOCK_DATA.csv");
		BufferedReader br = new BufferedReader(reader);

		BufferedReader breader = new BufferedReader(reader);

		String temp = breader.readLine();

		assertEquals(temp, "City,Country");

		int count = 0;
		temp = breader.readLine();
		String country, city;
		String[] something = new String[2];
		while (temp != null) {
			something = temp.split(",");
			cities.add(something[0]);
			countries.add(something[1]);
			count++;
			temp = breader.readLine();
			
		}
		
		assertEquals(count, 1000);
		SortCities();
		findCountries();
		uniqueCities() ;
		
		CountiresUnique() ;

	}

	public void removeExistingFields() {
		List<WebElement> fields = driver
				.findElements(By.xpath("//a[@class='close remove-field remove_nested_fields']"));

		for (WebElement webElement : fields) {
			webElement.click();
		}
	}

	public boolean verifyStrings() {
		List<WebElement> acutal = driver.findElements(By.xpath("//div[@class='table-header']//div"));
		List<String> expected = new ArrayList<>();
		expected.add("Field Name");
		expected.add("Type");
		expected.add("Options");

		for (int i = 0; i < acutal.size(); i++) {
			if (!acutal.get(i).getText().equals(expected.get(i))) {
				return false;
			}
		}
		return true;
	}
	public void SortCities() {
		Collections.sort(cities);
		int max=cities.get(0).length();
		for(String string :cities) {
		if(string.length()>max){	
			max=string.length();
			
		}
	}
		int min=cities.get(0).length();
		for(String string :cities) {
			if(string.length()<min){	
				min=string.length();
				
			}
			
	}
		System.out.println("max lenght"+ max);
		System.out.println("min lenght"+ min);
	}
	
	
	public void findCountries() {
		int count=0;
		Set<String> k=new HashSet<>(countries);
		for (String outer : k) {
		
			for (String inner : countries) {
				if(inner.equals(outer))
					count++;
			}
			System.out.println(outer+ "-"+count);
			count=0;
		}
	}
	
	public void uniqueCities() {
		Set<String> citiesSet = new HashSet<>(cities);

		int uniqueCityCount = 0;

		for (int i = 0; i < cities.size(); i++) {
			if (i == cities.lastIndexOf(cities.get(i)))
				uniqueCityCount++;
		}

		System.out.println("Unique city count by for loop: " + uniqueCityCount);
		System.out.println("Unique city count by HashSet: " + citiesSet.size());

		assertEquals(uniqueCityCount, citiesSet.size());
	}

	
	
	
	public void CountiresUnique() {
				Set<String> countrySet = new HashSet<>(countries);

		
				int uniqueCountryCount = 0;

		for (int i = 0; i < countries.size(); i++) {
			if (i == countries.lastIndexOf(countries.get(i)))
				uniqueCountryCount++;
		}

		System.out.println("Unique country count by for loop: " + uniqueCountryCount);
		System.out.println("Unique country count by HashSet: " + countrySet.size());

		assertEquals(uniqueCountryCount, countrySet.size());
	}


	
	
	
	
	
	
	@AfterClass
	 public void driverClose() {
	 driver.close();

	 }
}



package test;

import java.io.IOException;
import java.time.*;
import java.util.*;
import java.util.stream.IntStream;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import utils.JsonUtils;

public class RechercheSimple {

	
	protected static WebDriver driver;
	
	protected static List<Map<String, Object>> toutesDonnees;
	
	protected static List<Map<String, Object>> donneesSelectionnees;
	
	
	public static void lancerRechercheSimple(Map<String, Object> data) throws InterruptedException {

		
		System.setProperty("webdriver.chroms.driver", "/Users/emilietossan/chromedriver/mac_arm-144.0.7522.0/chromedriver-mac-arm64/chromedriver");
		
		driver = new ChromeDriver();
		
		driver.get("https://www.airbnb.fr/");
		
		driver.manage().window().maximize();
		
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		
		
		WebElement acceptCookieButton = driver.findElement(By.xpath("//button[contains(text(), 'Accepter')]"));
		
		acceptCookieButton.click();
		
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		
		driver.findElement(By.cssSelector("input[name='query']")).sendKeys((String) data.get("ville"));
		
		Thread.sleep(2000);
		
		driver.findElement(By.xpath("//div[@id='bigsearch-query-location-listbox']/div[1]")).click();
		
		
		Thread.sleep(1000);
		
		
		LocalDate todayDate = LocalDate.now();
		
		LocalDate departureDate = todayDate.plusDays((Integer) data.get("jours"));
		
		driver.findElement(By.xpath("//button[contains(@data-state--date-string, '" + todayDate + "')]")).click();
		
		driver.findElement(By.xpath("//button[contains(@data-state--date-string, '" + departureDate + "')]")).click();
		
		
		Thread.sleep(1000);
		
		
		driver.findElement(By.xpath("//div[contains(text(), 'Voyageurs')][1]")).click();
		
		WebElement addTraveller = driver.findElement(By.xpath("//h3[contains(text(), 'Adultes')]/ancestor::div[@data-testid='search-block-filter-stepper-row-adults']/div[@id='stepper-adults']/button[2]"));
		IntStream.range(0, (Integer) data.get("voyageurs")).forEach(i -> addTraveller.click());
		
		driver.findElement(By.xpath("//div[contains(text(), 'Rechercher')]/parent::div")).click();
		
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		toutesDonnees = JsonUtils.lireDonneesDepuisJSON();
		
		donneesSelectionnees = JsonUtils.selectionnerDonnees(toutesDonnees, "smoke");
	
		for (Map<String, Object> data : donneesSelectionnees) lancerRechercheSimple(data); 
		
	}

}

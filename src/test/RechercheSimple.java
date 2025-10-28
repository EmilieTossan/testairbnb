package test;

import java.time.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;

public class RechercheSimple {

	public static void main(String[] args) throws InterruptedException {

		System.setProperty("webdriver.chrome.driver","/Users/emilietossan/Documents/chromedriver");
		
		// Prépare l'ouverture de la machine sur Chrome
		WebDriver driver = new ChromeDriver();

		// Va sur AirBnb
		driver.get("https://www.airbnb.fr/");
		
		// Airbnb a besoin un certain temps pour s'ouvrir, au moins 30 secondes
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(45));
		
		String btnAccept = "//button[contains(text(),'Accepter')]";

		// Accepte les cookies nécessaires
		if(driver.findElement(By.xpath(btnAccept)) != null) {
			
			driver.findElement(By.xpath(btnAccept)).click();
		}
		
		// Laisse un certain temps après avoir accepté les cookies
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		// Saisie "Paris" dans Destination		
		driver.findElement(By.cssSelector("input[name='query']")).sendKeys("Paris");

		// driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		Thread.sleep(5000);
		
		// Sélectionne sur "Paris"
		driver.findElement(By.xpath("//div[@id='bigsearch-query-location-listbox']/div[1]")).click();
		
		LocalDate todayDate = LocalDate.now();
		
		// Sélectionne sur la date d'aujourd'hui
		driver.findElement(By.xpath("//button[contains(@data-state--date-string, '" + todayDate + "')]")).click();
		
		LocalDate dayAfterTomorrow = todayDate.plusDays(2);
		
		// Sélectionne deux jours après
		driver.findElement(By.xpath("//button[contains(@data-state--date-string, '" + dayAfterTomorrow + "')]")).click();
		
		// Va sur la section Ajout des voyageurs
		driver.findElement(By.xpath("//div[contains(text(),'Voyageurs')][1]")).click();
		
		// Ajoute un adulte
		driver.findElement(By.xpath("//h3[contains(text(),'Adultes')]/parent::*/parent::*/parent::*/div[2]/button[2]")).click();
		
		// Clique sur Rechercher
		driver.findElement(By.xpath("//div[contains(text(),'Rechercher')]/parent::div")).click();
	}

}

package test;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class RechercheSimple {

	public static void main(String[] args) throws InterruptedException {

		System.setProperty("webdriver.chrome.driver","/Users/emilietossan/Documents/chromedriver");
		WebDriver driver = new ChromeDriver();
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		// Va sur AirBnb
		driver.get("https://www.airbnb.fr/");
		
		// Accepte les cookies nécessaires
		driver.findElement(By.xpath("//section[contains(@class,'imqckrr')]/div/div[2]/div[2]/button")).click();
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
		// Saisie "Paris" dans Destination
		driver.findElement(By.xpath("//input[contains(@class,'fp9kp52')]")).sendKeys("Paris");
		
		// Sélectionne sur "Paris"
		driver.findElement(By.xpath("//div[contains(@class,'piqlc25')]/div/div[1]")).click();
		
		// Sélectionne sur la date d'aujourd'hui
		driver.findElement(By.xpath("//div[contains(@class, 'e1y5p8jl')]/div[1]/button[contains(@aria-label, 'Aujourd')]")).click();
		
		// Sélectionne deux jours après
		driver.findElement(By.xpath("//div[contains(@class, 'e1y5p8jl')]/div[1]/button[contains(@aria-label, 'Aujourd')]/following-sibling::button[2]")).click();
	}

}
